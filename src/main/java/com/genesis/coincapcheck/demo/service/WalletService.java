package com.genesis.coincapcheck.demo.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.genesis.coincapcheck.demo.dto.AssetHistoryResponseDTO;
import com.genesis.coincapcheck.demo.dto.MyWalletDTO;
import com.genesis.coincapcheck.demo.dto.RatesResultDTO;
import com.genesis.coincapcheck.demo.dto.WalletResultDTO;
import com.genesis.coincapcheck.demo.rest.CoinCapRestAdapter;
import com.genesis.coincapcheck.demo.utils.Constants;
import com.genesis.coincapcheck.demo.utils.FileUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WalletService {
	
	@Autowired
	private FileUtils fileUtils;
	
	@Autowired
	private CoinCapRestAdapter coinCapRestAdapter;
	
	RatesResultDTO rates;
	
	public WalletResultDTO checkMyPositionOnCoinCap() throws FileNotFoundException, IOException, InterruptedException, ExecutionException, TimeoutException {
		
		List<MyWalletDTO> myWallet = openLocalWallet();
		
		return calculateCoinCapCheckResults(myWallet);
	}
	
	public Map<String, AssetHistoryResponseDTO> normalizeMapResponse(Map<String, AssetHistoryResponseDTO> coinCapMapResponse, Map<String, String>idToSymbolMap){
		Map<String, AssetHistoryResponseDTO> normalized = new HashMap<String, AssetHistoryResponseDTO>();
		
		for(Map.Entry<String, AssetHistoryResponseDTO> item: coinCapMapResponse.entrySet()) {
			normalized.put(idToSymbolMap.get(item.getKey()), item.getValue());
		}
		
		return normalized;
	}
	
	public Map<String, String> createIdToSymbolReference(RatesResultDTO rates, List<MyWalletDTO> myWallet){
		Map<String, String> idToSymbolMap = new HashMap<String, String>(); 
		List<String> myWalletSymbols = myWallet.stream().map(asset -> asset.getSymbol()).collect(Collectors.toList());
		
		rates.getData().stream().filter(rate -> myWalletSymbols.contains(rate.getSymbol()))
											.forEach(rate -> idToSymbolMap.put(rate.getId(), rate.getSymbol()));
		
		return idToSymbolMap;
	}
	
	public WalletResultDTO calculateCoinCapCheckResults(List<MyWalletDTO> myWallet) throws InterruptedException, ExecutionException, TimeoutException{
		rates = getCoinCapRatesInfo();
		Map<String, AssetHistoryResponseDTO> coinCapMapResponse = new HashMap<String, AssetHistoryResponseDTO>();	
		Map<String, String> idToSymbolMap = createIdToSymbolReference(rates, myWallet);
		
		log.info("Now its: {}", LocalTime.now());
		coinCapMapResponse.putAll(getAsyncCoinCapAssetHistoryInfoByListIds(idToSymbolMap.keySet()));
		coinCapMapResponse = normalizeMapResponse(coinCapMapResponse, idToSymbolMap);
	
		Map<String, MyWalletDTO> myWalletHash = new HashMap<String, MyWalletDTO>();
		myWallet.stream().forEach(asset -> myWalletHash.put(asset.getSymbol(), asset));
		
		return createWalletResult(myWalletHash, coinCapMapResponse);
		
	}
	
	public WalletResultDTO createWalletResult(Map<String, MyWalletDTO>myWalletHash, Map<String, AssetHistoryResponseDTO> coinCapMapResponse) {
		Map<String, BigDecimal> position = new HashMap<String, BigDecimal>();
		Map<String, BigDecimal> coinCapMap = new HashMap<String, BigDecimal>();
		
		coinCapMapResponse.entrySet().stream().forEach(asset -> {position.put(asset.getKey(), 
														myWalletHash.get(asset.getKey()).getQuantity()
														.multiply(asset.getValue().getData().get(0).getPriceUsd()));
													    coinCapMap.put(asset.getKey(),
													    asset.getValue().getData().get(0).getPriceUsd());
														});
		
		
		BigDecimal total = position.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
		
		Map<String, BigDecimal> performance = new HashMap<String, BigDecimal>();
		
		position.keySet().stream().forEach(posKey -> performance.put(posKey, 
																	 coinCapMap.get(posKey).multiply(Constants.BIG_DECIMAL_100).divide(myWalletHash.get(posKey).getPrice(), 2, RoundingMode.HALF_UP)));
		
		log.info("size:{}",coinCapMapResponse.size());
		log.info("Total:{} ", total);
		
		performance.keySet().stream().forEach(perKey -> log.info("performance: {}", performance.get(perKey)));
		
		Entry<String, BigDecimal> best = maxPerformanceFromMap(performance).orElseGet(null);
		Entry<String, BigDecimal> worst = minPerformanceFromMap(performance).orElseGet(null);
		
		WalletResultDTO walletResult = new WalletResultDTO();
		walletResult.setTotal(total);
		walletResult.setBest_asset(best.getKey());
		walletResult.setBest_performance(best.getValue());
		walletResult.setWorst_asset(worst.getKey());
		walletResult.setWorst_performance(worst.getValue());
		
		return walletResult;
	}
	
	public Map<String, AssetHistoryResponseDTO> getAsyncCoinCapAssetHistoryInfoByListIds(Set<String> ids){
		Map<String, AssetHistoryResponseDTO> responseMap = new HashMap<String, AssetHistoryResponseDTO>();
		
	    List<CompletableFuture<CompletableFuture<Map<String, AssetHistoryResponseDTO>>>> futures = ids.stream()
	            .map(id -> CompletableFuture.completedFuture(id).thenApply(s -> coinCapRestAdapter.getAsyncCoinCapAssetHistoryInfoById(s)))
	            .collect(Collectors.toList());
	    CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()])).whenComplete((v, th) -> {
	        futures.forEach(cf -> {
				try {
					responseMap.putAll(cf.get().get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			});
	    });
		
	    return responseMap;
	
	}
	
	public Optional<Entry<String, BigDecimal>> maxPerformanceFromMap(Map<String, BigDecimal> map) {
	    Optional<Entry<String, BigDecimal>> maxEntry = map.entrySet()
	        .stream()
	        .max((Entry<String, BigDecimal> e1, Entry<String, BigDecimal> e2) -> e1.getValue()
	            .compareTo(e2.getValue())
	        );
	    
	    return maxEntry;
	}

	public Optional<Entry<String, BigDecimal>> minPerformanceFromMap(Map<String, BigDecimal> map) {
	    Optional<Entry<String, BigDecimal>> minEntry = map.entrySet()
	        .stream()
	        .min((Entry<String, BigDecimal> e1, Entry<String, BigDecimal> e2) -> e1.getValue()
	            .compareTo(e2.getValue())
	        );
	    
	    return minEntry;
	}

	public RatesResultDTO getCoinCapRatesInfo() {
		if(!Objects.isNull(rates)) {
			return rates;
		}
		
		return coinCapRestAdapter.getCoinCapRatesInfo();
	}
	
	public List<MyWalletDTO> openLocalWallet() throws FileNotFoundException, IOException{
		List<List<String>> myWalletRawData = fileUtils.openFile(Constants.FILE_WALLET);
		
		List<MyWalletDTO> myWalletList = myWalletRawData.stream().map(rawData -> new MyWalletDTO(rawData)).collect(Collectors.toList());
		
		myWalletList.stream().forEach(wallet -> log.info(wallet.getSymbol()+ "-"+ wallet.getPrice() + "-" + wallet.getQuantity()));
		
		return myWalletList;
	}
}
