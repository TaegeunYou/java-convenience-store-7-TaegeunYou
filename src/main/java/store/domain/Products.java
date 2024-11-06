package store.domain;

import java.util.Collections;
import java.util.List;
import store.global.file.dto.ProductFileDto;

public class Products {
    List<Product> products;
    public Products(List<ProductFileDto> productFileDtos) {
        this.products = productFileDtos.stream().map(Product::new).toList();
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
