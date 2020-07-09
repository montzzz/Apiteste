package com.mateus.apiteste.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mateus.apiteste.domain.Categoria;
import com.mateus.apiteste.domain.Cliente;
import com.mateus.apiteste.domain.ItemPedido;
import com.mateus.apiteste.domain.PagamentoComBoleto;
import com.mateus.apiteste.domain.Pedido;
import com.mateus.apiteste.domain.enums.EstadoPagamento;
import com.mateus.apiteste.repositories.ItemPedidoRepository;
import com.mateus.apiteste.repositories.PagamentoRepository;
import com.mateus.apiteste.repositories.PedidoRepository;
import com.mateus.apiteste.repositories.ProdutoRepository;
import com.mateus.apiteste.security.UserSS;
import com.mateus.apiteste.services.exceptions.AuthorizationException;
import com.mateus.apiteste.services.exceptions.ObjectNotfoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired ClienteService clienteService;
	
	public Pedido find(Integer id) {
		
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotfoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());		
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(produtoService.find(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);			
		}
		
		itemPedidoRepository.saveAll(obj.getItens());	
		
		System.out.println(obj);
		
		return obj;
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		UserSS user = UserService.authenticated();
		
		if (user == null) {
			throw new AuthorizationException("Acesso negado!");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		Cliente cliente = clienteService.find(user.getId());
		
		return repo.findByCliente(cliente, pageRequest);
	}

}
