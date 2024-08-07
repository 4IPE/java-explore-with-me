package service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"service", "ru.practicum.clients"})
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
