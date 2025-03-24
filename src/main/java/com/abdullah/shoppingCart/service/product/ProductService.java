package com.abdullah.shoppingCart.service.product;

import com.abdullah.shoppingCart.dto.ProductDTO;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.mapper.CategoryMapper;
import com.abdullah.shoppingCart.mapper.ProductMapper;
import com.abdullah.shoppingCart.model.Category;
import com.abdullah.shoppingCart.model.Product;
import com.abdullah.shoppingCart.repository.CategoryRepository;
import com.abdullah.shoppingCart.repository.ProductRepository;
import com.abdullah.shoppingCart.util.SlugUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService{


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final SlugUtils utils = new SlugUtils();
    private SlugUtils slugUtils = new SlugUtils();

    @Override
    public ProductDTO addProduct(ProductDTO productDTO) {
        //check category
        Category category = categoryRepository.findByName(productDTO.getCategory().getName())
                .orElseGet(() -> {
                    Category newCategory = new Category(productDTO.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });
        productDTO.setSlug(generateUniqueSlug(slugUtils.generateSlug(productDTO.getName())));
        Product product = productMapper.toEntity(productDTO);
        product.setCategory(category);
        productRepository.save(product);

        return productMapper.toDto(product);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        return productRepository.findById(id).map(product -> {
            return productMapper.toDto(product);
                })
                .orElseThrow(()-> new ResourcesNotFoundException("Product with id" + id + " is not found ."));
    }

    @Override
    public ProductDTO getProductBySlug(String slug) {
        return productRepository.findBySlug(slug).map(product -> {
            return productMapper.toDto(product);
                })
                .orElseThrow(()-> new ResourcesNotFoundException("Product with Slug" + slug + " is not found ."));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                ()-> {throw new ResourcesNotFoundException("Product with id "+id+ " not found");});
    }



    @Override
    public ProductDTO updateProductById(ProductDTO productDTO, Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourcesNotFoundException(String.format("Product with ID %d not found", id)));

        productMapper.updateProduct(product, productDTO);

        // Custom logic for slug
        if (productDTO.getName() != null) {
            product.setSlug(generateUniqueSlug(slugUtils.generateSlug(productDTO.getName())));
        }

        // Custom logic for category
        if (productDTO.getCategory() != null && productDTO.getCategory().getName() != null) {
            Category category = categoryRepository.findByName(productDTO.getCategory().getName())
                    .orElseGet(() -> {
                        Category newCategory = new Category(productDTO.getCategory().getName());
                        return categoryRepository.save(newCategory);
                    });
            product.setCategory(category);
        }

        return productMapper.toDto(productRepository.save(product));
    }



    @Override
    public List<ProductDTO> getAllProduct() {
        return productMapper.toDtoList(productRepository.findAll());
    }


    @Override
    public List<ProductDTO> filterProducts(String name, String brand, String category) {
        Specification<Product> spec = Specification.where(null);

        if (category != null && !category.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("category").get("name"), category));
        }
        if (brand != null && !brand.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("brand"), brand));
        }
        if (name != null && !name.isBlank()) {
            spec = spec.and((root, query, cb) ->
                    cb.like(root.get("name"), "%"+name+"%"));
        }

        return productMapper.toDtoList(productRepository.findAll(spec));
    }


    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }




    private String generateUniqueSlug(String baseSlug) {
        String candidateSlug = baseSlug;
        int suffix = 1;

        while (productRepository.findBySlug(candidateSlug).isPresent()) {
            candidateSlug = baseSlug + "-" + suffix;
            suffix++;
        }
        return candidateSlug;
    }

}
