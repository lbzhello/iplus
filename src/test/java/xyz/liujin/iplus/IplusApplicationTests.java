package xyz.liujin.iplus;

import com.google.common.cache.Cache;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IplusApplicationTests {
	@Autowired
	private Cache<String, Object> cache;

	@Test
	void contextLoads() {
		System.out.println();
	}

}
