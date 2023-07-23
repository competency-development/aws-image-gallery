package com.example.gallery.config;

import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.polly.AmazonPolly;
import com.amazonaws.services.polly.AmazonPollyClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClientBuilder;
import software.amazon.awssdk.services.rekognition.RekognitionClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@Configuration
public class AWSConfiguration {

    @Value("${aws.profile:default}")
    private String profile;

    @Value("${cloud.aws.region.static}")
    private String region;

    @Value("${amazon.dynamodb.endpoint}")
    private String dynamoDBEndpointLocal;

    /**
     * AWS S3 bean configuration
     */
    @Bean
    public AmazonS3 awsS3Client() {
        return AmazonS3ClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new ProfileCredentialsProvider(profile))
                .build();
    }

    /**
     * AWS Rekognition bean configuration
     */
    @Bean
    public RekognitionClient awsRekognitionClient() {
        return RekognitionClient.builder()
                .region(Region.of(region)) // has to be same with the region of S3 object
                .credentialsProvider(software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider.create(profile))
                .build();
    }

    /**
     * AWS Polly bean configuration
     */
    @Bean
    public AmazonPolly awsPollyClient() {
        return AmazonPollyClientBuilder.standard()
                .withRegion(Regions.fromName(region))
                .withCredentials(new ProfileCredentialsProvider(profile))
                .build();
    }

    /**
     * AWS DynamoDB (V2) bean configuration
     */
    @Bean
    public DynamoDbClient awsDynamoDBClient() throws URISyntaxException {
        DynamoDbClientBuilder dynamoDBClientBuilder = DynamoDbClient.builder()
                .region(Region.of(region))
                .credentialsProvider(software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider.create(profile));
        if (dynamoDBEndpointLocal != null && !Objects.equals(dynamoDBEndpointLocal, "disable")) {
            dynamoDBClientBuilder.endpointOverride(new URI(dynamoDBEndpointLocal));
        }
        return dynamoDBClientBuilder.build();
    }

    /**
     * AWS DynamoDB Enhanced (V2) bean configuration
     */
    @Bean
    public DynamoDbEnhancedClient getDynamoDbEnhancedClient() throws URISyntaxException {
        return DynamoDbEnhancedClient.builder()
                .dynamoDbClient(awsDynamoDBClient())
                .build();
    }

}
