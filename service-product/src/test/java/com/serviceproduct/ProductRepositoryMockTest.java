package com.serviceproduct;

import com.serviceproduct.entity.Category;
import com.serviceproduct.entity.Product;
import com.serviceproduct.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

@DataJpaTest
public class ProductRepositoryMockTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void whenFindByCategory_thenReturnListProduct(){
        Product product01 = Product.builder()
                .name("computer")
                .category(Category.builder().id(1L).build())
                .description("")
                .stock(Double.parseDouble("10"))
                .price(Double.parseDouble("1240.99"))
                .status("Created")
                .createAt(new Date())
                .build();

        productRepository.save(product01);

        List<Product> founds = productRepository.findAllByCategory(product01.getCategory());

        // Se manejan los aciertos, de lo cual se espera una lista de tamaño 3.
        Assertions.assertThat(founds.size()).isEqualTo(3);
    }

}
