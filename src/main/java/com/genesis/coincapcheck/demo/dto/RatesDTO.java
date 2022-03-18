package com.genesis.coincapcheck.demo.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatesDTO {
	String id;
	String symbol;
	String currencySymbol;
	String type;
	BigDecimal rateUsd;
}
