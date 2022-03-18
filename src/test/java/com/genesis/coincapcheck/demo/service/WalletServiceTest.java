package com.genesis.coincapcheck.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.genesis.coincapcheck.demo.dto.MyWalletDTO;
import com.genesis.coincapcheck.demo.rest.CoinCapRestAdapter;
import com.genesis.coincapcheck.demo.utils.FileUtils;

@SpringBootTest
public class WalletServiceTest {

	@Mock
	private CoinCapRestAdapter coinCapRestAdapter;
	@Mock
	private FileUtils fileUtils;
	
	@InjectMocks
	private WalletService walletService;
	
	@Test
	public void testMinPerformance() {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		map.putAll(Collections.singletonMap("BTC", new BigDecimal("100")));
		map.putAll(Collections.singletonMap("ETH", new BigDecimal("200")));
		Optional<Map.Entry<String, BigDecimal>> min = walletService.minPerformanceFromMap(map);
		assertThat("BTC".equals(min.get().getKey()));
	}
	
	@Test
	public void testMaxPerformance() {
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		map.putAll(Collections.singletonMap("BTC", new BigDecimal("100")));
		map.putAll(Collections.singletonMap("ETH", new BigDecimal("200")));
		Optional<Map.Entry<String, BigDecimal>> min = walletService.maxPerformanceFromMap(map);
		assertThat("ETH".equals(min.get().getKey()));
	}
	
	@Test
	public void openLocalWallet() throws FileNotFoundException, IOException {
		List<MyWalletDTO> wallet = walletService.openLocalWallet();
		assertThat(wallet.size() == 2);
	}

}
