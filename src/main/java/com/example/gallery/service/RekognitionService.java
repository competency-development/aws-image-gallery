package com.example.gallery.service;

import com.example.gallery.repository.ImagesRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.rekognition.RekognitionClient;
import software.amazon.awssdk.services.rekognition.model.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RekognitionService {

    @Value("${s3.bucket.images}")
    private String bucket;

    private final RekognitionClient rekognitionClient;

    private final ImagesRepository imagesRepository;

    public RekognitionService(RekognitionClient rekognitionClient, ImagesRepository imagesRepository) {
        this.rekognitionClient = rekognitionClient;
        this.imagesRepository = imagesRepository;
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

            imagesRepository.find(imageName).ifPresent(image -> {
                image.setDescription(description);
                imagesRepository.save(image);
            });

            return description;

        } catch(RekognitionException e) {
            e.printStackTrace();
        }


        return "Rekognition gone wrong";
    }
}
