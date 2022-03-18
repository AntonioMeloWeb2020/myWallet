package com.genesis.coincapcheck.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResultDTO {
	BigDecimal total;
	String best_asset;
	BigDecimal best_performance;
	String worst_asset;
	BigDecimal worst_performance;
	
}
