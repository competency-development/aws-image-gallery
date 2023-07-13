package com.example.gallery.service.mapper;

import org.springframework.stereotype.Component;

import com.example.gallery.domain.Image;
import com.example.gallery.service.dto.ImageDTO;

@Component
public class ImageMapper {

    public ImageDTO toDto(Image image) {
        return new ImageDTO(image.getKey(), image.getUrl(), image.getDescription());
    }

}
