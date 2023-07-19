package com.example.gallery.repository;

import com.example.gallery.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {

    public Optional<Image> findByKey(String key);

}
