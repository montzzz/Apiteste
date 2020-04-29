package com.mateus.apiteste;

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
import com.mateus.apiteste.domain.Produto;
import com.mateus.apiteste.domain.enums.TipoCliente;
import com.mateus.apiteste.repositories.CategoriaRepository;
import com.mateus.apiteste.repositories.CidadeRepository;
import com.mateus.apiteste.repositories.ClienteRepository;
import com.mateus.apiteste.repositories.EnderecoRepository;
import com.mateus.apiteste.repositories.EstadoRepository;
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
	
	public static void main(String[] args) {
		SpringApplication.run(ApitesteApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		
		// Inserindo categorias e produtos
		Categoria cat1 = new Categoria(null, "Informática");
		Categoria cat2 = new Categoria(null, "Escritorio");
		
		Produto p1 = new Produto(null, "Computador", 2000.00);
		Produto p2 = new Produto(null, "Impressora", 800.00);
		Produto p3 = new Produto(null, "Mouse", 80.00);
		
		cat1.getProdutos().addAll(Arrays.asList(p1,p2,p3));
		cat2.getProdutos().addAll(Arrays.asList(p2));
		
		p1.getCategorias().addAll(Arrays.asList(cat1));
		p2.getCategorias().addAll(Arrays.asList(cat1, cat2));
		p3.getCategorias().addAll(Arrays.asList(cat1));
				
		categoriaRepository.saveAll(Arrays.asList(cat1, cat2));	
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
	}
	
	
	
}
