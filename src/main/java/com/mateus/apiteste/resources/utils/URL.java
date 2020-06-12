package com.mateus.apiteste.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class URL {
	
	public static List<Integer> decodeIntList(String s){
		String[] vet = s.split(",");
		
		// pode ser montado uma lista pelo for, ou apenas retornar conforme a última linha (para otimizar e deixar limpo o código).
		List<Integer> lista = new ArrayList<Integer>();
		for (int i=0; i < vet.length; i++) {
				lista.add(Integer.parseInt(vet[i]));		
		}
		
		//return lista;
		return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
		
	}
	
	
	public static String decodeParam(String param) {
		try {
			return URLDecoder.decode(param, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			return "";
		}
		
	}

}
