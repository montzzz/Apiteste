package com.mateus.apiteste.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mateus.apiteste.domain.Categoria;
import com.mateus.apiteste.domain.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{
	
		
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat in :categorias")
	Page<Produto> search(@Param("nome") String nome,@Param("categorias") List<Categoria> categorias,Pageable pageRequest);
	
	@Transactional(readOnly = true)
	// Outro jeito de fazer pela API do JPQL (sem precisar digitar a query manualmente).
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
}
