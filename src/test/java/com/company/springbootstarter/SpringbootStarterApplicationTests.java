package com.company.springbootstarter;

import com.company.springbootstarter.controller.HelloController;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@SpringBootTest
class SpringbootStarterApplicationTests {

    @Resource
    private ApplicationContext ctx;

    @Test
    void contextLoads() {
        System.out.println(ctx);

         HelloController helloController =  ctx.getBean(HelloController.class);
         System.out.println("helloController = " + helloController);
    }

}
