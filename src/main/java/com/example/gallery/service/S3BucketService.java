package com.example.gallery.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@Service
public class S3BucketService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket}")
    private String bucket;

    /**
     * Lists all object keys on the S3 bucket.
     * 
     * @return list of object keys
     */
    public List<String> getAllKeys() {
        ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucket);
        ObjectListing objectListing = s3Client.listObjects(request);
        List<String> keys = new ArrayList<>();
        while (true) {
            List<S3ObjectSummary> objectSummaries = objectListing.getObjectSummaries();
            if (objectSummaries.isEmpty()) {
                break;
            }
            for (S3ObjectSummary objectSummary : objectSummaries) {
                if (!objectSummary.getKey().endsWith("/"))
                    keys.add(objectSummary.getKey());
            }
            objectListing = s3Client.listNextBatchOfObjects(objectListing);
        }
        return keys;
    }

    /**
     * Gets public URL for the object by key.
     * 
     * @param key object key in S3 bucket
     * @return S3 URL
     */
    public String getUrlByKey(String key) {
        return s3Client.getUrl(bucket, key).toString();
    }

    /**
     * Uploads file to S3 bucket.
     * 
     * @param key  object key
     * @param file file
     * @return S3 URL
     * @throws IOException if error occurred while reading file
     */
    public String upload(String key, MultipartFile file) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        s3Client.putObject(bucket, key, file.getInputStream(), metadata);
        return getUrlByKey(key);
    }

    /**
     * Deletes object from S3 bucket.
     *
     * @param key object key to delete
     */
    public void delete(String key) {
        s3Client.deleteObject(bucket, key);
    }

}