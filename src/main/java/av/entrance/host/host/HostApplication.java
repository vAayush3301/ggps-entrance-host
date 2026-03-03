package av.entrance.host.host;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Instant;

@EnableAsync
@SpringBootApplication
public class HostApplication {

    public static Instant START_TIME = Instant.now();

    public static void main(String[] args) {
        SpringApplication.run(HostApplication.class, args);
    }
}