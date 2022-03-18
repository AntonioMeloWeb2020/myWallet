package com.genesis.coincapcheck.demo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetHistoryResponseDTO {
	public List<AssetHistoryDTO> data;
	public Long timestamp;
}
