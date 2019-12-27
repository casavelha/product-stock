package com.gft.cwa.ubs.bean;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductEntry {
    private Long id;
    private String product;
    private Integer quantity;
    private BigDecimal price;
    private String type;
    private String industry;
    private String origin;
    private String source;
}