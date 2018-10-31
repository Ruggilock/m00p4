package absortio.m00p4.systemSpring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@ComponentScan(basePackages = "absortio.m00p4.negocio")
@EnableJpaRepositories(basePackages = "absortio.m00p4.negocio.repository")
public class ApplicationConfiguration {
}

