package ua.hlibbabii.langreader;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

import static org.springframework.boot.SpringApplication.run;

/**
 * Created by hlib on 02.04.16.
 */
@Configuration
@ImportResource("classpath:spring/spring.xml")
@EnableAutoConfiguration
@ComponentScan
public class Startup {

    public static void main(String[] args) {
        run(Startup.class, args);
    }

}
