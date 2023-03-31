package xyz.liujin.start;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.debug.DebugConfig;

@SpringBootApplication
public class StartApplication {
	@Autowired
	private DebugConfig debugConfig;

	public static void main(String[] args) {
		SpringApplication.run(StartApplication.class, args);
	}

}
