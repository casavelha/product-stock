package com.gft.cwa.ubs.persistence;

import org.springframework.data.repository.CrudRepository;

import com.gft.cwa.ubs.bean.ProductEntry;

public interface ProductEntryRepository extends CrudRepository<ProductEntry, Long>{
	public ProductEntry findBySourceAndSourceLine(String source, Long sourceLine);

}
