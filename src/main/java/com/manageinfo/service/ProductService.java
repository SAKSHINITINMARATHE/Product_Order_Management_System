package com.manageinfo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.manageinfo.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manageinfo.model.Product;
import com.manageinfo.repository.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;

	public List<Product> getAllProducts() {
		List<Product> allProducts = productRepository.findAll();
		List<Product> visibleProducts = new ArrayList<>();
		for (Product p : allProducts) {
			if (!p.isDeleted()) {
				visibleProducts.add(p);
			}
		}
		return visibleProducts;
	}

	public List<Product> getAllActiveProducts() {
		List<Product> allProducts = productRepository.findAll();
		List<Product> activeProducts = new ArrayList<>();

		for (Product product : allProducts) {
			if (product.isActive() && !product.isDeleted()) {
				activeProducts.add(product);
			}
		}

		return activeProducts;
	}

	public Optional<Product> getProductById(int id) {
		return productRepository.findById(id);
	}

	public Product saveProduct(Product product) {
		return productRepository.save(product);
	}

	public void deleteProductById(int id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isPresent()) {
			Product product = productOpt.get();
			product.setDeleted(true);
			product.setActive(false); // Also deactivate it
			productRepository.save(product);
		} else {
			throw new ResourceNotFoundException("Product not found");
		}
	}

	public Product updateProductStatus(int id, boolean isActive) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
		product.setActive(isActive);
		return productRepository.save(product);
	}
}
