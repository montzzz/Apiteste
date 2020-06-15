package com.mateus.apiteste.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.mateus.apiteste.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		// Crio um novo calendario, para adicionar 7 dias na data que vai ser inserido o pedido
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		
		pagto.setDataVencimento(cal.getTime());
	}

}
