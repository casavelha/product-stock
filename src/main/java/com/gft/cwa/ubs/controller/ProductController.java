package com.gft.cwa.ubs.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class ProductController {

    @RequestMapping("/products")
    public List<String> findProducts() {
        List<String> products = Arrays.asList("AS", "REE");

        return products;
    }

}