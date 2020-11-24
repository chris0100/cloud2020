package com.serviceproduct.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviceproduct.entity.Category;
import com.serviceproduct.entity.Product;
import com.serviceproduct.services.ProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/products")
public class ProductController {

    @Autowired
    private ProductServices productServicesObj;

    @GetMapping
    public ResponseEntity<List<Product>> listProduct(@RequestParam(name="categoryId", required = false) Long categoryId){

        List<Product> products = new ArrayList<>();

        //if you can not obtain a category, list all the products
        if (categoryId == null){
            products = productServicesObj.listAllProduct();
            // if the product list is null then make a response
            if (products.isEmpty()){
                return ResponseEntity.noContent().build();
            }
        }
        else{
            products = productServicesObj.findByCategory(Category.builder().id(categoryId).build());
            // if the product list is null then make a response
            if (products.isEmpty()){
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.ok(products);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable("id") Long id){
        Product product = productServicesObj.getProduct(id);

        // if the product list is null then make a response
        if (product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }


    // Send the product to create and return the response with the product in json format.
    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product, BindingResult result){

        // we must verify the errors
        if (result.hasErrors()){
            System.out.println("has errors");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, formatMessage(result));
        }

        Product productCreate = productServicesObj.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(productCreate);
    }


    //Update the product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("id") Long id, @RequestBody Product product){
        product.setId(id);
        Product productDB = productServicesObj.updateProduct(product);

        if (productDB == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDB);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") Long id){
        Product productDelete = productServicesObj.deleteProduct(id);
        if (productDelete == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(productDelete);
    }


    //increase the quantity
    @GetMapping("/{id}/stock")
    public ResponseEntity<Product> updateStockProduct(@PathVariable("id") Long id, @RequestParam(name = "quantity", required = true) Double quantity){
        Product product = productServicesObj.updateStock(id, quantity);
        if (product == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }



    //return the string message error
    private String formatMessage(BindingResult result){
        List<Map<String, String>> errors = result.getFieldErrors()
                .stream()
                .map(err -> {
                    Map<String, String> error = new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());

        //Use the class to transform
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();

        //Convert to json string
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try{
            jsonString = mapper.writeValueAsString(errorMessage);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        System.out.println(jsonString);
        return jsonString;
    }




}
