package com.abdullah.shoppingCart.service.product;

import com.abdullah.shoppingCart.dto.CategoryDTO;
import com.abdullah.shoppingCart.dto.ProductDTO;
import com.abdullah.shoppingCart.exceptions.ProductNotFoundException;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.model.Category;
import com.abdullah.shoppingCart.model.Product;
import com.abdullah.shoppingCart.repository.CategoryRepository;
import com.abdullah.shoppingCart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService implements IProductService{


    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private ModelMapper mapper;

    @Override
    public Product addProduct(ProductDTO productDTO) {

        Category category = categoryRepository.findByName(productDTO.getCategory().getName())
                .orElseGet(() -> {
                    Category newCategory = new Category(productDTO.getCategory().getName());
                    return categoryRepository.save(newCategory);
                });

        productDTO.setCategory(category);

        return productRepository.save(DtoToProduct(productDTO, category));
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new ProductNotFoundException("Product with id" + id + " is not found ."));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id).ifPresentOrElse(productRepository::delete,
                ()-> {throw new ResourcesNotFoundException("Product with id "+id+ " not found");});
    }

    @Override
    public Product updateProductById(ProductDTO productDTO, Long id) {
        return  productRepository.findById(id)
                .map(existingProduct -> updateProduct(existingProduct, productDTO))
                .map(productRepository::save)
                .orElseThrow(()-> new ResourcesNotFoundException("Product with id "+ id+ " not found"));
    }

    @Override
    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findAllByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findAllByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findAllByCategoryNameAndBrand(category, brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findAllByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findAllByBrandAndName(brand, name);
    }

    @Override
    public List<Product> filterProducts(String name, String brand, String category) {
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

        return productRepository.findAll(spec);
    }


    @Override
    public Long countProductsByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand, name);
    }


    private Product DtoToProduct (ProductDTO productDTO, Category category){
        return new Product(
                productDTO.getName(),
                productDTO.getBrand(),
                productDTO.getPrice(),
                productDTO.getInventory(),
                productDTO.getDescription(),
                category
        );
    }





    private Product updateProduct(Product existingProduct, ProductDTO newProduct) {
        if (newProduct.getName() != null) {
            existingProduct.setName(newProduct.getName());
        }
        if (newProduct.getBrand() != null) {
            existingProduct.setBrand(newProduct.getBrand());
        }
        if (newProduct.getPrice() != null) {
            existingProduct.setPrice(newProduct.getPrice());
        }
        if (newProduct.getInventory() != null) {
            existingProduct.setInventory(newProduct.getInventory());
        }
        if (newProduct.getDescription() != null) {
            existingProduct.setDescription(newProduct.getDescription());
        }

        if (newProduct.getCategory() != null && newProduct.getCategory().getName() != null) {
            Category category = categoryRepository.findByName(newProduct.getCategory().getName())
                    .orElseGet(() -> {
                        Category newCategory = new Category(newProduct.getCategory().getName());
                        return categoryRepository.save(newCategory);
                    });
            existingProduct.setCategory(category);
        }

        return existingProduct;
    }

}
