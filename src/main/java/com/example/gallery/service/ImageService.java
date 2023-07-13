package com.example.gallery.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.gallery.domain.Image;
import com.example.gallery.repository.ImageRepository;
import com.example.gallery.service.dto.ImageDTO;
import com.example.gallery.service.mapper.ImageMapper;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ImageService {

    @Autowired
    private ImageRepository repository;

    @Autowired
    private ImageMapper mapper;

    @Autowired
    private S3BucketService bucketService;

    /**
     * Lists all {@link Image} entities.
     * 
     * @return list of {@link ImageDTO}
     */
    public List<ImageDTO> getAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Uploads an image to S3 bucket and saves it to the database.
     * 
     * @param file {@link MultipartFile}
     * @return uploaded {@link ImageDTO}
     * @throws IOException if an error occurs while uploading
     */
    public ImageDTO upload(MultipartFile file) throws IOException {
        String imageName = RandomStringUtils.randomAlphanumeric(24);
        String imageUrl = bucketService.upload(imageName, file);

        Image image = new Image(null, imageName, imageUrl, null);
        repository.save(image);

        log.info("Image uploaded: {}", imageName);
        return mapper.toDto(image);
    }

    /**
     * Deletes images with provided URL
     * 
     * @param url image URL to delete
     */
    public void deleteByKey(String key) {
        Optional<Image> image = repository.findByKey(key);
        if (image.isPresent()) {
            bucketService.delete(key);
            repository.delete(image.get());
            log.info("Image deleted: {}", key);
        }
    }

}