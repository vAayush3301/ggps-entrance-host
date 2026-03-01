package av.entrance.host.host;

import av.entrance.host.host.config.FirebaseConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.temporal.Temporal;

@SpringBootApplication
public class HostApplication {

	public static Instant START_TIME = Instant.now();

	public static void main(String[] args) {
		SpringApplication.run(HostApplication.class, args);
	}
}