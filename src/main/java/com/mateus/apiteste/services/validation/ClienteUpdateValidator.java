package com.mateus.apiteste.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.mateus.apiteste.domain.Cliente;
import com.mateus.apiteste.domain.enums.TipoCliente;
import com.mateus.apiteste.dto.ClienteDTO;
import com.mateus.apiteste.repositories.ClienteRepository;
import com.mateus.apiteste.resources.exceptions.FieldMessage;
import com.mateus.apiteste.services.validation.utils.BR;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	@Autowired
	private HttpServletRequest request;
	
	@Override
	public void initialize(ClienteUpdate ann) {
		
	}
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		// Cria um URI para buscar o valor de parametro informado no request (Exemplo: http://localhost/8080/clientes/2, onde 2 é o prm)
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriID = Integer.parseInt(map.get("id"));
		
		// Cria a lista de erro
		List<FieldMessage> list = new ArrayList<>();
		
		List<Cliente> aux = new ArrayList<Cliente>();
		aux = repo.findByEmail(objDto.getEmail());
		
		for (int i = 0; i < aux.size(); i++) {
			// caso o ID seja diferente do prm, valida
			if ((aux.get(i).getId() != uriID) && (objDto.getEmail().equals(aux.get(i).getEmail()))) {
				list.add(new FieldMessage("Email", "Email já cadastrado para outro cliente!"));
				break;
			}
		}
		
		// inclusão dos erros (for em cima da lista gerada).
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		
		return list.isEmpty();
	}
	

}
