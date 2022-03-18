package com.genesis.coincapcheck.demo.rest;

import java.time.LocalTime;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.genesis.coincapcheck.demo.dto.AssetHistoryResponseDTO;
import com.genesis.coincapcheck.demo.dto.AssetResponseDTO;
import com.genesis.coincapcheck.demo.dto.RatesResultDTO;
import com.genesis.coincapcheck.demo.utils.Constants;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CoinCapRestAdapter {

	@Autowired
	private RestTemplate restTemplate;
	
	public RatesResultDTO getCoinCapRatesInfo() {	
		ResponseEntity<RatesResultDTO> responseEntity = restTemplate.exchange(
			    Constants.COIN_CAP_RATES_URL,
			    HttpMethod.GET,
			    null,
			    RatesResultDTO.class);
		
	    log.info("ResponseEntity Rates: {}",responseEntity);
	    return responseEntity.getBody();
	}
	
	public AssetHistoryResponseDTO getCoinCapAssetHistoryInfoById(String id) {
		AssetHistoryResponseDTO response = restTemplate.getForObject(
				Constants.COIN_CAP_ASSET_HISTORY_URL, 
				AssetHistoryResponseDTO.class, id);
		
		System.out.println(response);
		return response;		
	}
	
    @Async
    public CompletableFuture<Map<String, AssetHistoryResponseDTO>> getAsyncCoinCapAssetHistoryInfoById(String id) {
    	log.info("Submitted request for Asset {}:  {}", id, LocalTime.now()); 
    	
    	AssetHistoryResponseDTO response = restTemplate.getForObject(
				Constants.COIN_CAP_ASSET_HISTORY_URL, 
				AssetHistoryResponseDTO.class, id);
    	log.info("Async asset History response: {}", response);
        return CompletableFuture.completedFuture(Collections.singletonMap(id, response));
    }
	
	public AssetResponseDTO getCoinCapAssetInfoById(String id) {
		
		AssetResponseDTO response = restTemplate.getForObject(
				Constants.COIN_CAP_ASSET_URL, 
				AssetResponseDTO.class, id);
		
		log.info("response for Id: {} -> {}", id, response);
		return response;
			
	}
}
