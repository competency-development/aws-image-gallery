package com.example.gallery.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.gallery.domain.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    public Optional<Image> findByKey(String key);

}
