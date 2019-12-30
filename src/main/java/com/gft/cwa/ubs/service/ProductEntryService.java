package com.gft.cwa.ubs.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gft.cwa.ubs.bean.ProductDistribution;
import com.gft.cwa.ubs.bean.ProductEntry;
import com.gft.cwa.ubs.persistence.ProductEntryRepository;

@Service
public class ProductEntryService {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private EntityManager entityManager;
	
	@Value( "${data.files}" )
	private String dataFiles;
	
	@Autowired
	ProductEntryRepository per;
	
	public ResponseEntity<String> processFiles() {
		File dir = new File(dataFiles);
		File[] allFiles = dir.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.startsWith("data") && name.endsWith("json");
		    }
		});
		
		if(Objects.isNull(allFiles)) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No file found");
		}
		
		Arrays.asList(allFiles).parallelStream().forEach(f -> parseProductJsonFile(f));

		
		return ResponseEntity.ok("Files successfuly processed: \n"+Arrays.toString(allFiles));
	}
	
	

	private void parseProductJsonFile(File f) {
		try {
			JsonParser jsonParser = new JsonFactory().createParser(f);
			
			while(jsonParser.nextValue() != JsonToken.END_OBJECT){
				String name = jsonParser.getCurrentName();
				if("data".equals(name)) {
					System.out.println("Initial token, going innern with file "+f.getName());
					ProductEntry pe = new ProductEntry(); 
					Long sourceLine = 1L;
					while(jsonParser.nextValue() != JsonToken.END_ARRAY){
						String key = jsonParser.getCurrentName();
						if("product".equals(key)) {
							pe.setProduct(jsonParser.getText().trim());
						} else if("quantity".equals(key)) {
							pe.setQuantity(jsonParser.getIntValue());
						} else if ("price".equals(key)) {
							pe.setUSDPrice(jsonParser.getText());
						} else if ("type".equals(key)) {
							pe.setType(jsonParser.getText().trim());
						} else if ("industry".equals(key)) {
							pe.setIndustry(jsonParser.getText().trim());
						} else if ("origin".equals(key)) {
							pe.setOrigin(jsonParser.getText().trim());
							
							saveProductEntry(pe, f.getName(), sourceLine);
							
							sourceLine++;
							pe = new ProductEntry();
						}
					}
				}
			}
			
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void saveProductEntry(ProductEntry pe, String source, Long sourceLine) {
		pe.setSource(source);
		pe.setSourceLine(sourceLine);
		
		// Prevent saving duplicate lines
		if(Objects.isNull(per.findBySourceAndSourceLine(source, sourceLine))) {
			per.save(pe);
		}
		
	}



	public List<ProductDistribution> distributeProduct(String name, int lojistas) {
		List<ProductEntry> productEntries = per.findAllByProductOrderByQuantity(name);
		if(Objects.isNull(productEntries) || lojistas < 1) {
			return new ArrayList<>();
		}
		
		List<ProductDistribution> productDistributions = new ArrayList<>();
		IntStream.range(0, lojistas).forEach(i -> {
			ProductDistribution pd = new ProductDistribution();
			pd.setProducts(new ArrayList<>());
			pd.setLojista("Lojista "+(i+1));
			productDistributions.add(pd);
			
		});
		
		
		for(ProductEntry pe : productEntries) {
			int share = pe.getQuantity() / lojistas;
			int remainder = pe.getQuantity() % lojistas;
			
			// In case we have an uneven distribution we keep this list sorted 
			// so that the "lojista" with least total value get the remainder first
			productDistributions.sort(Comparator.comparingDouble(ProductDistribution::getTotalValue));
			
			// Distribute even shares
			if(share > 0) {
				productDistributions.forEach(p -> p.addProductEntry(pe, share));
			}
			
			// Distribute remainder
			if(remainder > 0) {
				IntStream.range(0, remainder).forEach(i -> productDistributions.get(i).addProductEntry(pe, 1));
			}
			
		}
		
		
		return productDistributions;
	}

}
