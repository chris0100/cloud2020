package com.serviceproduct;

import com.serviceproduct.entity.Category;
import com.serviceproduct.entity.Product;
import com.serviceproduct.repository.ProductRepository;
import com.serviceproduct.services.ProductServiceImpl;
import com.serviceproduct.services.ProductServices;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class ProductServiceMockTest {

    @Mock
    private ProductRepository productRepositoryObj;

    private ProductServices productServicesObj;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        productServicesObj = new ProductServiceImpl(productRepositoryObj);

        //Se crea un producto
        Product computer = Product.builder()
                .id(1L)
                .name("computer")
                .category(Category.builder().id(1L).build())
                .price(Double.parseDouble("12.5"))
                .stock(Double.parseDouble("5"))
                .build();

        //Realiza la busqueda
        Mockito.when(productRepositoryObj.findById(1L))
                .thenReturn(Optional.of(computer));

        // guarda el Product computer
        Mockito.when(productRepositoryObj.save(computer)).thenReturn(computer);
    }


    @Test
    public void whenValidGetID_ThenReturnProduct(){
        Product found = productServicesObj.getProduct(1L);
        Assertions.assertThat(found.getName()).isEqualTo("computer");
    }

    @Test
    public void whenValidUpdateStock_ThenReturnNewStock(){
        Product newStock = productServicesObj.updateStock(1L, Double.parseDouble("8"));
        Assertions.assertThat(newStock.getStock()).isEqualTo(13);

    }
}
