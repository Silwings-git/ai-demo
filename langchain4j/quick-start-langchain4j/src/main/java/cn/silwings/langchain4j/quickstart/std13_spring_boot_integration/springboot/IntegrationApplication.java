package cn.silwings.langchain4j.quickstart.std13_spring_boot_integration.springboot;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 对应依赖:
 * <dependency>
 * <groupId>dev.langchain4j</groupId>
 * <artifactId>langchain4j-open-ai-spring-boot-starter</artifactId>
 * </dependency>
 */
@SpringBootApplication
public class IntegrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(IntegrationApplication.class, args);
    }
}