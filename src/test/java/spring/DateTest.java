package spring;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateTest {
	private static final Logger logger = LoggerFactory.getLogger(DateTest.class);
	@Test
	public void testPath() {
		Path path = Paths.get("pathA/pathB/foo");
		
		logger.info("path[{}] folder[{}] grandparent[{}]", path, path.getParent(), path.getParent().getParent());
		
		logger.info("absolute[{}]", path.toAbsolutePath());
	}
}
