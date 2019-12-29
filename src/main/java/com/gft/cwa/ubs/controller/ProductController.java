package com.gft.cwa.ubs.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gft.cwa.ubs.bean.ProductEntry;
import com.gft.cwa.ubs.persistence.ProductEntryRepository;
import com.gft.cwa.ubs.service.ProductEntryService;

@RestController
public class ProductController {
	
	@Autowired
	ProductEntryRepository per;
	
	@Autowired
	ProductEntryService pes;
	

    @RequestMapping("/products")
    public List<ProductEntry> findProducts() {
    	List<ProductEntry> productsList = new ArrayList<>();
    	per.findAll().forEach(e -> productsList.add(e));
        return productsList;
    }
    
    @RequestMapping("/products/processFiles")
    public ResponseEntity<String> processFiles() {
    	return pes.processFiles();
    }

}