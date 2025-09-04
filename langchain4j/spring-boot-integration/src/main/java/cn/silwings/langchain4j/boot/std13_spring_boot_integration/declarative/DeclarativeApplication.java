package cn.silwings.langchain4j.boot.std13_spring_boot_integration.declarative;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 对应依赖:
 * <dependency>
 * <groupId>dev.langchain4j</groupId>
 * <artifactId>langchain4j-spring-boot-starter</artifactId>
 * </dependency>
 */
@SpringBootApplication
public class DeclarativeApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeclarativeApplication.class, args);
    }
}
