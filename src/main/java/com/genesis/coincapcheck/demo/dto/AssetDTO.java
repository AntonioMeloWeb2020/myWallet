package com.genesis.coincapcheck.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDTO {
	String id;
	Integer rank;
	String symbol;
	String name;
	BigDecimal supply;
	BigDecimal maxSupply;
	BigDecimal marketCapUsd;
	BigDecimal volumeUsd24Hr;
	BigDecimal priceUsd;
	BigDecimal changePercent24Hr;
	BigDecimal vwap24Hr;
	String explorer;
}
