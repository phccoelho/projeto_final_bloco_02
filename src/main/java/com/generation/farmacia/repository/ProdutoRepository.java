package com.generation.farmacia.repository;


import java.math.BigDecimal;
import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;

import com.generation.farmacia.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long>{
	
	List<Produto> findAllByTituloContainingIgnoreCase(String titulo);
	
	List<Produto> findByPrecoLessThan(BigDecimal preco);
	
	List<Produto> findByPrecoGreaterThan(BigDecimal preco);
}
