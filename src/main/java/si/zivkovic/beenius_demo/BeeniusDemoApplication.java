package si.zivkovic.beenius_demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

//@EnableCaching
@SpringBootApplication
public class BeeniusDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BeeniusDemoApplication.class, args);
	}

}
