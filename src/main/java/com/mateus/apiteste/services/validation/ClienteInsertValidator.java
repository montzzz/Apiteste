package com.mateus.apiteste.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.mateus.apiteste.domain.Cliente;
import com.mateus.apiteste.domain.enums.TipoCliente;
import com.mateus.apiteste.dto.ClienteNewDTO;
import com.mateus.apiteste.repositories.ClienteRepository;
import com.mateus.apiteste.resources.exceptions.FieldMessage;
import com.mateus.apiteste.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Override
	public void initialize(ClienteInsert ann) {
		
	}
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		// Cria a lista de erro
		List<FieldMessage> list = new ArrayList<>();
		
		// Verifica se a pessoa é fisica e valida se o CPF é valido (isValidCPF)
		if (objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) &&  !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF inválido!"));
		}
		
		// Verifica se a pessoa é juridica e valida se o CNPJ é valido (isValidCNPJ)
		if (objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) &&  !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido!"));
		}
		
		Cliente aux = repo.findByEmail(objDto.getEmail());
		if (aux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		// inclusão dos erros (for em cima da lista gerada).
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}
	

}
