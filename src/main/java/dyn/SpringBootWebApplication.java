package dyn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * Created by OM on 12.11.2017.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class SpringBootWebApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        System.out.println("Started!!!!!!");
        SpringApplication.run(SpringBootWebApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(SpringBootWebApplication.class);
    }
}
