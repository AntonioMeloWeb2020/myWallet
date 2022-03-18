package com.genesis.coincapcheck.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponseDTO {
	AssetDTO data;
	Long timestamp;
}
