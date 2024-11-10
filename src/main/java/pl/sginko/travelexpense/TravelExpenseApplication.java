package pl.sginko.travelexpense;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class TravelExpenseApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelExpenseApplication.class, args);
	}
}
