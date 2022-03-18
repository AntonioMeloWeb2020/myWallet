package com.genesis.coincapcheck.demo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.genesis.coincapcheck.demo.dto.AssetHistoryDTO;
import com.genesis.coincapcheck.demo.dto.AssetHistoryResponseDTO;
import com.genesis.coincapcheck.demo.dto.RatesDTO;

public class TestUtils {
	public static AssetHistoryResponseDTO generateAssetHistoryResponseDTO() {
		AssetHistoryResponseDTO response = new AssetHistoryResponseDTO();
		List<AssetHistoryDTO> list = new ArrayList<AssetHistoryDTO>();
		AssetHistoryDTO asset = new AssetHistoryDTO();
		asset.setDate(LocalDateTime.now());
		asset.setPriceUsd(new BigDecimal("1000.001"));
		asset.setTime(1111L);
		list.add(asset);
		response.setData(list);
		response.setTimestamp(1111L);
		
		return response;
	}
	
	public static List<RatesDTO> generateRatesDTOList() {
		List<RatesDTO> list = new ArrayList<RatesDTO>();
		
		RatesDTO ratesDto1 = new RatesDTO();
    	ratesDto1.setCurrencySymbol("BTC");
    	ratesDto1.setId("Bitcoin");
    	ratesDto1.setRateUsd(new BigDecimal("36000.0003"));
    	ratesDto1.setType("type");
 
       	RatesDTO ratesDto2 = new RatesDTO();
    	ratesDto2.setCurrencySymbol("ETH");
    	ratesDto2.setId("Ethereum");
    	ratesDto2.setRateUsd(new BigDecimal("36000.0003"));
    	ratesDto2.setType("type");
    	
    	list.add(ratesDto1);
    	list.add(ratesDto2);
    	
    	return list;
 
	}

}
