package com.example.gallery.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class S3ClientConfiguration {

    @Value("${aws.profile:kotsial}")
    private String profile;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 awsS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new ProfileCredentialsProvider(profile))
                .build();
    }

}