package com.example.gallery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;

@Configuration
public class PollyConfig {

    @Value("${aws.profile:default}")
    private String profile;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonPolly pollyClient() {
        return AmazonPollyClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new ProfileCredentialsProvider(profile)).build();
    }
}
