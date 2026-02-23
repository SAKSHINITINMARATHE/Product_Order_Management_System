package com.manageinfo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.manageinfo.exception.ResourceNotFoundException;

import com.manageinfo.model.Product;
import com.manageinfo.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductService productService;

	@GetMapping
	public List<Product> getActiveProducts() {
		return productService.getAllActiveProducts();
	}

	@GetMapping("/admin")
	public List<Product> getAllProducts() {
		return productService.getAllProducts();
	}

	@GetMapping("/{id}")
	public Product getProductById(@PathVariable int id) {
		return productService.getProductById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));
	}

	@PostMapping
	public Product addProduct(@RequestBody Product product) {
		return productService.saveProduct(product);
	}

	@PutMapping("/{id}")
	public Product updateProduct(@PathVariable int id, @RequestBody Product productData) {
		Product product = productService.getProductById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		product.setName(productData.getName());
		product.setTagline(productData.getTagline());
		product.setDescription(productData.getDescription());
		product.setPrice(productData.getPrice());
		product.setQuantity(productData.getQuantity());
		product.setActive(productData.isActive());

		return productService.saveProduct(product);
	}

	@PatchMapping("/{id}/status")
	public Product updateStatus(@PathVariable int id, @RequestParam boolean isActive) {
		return productService.updateProductStatus(id, isActive);
	}

	@DeleteMapping("/{id}")
	public void deleteProduct(@PathVariable int id) {
		productService.deleteProductById(id);
	}
}
