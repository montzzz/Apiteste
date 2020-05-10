package com.mateus.apiteste.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.mateus.apiteste.domain.Categoria;
import com.mateus.apiteste.repositories.CategoriaRepository;
import com.mateus.apiteste.services.exceptions.DataIntegrityException;
import com.mateus.apiteste.services.exceptions.ObjectNotfoundException;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repo;
	
	public Categoria find(Integer id) {
		
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotfoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		// verifico se o obj existe
		find(obj.getId());
		return repo.save(obj);		
	}
	
	public void delete(Integer id) {
		find(id);
		
		try {
			repo.deleteById(id);
		} catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos!");
		}	
	}

}
