package com.example.gallery.controller;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.gallery.service.S3BucketService;

@RestController
public class S3BucketController {

    @Autowired
    private S3BucketService service;

    @GetMapping("/s3/list")
    public ResponseEntity<List<String>> listObjects() {
        return new ResponseEntity<>(service.listObjects(), HttpStatus.OK);
    }

    @PostMapping("/s3/upload")
    public ResponseEntity<String> uploadObject(@RequestParam("objectKey") String objectKey,
            @RequestParam("file") MultipartFile file) {
        return new ResponseEntity<>(service.uploadObject(objectKey, file), HttpStatus.OK);
    }

    @GetMapping(value = "/s3/download")
    public ResponseEntity<byte[]> downloadFile(@RequestParam("objectKey") String objectKey) {
        ByteArrayOutputStream os = service.downloadObject(objectKey);
        return ResponseEntity.ok()
                .contentType(getMediaType(objectKey))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + objectKey + "\"")
                .body(os.toByteArray());
    }

    @GetMapping(value = "/s3/delete")
    public ResponseEntity<String> deleteFile(@RequestParam("objectKey") String objectKey) {
        return new ResponseEntity<>(service.deleteObject(objectKey), HttpStatus.OK);
    }

    private MediaType getMediaType(String fileName) {
        String[] fileNameSplit = fileName.split("\\.");
        String fileExtension = fileNameSplit[fileNameSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

}