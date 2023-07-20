package com.example.gallery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

@Configuration
public class RekognitionConfig {

    @Value("${aws.profile:kotsial}")
    private String profile;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public RekognitionClient amazonRekognition() {
        return RekognitionClient.builder()
                .region(Region.of(region)) // has to be same with the region of S3 object
                .credentialsProvider(ProfileCredentialsProvider.create(profile))
                .build();
    }
}
