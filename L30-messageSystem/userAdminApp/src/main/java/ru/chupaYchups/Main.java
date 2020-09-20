package ru.chupaYchups;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.chupaYchups.properties.MessageSystemClientNameProps;

@SpringBootApplication
@EnableConfigurationProperties({MessageSystemClientNameProps.class})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
