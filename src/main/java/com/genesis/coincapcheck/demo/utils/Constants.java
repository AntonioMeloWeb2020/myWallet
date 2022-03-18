package com.genesis.coincapcheck.demo.utils;

import java.math.BigDecimal;

public class Constants {
	public static String FILE_WALLET = "wallet.csv";
	public static String COMMA_DELIMITIER = ",";
	public static String COIN_CAP_RATES_URL = "https://api.coincap.io/v2/rates";
	public static String COIN_CAP_ASSET_URL = "https://api.coincap.io/v2/assets/{id}";
	public static String INTERVAL_TO_TEST = "interval=d1&start=1617753600000&end=1617753601000";
	public static String COIN_CAP_ASSET_HISTORY_URL = "https://api.coincap.io/v2/assets/{id}/history?"+INTERVAL_TO_TEST;
	
	public static Integer ASYNC_THREAD_MAX_POOL = 3;
	
	public static BigDecimal BIG_DECIMAL_100 = new BigDecimal("100");
}
