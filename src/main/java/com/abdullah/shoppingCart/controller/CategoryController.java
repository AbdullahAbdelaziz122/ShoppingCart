package com.abdullah.shoppingCart.controller;

import com.abdullah.shoppingCart.dto.CategoryDTO;
import com.abdullah.shoppingCart.exceptions.AlreadyExistsException;
import com.abdullah.shoppingCart.exceptions.ResourcesNotFoundException;
import com.abdullah.shoppingCart.model.Category;
import com.abdullah.shoppingCart.response.ApiResponse;
import com.abdullah.shoppingCart.service.category.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService iCategoryService;

    @PostMapping()
    public ResponseEntity<ApiResponse> addCategory(@Valid @RequestBody CategoryDTO categoryDto) {
        try {
            if(categoryDto == null){
                return ResponseEntity.badRequest()
                        .body(new ApiResponse("Category name Cannot be empty", null));
            }
            CategoryDTO response = iCategoryService.addCategory(categoryDto);
            return ResponseEntity.ok().body(new ApiResponse("Category add Success", response));

        }catch (AlreadyExistsException e ){
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse(" Category already exist", null));
        }
    }


    @GetMapping("/category/{id}")
    public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long id){

        try {
            CategoryDTO response = iCategoryService.getCategoryById(id);
            return ResponseEntity.ok(new ApiResponse("Category Found", response));
        }catch (ResourcesNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(String.format("Category with ID %d not found", id), null)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected error occurred", null));
        }
    }

    @GetMapping()
    public ResponseEntity<ApiResponse> getCategoryByName(@RequestParam (required = false) String name){

        try {

            // check if No Parameter is given
            if (name == null || name.isBlank()){
                List<CategoryDTO> response = iCategoryService.getAllCategories();
                if (response != null && !response.isEmpty()) {
                    return ResponseEntity.ok(new ApiResponse("Categories Found", response));
                }
                return ResponseEntity.ok(new ApiResponse("No Categories Found",null));
            }

            // return specific category
            CategoryDTO category = iCategoryService.getCategoryByName(name);
            return ResponseEntity.ok(new ApiResponse("Category Found", category));
        }catch (ResourcesNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(String.format("Category with name %s not found", name), null)
            );
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("An unexpected error occurred", null));
        }
    }


    @PutMapping("/category/{id}")
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryDTO categoryDTO                                          ){

        try {
            CategoryDTO category = iCategoryService.updateCategory(categoryDTO, id);
            return ResponseEntity.ok(new ApiResponse("Category update success", category));
        } catch (ResourcesNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(String.format("Category Not Found with ID %d", id), null)
            );
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new ApiResponse("Something went Wrong", null)
            );
        }
    }

    @DeleteMapping("/category/{id}")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long id){

        try {
            iCategoryService.deleteCategory(id);
            return ResponseEntity.ok(new ApiResponse("Category Delete success", null));
        } catch (ResourcesNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(String.format("Category with ID %d Not Found", id), null));

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Something went Wrong", null));
        }
    }




}
