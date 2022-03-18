package com.genesis.coincapcheck.demo.api;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.genesis.coincapcheck.demo.TestUtils;
import com.genesis.coincapcheck.demo.dto.AssetHistoryResponseDTO;
import com.genesis.coincapcheck.demo.dto.RatesResultDTO;
import com.genesis.coincapcheck.demo.rest.CoinCapRestAdapter;
import com.genesis.coincapcheck.demo.service.WalletService;
import com.genesis.coincapcheck.demo.utils.Constants;

@SpringBootTest
public class CoinCapAPITest {

	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private CoinCapRestAdapter coinCapRestAdapter;
	
	@InjectMocks
	private WalletService walletService;

	@Test 
	public void getCoinCapRatesTest() {
		RatesResultDTO ratesResult = new RatesResultDTO();
		ratesResult.setData(TestUtils.generateRatesDTOList());		
		ResponseEntity<RatesResultDTO> response = ResponseEntity.ok(ratesResult);

		Mockito.when(restTemplate.exchange( Constants.COIN_CAP_RATES_URL,
			    HttpMethod.GET,
			    null,
			    RatesResultDTO.class)).thenReturn(response);
		RatesResultDTO result = coinCapRestAdapter.getCoinCapRatesInfo();
		assertThat(result.getData().size()==2);
	}
	
	@Test
	public void getCoinCapAssetHistoryInfoTest() {
		AssetHistoryResponseDTO assetHistory = TestUtils.generateAssetHistoryResponseDTO();
		
		Mockito.when(restTemplate.getForObject(
				Constants.COIN_CAP_ASSET_HISTORY_URL, 
				AssetHistoryResponseDTO.class, "abc")).thenReturn(assetHistory);
		assertThat(assetHistory.getData().size()==1);
		
	}
	
	public AssetHistoryResponseDTO getCoinCapAssetHistoryInfoById(String id) {
		AssetHistoryResponseDTO response = restTemplate.getForObject(
				Constants.COIN_CAP_ASSET_HISTORY_URL, 
				AssetHistoryResponseDTO.class, id);
		
		System.out.println(response);
		return response;		
	}
		
}