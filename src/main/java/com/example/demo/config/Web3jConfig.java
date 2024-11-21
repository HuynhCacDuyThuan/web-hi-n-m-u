package com.example.demo.config;

import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.util.concurrent.TimeUnit;

@Configuration
public class Web3jConfig {

    @Bean
    public Web3j web3j() {
     
        // Create a Web3j instance with a custom OkHttpClient
        return Web3j.build(new HttpService("http://127.0.0.1:8545"));
    }
}
