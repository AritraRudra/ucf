package in.co.test.ucf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableJpaAuditing
public class UcfApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(UcfApplication.class);

	public static void main(final String[] args) {
		SpringApplication.run(UcfApplication.class, args);
	}

}
