package top.iseason.metaworldeducation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableCaching
@SpringBootApplication
@EnableTransactionManagement
public class MetaWorldEducationApplication {

    public static void main(String[] args) {
        SpringApplication.run(MetaWorldEducationApplication.class, args);
    }

}
