package com.abdullah.shoppingCart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "image") // Explicit table name to avoid conflicts
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_name") // Explicit column name
    private String fileName;

    @Column(name = "file_type") // Explicit column name
    private String fileType;

    @Lob
    @Column(name = "image_data") // Rename 'blob' to avoid keyword conflict
    private byte[] imageData; // Use byte[] instead of Blob

    @Column(name = "download_url")
    private String downloadUrl;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}