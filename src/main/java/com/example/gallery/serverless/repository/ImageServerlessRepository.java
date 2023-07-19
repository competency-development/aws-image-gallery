package com.example.gallery.serverless.repository;

import com.example.gallery.serverless.domain.ImageServerless;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

@EnableScan
public interface ImageServerlessRepository extends CrudRepository<ImageServerless, Long> {

    Optional<ImageServerless> findByKey(String key);

}
