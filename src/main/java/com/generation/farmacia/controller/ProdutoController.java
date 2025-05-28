package com.generation.farmacia.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.farmacia.model.Produto;
import com.generation.farmacia.repository.CategoriaRepository;
import com.generation.farmacia.repository.ProdutoRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ProdutoController{
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@GetMapping
	public ResponseEntity<List<Produto>> getAll(){
		//SELECT * FROM tb_postagens;
		return ResponseEntity.ok(produtoRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id){
		return produtoRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@GetMapping("/titulo/{titulo}")
	public ResponseEntity<List<Produto>> getAllByTitulo(@PathVariable String titulo){		
		return ResponseEntity.ok(produtoRepository.findAllByTituloContainingIgnoreCase(titulo));
	}
	
	//Select pelo Maior Preço
	/*@GetMapping("/maior_preco/{preco}")
	public ResponseEntity<Produto> getProdutoComMaiorPreco() {
	    return produtoRepository.findTopByOrderByPrecoDesc()
	    		.map(resposta -> ResponseEntity.ok(resposta))
	            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}*/
	
	//Select pelo Menor Preço
	/*@GetMapping("/menor_preco/{preco}")
	public ResponseEntity<Produto> getProdutoComMenorPreco() {
	    return produtoRepository.findTopByOrderByPrecoAsc()
	    		.map(resposta -> ResponseEntity.ok(resposta))
	            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}*/
	
	@PostMapping
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto){
		
		if(categoriaRepository.existsById(produto.getCategoria().getId())) {				
			return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
		}
		throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A categoria não existe!", null);
	}
	
	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto){
		
		if(produto.getId() == null)
			return ResponseEntity.badRequest().build();
		
		if(produtoRepository.existsById(produto.getId()))	{		
			if(categoriaRepository.existsById(produto.getCategoria().getId())) {
			
				return ResponseEntity.status(HttpStatus.OK).body(produtoRepository.save(produto));
			}
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O tema não existe!", null);
		}	
		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		
		Optional<Produto> produto = produtoRepository.findById(id);
		
		if(produto.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
			
		}
		produtoRepository.deleteById(id);
		
		//DELETE FROM tb_postagens WHERE id=?;
		
	}
}