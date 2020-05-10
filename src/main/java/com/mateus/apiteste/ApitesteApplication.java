package com.mateus.apiteste;

import java.text.SimpleDateFormat;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mateus.apiteste.domain.Categoria;
import com.mateus.apiteste.domain.Cidade;
import com.mateus.apiteste.domain.Cliente;
import com.mateus.apiteste.domain.Endereco;
import com.mateus.apiteste.domain.Estado;
import com.mateus.apiteste.domain.ItemPedido;
import com.mateus.apiteste.domain.Pagamento;
import com.mateus.apiteste.domain.PagamentoComBoleto;
import com.mateus.apiteste.domain.PagamentoComCartao;
import com.mateus.apiteste.domain.Pedido;
import com.mateus.apiteste.domain.Produto;
import com.mateus.apiteste.domain.enums.EstadoPagamento;
import com.mateus.apiteste.domain.enums.TipoCliente;
import com.mateus.apiteste.repositories.CategoriaRepository;
import com.mateus.apiteste.repositories.CidadeRepository;
import com.mateus.apiteste.repositories.ClienteRepository;
import com.mateus.apiteste.repositories.EnderecoRepository;
import com.mateus.apiteste.repositories.EstadoRepository;
import com.mateus.apiteste.repositories.ItemPedidoRepository;
import com.mateus.apiteste.repositories.PagamentoRepository;
import com.mateus.apiteste.repositories.PedidoRepository;
import com.mateus.apiteste.repositories.ProdutoRepository;
import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

@SpringBootApplication
public class ApitesteApplication implements CommandLineRunner{

	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private EstadoRepository estadoRepository;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired 
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired ItemPedidoRepository itemPedidoRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(ApitesteApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		
		// Inserindo categorias e produtos
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritorio");
		Categoria cat3 = new Categoria(null, "Cama Mesa e Banho");
		Categoria cat4 = new Categoria(null, "Eletrônica");
		Categoria cat5 = new Categoria(null, "Jardinagem");
		Categoria cat6 = new Categoria(null, "Decoração");
		Categoria cat7 = new Categoria(null, "Perfumaria");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
				
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2,cat3,cat4,cat5,cat6,cat7));	
		produtoRepository.saveAll(Arrays.asList(p1,p2,p3));
		
		// Inserindo estados e cidades
		Estado est1 = new Estado(null, "Minas Gerais");
		Estado est2 = new Estado(null, "São Paulo");
		
		Cidade c1 = new Cidade(null, "Uberlândia", est1);
		Cidade c2 = new Cidade(null, "São Paulo", est2);
		Cidade c3 = new Cidade(null, "Campinas", est2);
		
		est1.getCidades().addAll(Arrays.asList(c1));
		est2.getCidades().addAll(Arrays.asList(c2,c3));
		
		estadoRepository.saveAll(Arrays.asList(est1,est2));
		cidadeRepository.saveAll(Arrays.asList(c1,c2,c3));
		
		// Inserindo cliente
		Cliente cli1 = new Cliente(null, "Maria Silva", "mariasilva@gmail.com", "0000000", TipoCliente.PESSOAFISICA);
		cli1.getTelefones().addAll(Arrays.asList("9999999", "11651561"));
		
		// Endereços
		Endereco e1 = new Endereco(null, "Rua Flores", "300", "Apto 303", "Jardim", "89120000", cli1, c1);
		Endereco e2 = new Endereco(null, "Rua Flores2", "301", "Apto 301", "Centro", "89130000", cli1, c2);
		
		// Associa os endereços ao cliente
		cli1.getEndereco().addAll(Arrays.asList(e1,e2));
		
		// salva os dados
		clienteRepository.saveAll(Arrays.asList(cli1));
		enderecoRepository.saveAll(Arrays.asList(e1,e2));
		
		
		// Inserindo pedidos
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		
		Pedido ped1 = new Pedido(null, sdf.parse("30/09/2017 10:32"), cli1, e1);
		Pedido ped2 = new Pedido(null, sdf.parse("10/10/2017 19:35"), cli1, e2);
		
		Pagamento pagto1 = new PagamentoComCartao(null, EstadoPagamento.QUIDADO, ped1, 6);
		// seta o pagamento no pedido
		ped1.setPagamento(pagto1);
		
		Pagamento pagto2 = new PagamentoComBoleto(null, EstadoPagamento.PENDENTE, ped2, sdf.parse("20/10/2017 00:00"), null);
		ped2.setPagamento(pagto2);
		
		cli1.getPedidos().addAll(Arrays.asList(ped1,ped2));
		
		pedidoRepository.saveAll(Arrays.asList(ped1, ped2));
		pagamentoRepository.saveAll(Arrays.asList(pagto1, pagto2));
		
		//Insere os itens de pedido
		ItemPedido ip1 = new ItemPedido(ped1, p1, 0.00, 1, 2000.00);
		ItemPedido ip2 = new ItemPedido(ped1, p3, 0.00, 2, 80.00);
		ItemPedido ip3 = new ItemPedido(ped2, p2, 100.00, 1, 800.00);
		
		ped1.getItens().addAll(Arrays.asList(ip1,ip2));
		ped2.getItens().addAll(Arrays.asList(ip3));
		
		
		p1.getItens().addAll(Arrays.asList(ip1));
		p2.getItens().addAll(Arrays.asList(ip3));
		p3.getItens().addAll(Arrays.asList(ip2));
		
		itemPedidoRepository.saveAll(Arrays.asList(ip1,ip2,ip3));
	}
	
	
	
}
