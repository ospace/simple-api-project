package spring;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaTest {
    private static final Logger logger = LoggerFactory.getLogger(JavaTest.class);
    
    @Test
    public void testInteger() {
        Integer val = 10;
        
        String str = val.toString();
        
        logger.info("val vs str: {}, {}", val, str);
     
        assertThat("10".equals(str)).isTrue();
        assertThat(str).isEqualTo("10");
    }
}
