package pl.books.magagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BooksMagagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(BooksMagagementApplication.class, args);
	}

}
