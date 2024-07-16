package com.bankingAppDemo.bankingAppDemo.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;
@Configuration
public class CloudinaryConfig {

    private final String CLOUD_NAME = "API NAME";
    private final String API_KEY = "API_KEY";
    private final String API_SECRET = "API_SECRET";

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> congfig = new HashMap<>();

        congfig.put("cloud_name", CLOUD_NAME);
        congfig.put("api_key", API_KEY);
        congfig.put("api_secret", API_SECRET);

        return new Cloudinary(congfig);
    }
}
