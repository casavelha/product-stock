package com.gft.cwa.ubs;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;

import com.gft.cwa.ubs.bean.ProductEntry;
import com.gft.cwa.ubs.persistence.ProductEntryRepository;

@DataJpaTest
class ProductStockApplicationTests {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private EntityManager entityManager;
	@Autowired
	private ProductEntryRepository per;

	
	@Test
	void testDatabaseConnection() {
		assertThat(dataSource).isNotNull();
		assertThat(jdbcTemplate).isNotNull();
		assertThat(entityManager).isNotNull();
	}
	
	
	@Test
	void testDatabaseOperations() {
		ProductEntry pe = new ProductEntry();
		pe.setProduct("ETM");
		pe.setQuantity(21);

		pe.setUSDPrice("$7.55");
		pe.setType("M");
		pe.setIndustry("Broadcasting");
		pe.setOrigin("MI");
		pe.setSource("data_test.json");
		pe.setSourceLine(4l);

		per.save(pe);

		ProductEntry savedPe = per.findById(pe.getId()).orElse(null);
		assertThat(savedPe).isNotNull();
		
		
		savedPe = per.findBySourceAndSourceLine("data_test.json", 4L);
		assertThat(savedPe).isNotNull();
		
		
		per.delete(savedPe);
		
		ProductEntry deletedPe = per.findById(pe.getId()).orElse(null);
		assertThat(deletedPe).isNull();

	}

}
