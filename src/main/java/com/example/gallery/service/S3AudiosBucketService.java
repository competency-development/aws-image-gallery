package com.example.gallery.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3AudiosBucketService {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${s3.bucket.audios}")
    private String bucket;

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
    public String upload(String key, InputStream inputStream) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("audio/mpeg3");
        s3Client.putObject(bucket, key, inputStream, metadata);
        return getUrlByKey(key);
    }
}