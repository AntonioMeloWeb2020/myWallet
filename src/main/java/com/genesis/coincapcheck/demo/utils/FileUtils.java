package com.genesis.coincapcheck.demo.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class FileUtils {
	public List<List<String>> openFile(String fileName) throws FileNotFoundException, IOException {
		List<List<String>> records = new ArrayList<>();
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(Constants.FILE_WALLET);

		InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
		
		try (BufferedReader br = new BufferedReader(streamReader)) {
		    String line;
		    //skip first line
		    br.readLine();
		    
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(Constants.COMMA_DELIMITIER);
		        records.add(Arrays.asList(values));
		    }
		}
		
		return records;
	}
}
