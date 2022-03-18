package com.genesis.coincapcheck.demo.dto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyWalletDTO {
	public MyWalletDTO(List<String> rawData) {
		this.symbol = rawData.get(0);
		this.quantity = new BigDecimal(rawData.get(1)).setScale(2, RoundingMode.HALF_UP);
		this.price = new BigDecimal(rawData.get(2)).setScale(2, RoundingMode.HALF_UP);
	}
	
	String symbol;
	BigDecimal quantity;
	BigDecimal price;
}
