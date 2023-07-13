package com.example.gallery.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class S3BucketService {

    private Logger logger = LoggerFactory.getLogger(S3BucketService.class);

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket.name}")
    private String bucketName;

    /**
     * Lists all objects on S3 bucket.
     * 
     * @return list of object keys
     */
    public List<String> listObjects() {
        ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                .withBucketName(bucketName);
        List<String> keys = new ArrayList<>();
        ObjectListing objects = s3Client.listObjects(listObjectsRequest);
        while (true) {
            List<S3ObjectSummary> objectSummaries = objects.getObjectSummaries();
            if (objectSummaries.isEmpty()) {
                break;
            }
            for (S3ObjectSummary item : objectSummaries) {
                if (!item.getKey().endsWith("/"))
                    keys.add(item.getKey());
            }
            objects = s3Client.listNextBatchOfObjects(objects);
        }
        return keys;
    }

    /**
     * Uploads file to AWS S3.
     *
     * @param objectKey - S3 bucket object key
     * @param file      - local file
     * @return upload result message
     */
    public String uploadObject(String objectKey, MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            s3Client.putObject(bucketName, objectKey, file.getInputStream(), metadata);
            return "File uploaded: " + objectKey;
        } catch (IOException ioe) {
            logger.error("IOException: {}", ioe.getMessage());
        } catch (AmazonServiceException serviceException) {
            logger.info("AmazonServiceException: {}", serviceException.getMessage());
        } catch (AmazonClientException clientException) {
            logger.info("AmazonClientException: {}", clientException.getMessage());
        }
        return "File not uploaded: " + objectKey;
    }

    /**
     * Downloads object from S3 bucket.
     *
     * @param objectKey - key of object to download
     * @return output stream of S3 object
     */
    public ByteArrayOutputStream downloadObject(String objectKey) {
        try {
            S3Object s3object = s3Client.getObject(new GetObjectRequest(bucketName, objectKey));
            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            return outputStream;
        } catch (Exception e) {
            logger.error("Exception: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Deletes object from S3 bucket.
     *
     * @param objectKey - key of object to delete
     * @return result message
     */
    public String deleteObject(String objectKey) {
        s3Client.deleteObject(bucketName, objectKey);
        return "Object deleted: " + objectKey;
    }

}