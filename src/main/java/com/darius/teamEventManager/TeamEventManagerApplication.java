package com.darius.teamEventManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class TeamEventManagerApplication {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(TeamEventManagerApplication.class, args);
        System.out.println("##### BEANS #####");
        for (String bean : ctx.getBeanDefinitionNames()) {
            System.out.println(bean);
        }
    }

}
