package com.mateus.apiteste.services;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.mateus.apiteste.domain.Cidade;
import com.mateus.apiteste.domain.Cliente;
import com.mateus.apiteste.domain.Endereco;
import com.mateus.apiteste.domain.enums.Perfil;
import com.mateus.apiteste.domain.enums.TipoCliente;
import com.mateus.apiteste.dto.ClienteDTO;
import com.mateus.apiteste.dto.ClienteNewDTO;
import com.mateus.apiteste.repositories.CidadeRepository;
import com.mateus.apiteste.repositories.ClienteRepository;
import com.mateus.apiteste.repositories.EnderecoRepository;
import com.mateus.apiteste.security.UserSS;
import com.mateus.apiteste.services.exceptions.AuthorizationException;
import com.mateus.apiteste.services.exceptions.DataIntegrityException;
import com.mateus.apiteste.services.exceptions.ObjectNotfoundException;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repo;	
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	
	public Cliente find(Integer id) {
		
		UserSS user = UserService.authenticated();
		
		if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		Optional<Cliente> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotfoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEndereco());
		return obj; 
	}
	
	public Cliente update(Cliente obj) {
		// verifico se o obj existe
		Cliente newObj = find(obj.getId()); 
		updateData(newObj, obj);
		return repo.save(newObj);		
	}
	
	public void delete(Integer id) {
		find(id);
		
		try {
			repo.deleteById(id);
		} catch(DataIntegrityViolationException e){
			throw new DataIntegrityException("Não é possível excluir porque há pedidos vinculados!");
		}	
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDto(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null);
	}
	
	public Cliente fromDto(ClienteNewDTO objDto) {
		Cliente cli = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum(objDto.getTipo()), /*faz o encode da senha*/pe.encode(objDto.getSenha()));
		Optional<Cidade> cid = cidadeRepository.findById(objDto.getCidadeId());
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(), cli, cid.get());
		cli.getEndereco().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		
		if (objDto.getTelefone2() != null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if (objDto.getTelefone3() != null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;		
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		return s3Service.uploadFile(multipartFile);
	}
}
