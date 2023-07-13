package com.example.gallery.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.gallery.service.S3BucketService;

@RestController
public class S3BucketController {

    @Autowired
    private S3BucketService bucketService;

    @GetMapping("/s3")
    public ResponseEntity<List<String>> getKeys() {
        return new ResponseEntity<>(bucketService.getAllKeys(), HttpStatus.OK);
    }

    @PostMapping("/s3/upload")
    public ResponseEntity<String> upload(@RequestParam("key") String key,
            @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(bucketService.upload(key, file),
                HttpStatus.OK);
    }

    @GetMapping(value = "/s3/delete")
    @ResponseStatus(HttpStatus.OK)
    public void deleteFile(@RequestParam("key") String key) {
        bucketService.delete(key);
    }

}