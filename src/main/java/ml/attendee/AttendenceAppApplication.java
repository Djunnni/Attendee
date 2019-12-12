package ml.attendee;

import javax.persistence.EntityListeners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;

@ComponentScan
@SpringBootApplication
@EnableJpaAuditing
@EnableAutoConfiguration
@EntityListeners(AuditingEntityListener.class)
public class AttendenceAppApplication extends SpringBootServletInitializer{

	@Bean
    public Java8TimeDialect java8TimeDialect() {
        return new Java8TimeDialect();
    }
	
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AttendenceAppApplication.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(AttendenceAppApplication.class, args);
	}
  
}
