package com.example.gallery.service;

import com.example.gallery.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RekognitionService {

    @Value("${s3.bucket.images}")
    private String bucket;

    private final RekognitionClient rekognitionClient;

    private final ImageRepository imageRepository;

    public RekognitionService(RekognitionClient rekognitionClient, ImageRepository imageRepository) {
        this.rekognitionClient = rekognitionClient;
        this.imageRepository = imageRepository;
    }

    /**
     * Takes image key in S3 bucket and send that image to Rekognition service. The service processes image and returns
     * labels that describe given image.
     *
     * @param imageName - key of the image in the bucket
     * @return concatenated string of labels
     */
    public String generateImageDescription(String imageName) {
        DetectLabelsRequest request = DetectLabelsRequest.builder()
                .image(Image.builder()
                        .s3Object(S3Object.builder()
                                .bucket(bucket)
                                .name(imageName)
                                .build())
                        .build())
                .maxLabels(10)
                .minConfidence(75F)
                .build();

        try {
            DetectLabelsResponse response = rekognitionClient.detectLabels(request);
            List<Label> labels = response.labels();

            System.out.println("Detected labels for " + imageName);
            String description = labels.stream().map(Label::name).collect(Collectors.joining(", "));

            imageRepository.findByKey(imageName).ifPresent(image -> {
                image.setDescription(description);
                imageRepository.save(image);
            });

            return description;

        } catch(RekognitionException e) {
            e.printStackTrace();
        }


        return "Rekognition gone wrong";
    }
}
