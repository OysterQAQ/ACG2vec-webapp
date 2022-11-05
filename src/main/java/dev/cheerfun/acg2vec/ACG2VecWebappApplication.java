package dev.cheerfun.acg2vec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableScheduling
@EnableCaching
@EnableTransactionManagement

public class ACG2VecWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(ACG2VecWebappApplication.class, args);
    }

}
