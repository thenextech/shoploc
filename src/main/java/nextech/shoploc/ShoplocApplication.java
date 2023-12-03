package nextech.shoploc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ShoplocApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoplocApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("http://localhost:3000", "http://172.28.100.230:30100")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
                ;
            }
        };
    }

}