package com.gft.cwa.ubs.bean;

import lombok.Data;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;


@Data
@Entity
@Table(name = "products")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductEntry {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    private String product;
    private Integer quantity;
    private BigDecimal price;
    private String type;
    private String industry;
    private String origin;
    
    
    // Source and source line will form the uniqueness of each product entry.
    // Every line from a different file (source) must be stored only once.
    // What makes a file (source) unique is the file name. But it can easily be
    // modified to incorporate a files data or size so the system knows when
    // to reload a file.
    private String source;
    
    @Column(name = "source_line")
    private Long sourceLine;
    
    
    public void setUSDPrice(String stringPrice) {
    	this.setPrice(new BigDecimal(stringPrice.replace("$", "")));
    }
    
    
}