package com.example.gallery.service;

import com.example.gallery.domain.Image;
import com.example.gallery.repository.ImagesRepository;
import com.example.gallery.service.dto.ImageDTO;
import com.example.gallery.service.mapper.ImageMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ImageService {

    @Autowired
    private ImagesRepository repository;

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
        return repository.findAll().items().stream()
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
        String imageName = UUID.randomUUID().toString();
        String imageUrl = bucketService.upload(imageName, file);

        Image image = new Image(imageName, imageUrl, null);
        repository.save(image);

        log.info("Image uploaded: {}", imageName);
        return mapper.toDto(image);
    }

    /**
     * Deletes images with provided URL
     *
     * @param key image id to delete
     */
    public void deleteByKey(String key) {
        repository.find(key).ifPresent(item -> {
            bucketService.delete(key);
            repository.delete(key);
            log.info("Image deleted: {}", key);
        });
    }

}