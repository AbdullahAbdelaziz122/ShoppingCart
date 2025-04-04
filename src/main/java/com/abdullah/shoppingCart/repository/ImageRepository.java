package com.abdullah.shoppingCart.repository;

import com.abdullah.shoppingCart.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

    Optional<Image> getImageByDownloadUrl(String url);
}
