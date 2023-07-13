package com.example.gallery.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.gallery.service.ImageService;
import com.example.gallery.service.dto.ImageDTO;

@RestController
public class ImageController {

    @Autowired
    private ImageService imageService;

    @CrossOrigin
    @GetMapping("images")
    public ResponseEntity<List<ImageDTO>> getImages() {
        return new ResponseEntity<>(imageService.getAll(), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("images/upload")
    public ResponseEntity<ImageDTO> upload(
            @RequestParam("file") MultipartFile file) throws IOException {
        return new ResponseEntity<>(imageService.upload(file), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("images/delete")
    @ResponseStatus(value = HttpStatus.OK)
    public void delete(@RequestParam("key") String key) {
        imageService.deleteByKey(key);
    }

}
