package pl.books.magagement;

import org.springframework.boot.SpringApplication;

public class TestBooksMagagementApplication {

	public static void main(String[] args) {
		SpringApplication.from(BooksMagagementApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
