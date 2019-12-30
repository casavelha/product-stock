package com.gft.cwa.ubs.bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import lombok.Data;

@Data
public class ProductDistribution {
	private String lojista;
	private List<ProductEntry> products;
	
	private Double totalValue = 0.0;
	
	public Double updateTotalValue() {
		setTotalValue(products.stream().map(p -> p.getPrice().multiply(new BigDecimal(p.getQuantity()))).mapToDouble(BigDecimal::doubleValue).sum());
		return getTotalValue();
	}
	
	public void addProductEntry(ProductEntry pe) {
		if(Objects.isNull(products)) {
			products = new ArrayList<>();
		}
		Optional<ProductEntry> foundProduct = products.stream().filter(product -> product.getId() == pe.getId()).findAny();
		
		if(foundProduct.isPresent()) {
			foundProduct.get().setQuantity(foundProduct.get().getQuantity()+pe.getQuantity());
		} else {
			products.add(pe);
		}
		updateTotalValue();
	}
	
	public void addProductEntry(ProductEntry pe, int quantity) {
		ProductEntry newProductEntry = new ProductEntry();
		newProductEntry.setIndustry(pe.getIndustry());
		newProductEntry.setOrigin(pe.getOrigin());
		newProductEntry.setPrice(pe.getPrice());
		newProductEntry.setProduct(pe.getProduct());
		newProductEntry.setQuantity(quantity);
		newProductEntry.setType(pe.getType());
		newProductEntry.setId(pe.getId());
		
		addProductEntry(newProductEntry);
	}

}
