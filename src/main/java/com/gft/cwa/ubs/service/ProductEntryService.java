package com.gft.cwa.ubs.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.gft.cwa.ubs.bean.ProductEntry;
import com.gft.cwa.ubs.persistence.ProductEntryRepository;

@Service
public class ProductEntryService {
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	ProductEntryRepository per;
	
	public ResponseEntity<String> processFiles() {
		File dir = new File("/Users/casavelha/data/files");
//		File[] allFiles = dir.listFiles();
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
					System.out.println("Initial token, going innern");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

}
