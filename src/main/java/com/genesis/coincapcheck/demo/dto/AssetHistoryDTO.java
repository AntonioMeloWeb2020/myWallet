package com.genesis.coincapcheck.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssetHistoryDTO {
	public BigDecimal priceUsd;
	public Long time;
	public LocalDateTime date;
}
