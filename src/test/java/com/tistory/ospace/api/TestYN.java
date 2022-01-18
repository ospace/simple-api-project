package com.tistory.ospace.api;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.tistory.ospace.api.util.YN;

public class TestYN {
	private static final Logger logger = LoggerFactory.getLogger(TestYN.class);
	
	@Test
	public void testYN() {
		YN val = YN.Y;
		
		logger.info("value[{}] value[{}]", val.name(), val.toString());
		logger.info("yn[{}]", Enum.valueOf(YN.class, "N").toString());
	}
}
