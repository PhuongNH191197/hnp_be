package com.elcom.plus.user.client;

import feign.Request;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MeCallConfiguration implements RequestInterceptor {
    @Bean
    public Request.Options options(){
        return new Request.Options(10000, 60000, false);
    }

    @Override
    public void apply(RequestTemplate requestTemplate) {

    }
}
