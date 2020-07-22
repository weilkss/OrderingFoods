package cn.com.diancan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableScheduling
@MapperScan("cn.com.diancan.mapper")
public class DiancanApplication {

    public static void main(String[] args) {
        SpringApplication.run(DiancanApplication.class, args);
    }

    /**
     * 解决跨域问题
     */
    @Bean
    public WebMvcConfigurer restConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry  registry) {
                registry.addMapping("/**/*").allowedOrigins("*").allowedMethods("*").allowCredentials(true);
            }
        };
    }
}
