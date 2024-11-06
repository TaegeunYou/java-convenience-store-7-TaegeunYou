package store.domain;

import java.util.List;
import store.global.file.ProductFileDto;

public class Products {
    List<Product> products;
    public Products(List<ProductFileDto> productFileDtos) {
        this.products = productFileDtos.stream().map(Product::new).toList();
    }
}
