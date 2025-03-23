package com.abdullah.shoppingCart.controller;

import com.abdullah.shoppingCart.dto.ProductDTO;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.model.Product;
import com.abdullah.shoppingCart.response.ApiResponse;
import com.abdullah.shoppingCart.service.product.IProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/products")
public class ProductController {

    private final IProductService productService;
    private final ModelMapper mapper;

    @PostMapping
    public ResponseEntity<ApiResponse> addProduct(@Valid @RequestBody ProductDTO productDTO){

        try {
            Product product = productService.addProduct(productDTO);
            return ResponseEntity.ok(new ApiResponse("Product Add success", product));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something went wrong", null));
        }

    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProduct(){
        try {
            List<Product> products = productService.getAllProduct();
            String message = products.isEmpty() ? "No Products Found": "Products Found";
            return ResponseEntity.ok(new ApiResponse(message,products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Something went Wrong", null)
            );
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable Long id){
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(new ApiResponse("Products Found",product));

        }catch (ResourcesNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(String.format("Product With ID %d Not found", id), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Something went Wrong", null)
            );
        }
    }

    @GetMapping("/product/{slug}")
    public ResponseEntity<ApiResponse> getProductById(@PathVariable String slug){
        try {
            Product product = productService.getProductBySlug(slug);
            return ResponseEntity.ok(new ApiResponse("Products Found",product));

        }catch (ResourcesNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(String.format("Product With ID %s Not found", slug), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Something went Wrong", null)
            );
        }
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse> filterProducts(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String brand,
            @RequestParam (required = false) String category
    ){
        try {
            List<Product> products = productService.filterProducts(name, brand, category);
            String message = products.isEmpty() ? "No Product Found" : "Products Found";
            return ResponseEntity.ok(new ApiResponse(message, products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("Something went Wrong", null));
        }

    }

    @PutMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductDTO  productDTO
    ){

        try {
            Product product = productService.updateProductById(productDTO, productId);
            return ResponseEntity.ok(new ApiResponse("Product Update Success", product));

        } catch (ResourcesNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(String.format("Product With ID %d Not found", productId), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Something went Wrong", null)
            );
        }
    }


    @DeleteMapping("/product/{productId}")
    public ResponseEntity<ApiResponse> deleteProduct(@PathVariable Long productId){
        try {
            productService.deleteProductById(productId);
            return ResponseEntity.ok(new ApiResponse("Product Delete Success", null));

        } catch (ResourcesNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(String.format("Product With ID %d Not found", productId), null)
            );
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Something went Wrong", null)
            );
        }
    }


}
