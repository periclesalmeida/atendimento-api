package br.com.periclesalmeida.atendimento.util;

import java.util.List;

public class VerificadorUtil {
	
	public static boolean estaNulo(Object objeto) {
		return objeto == null;
	}

	public static boolean naoEstaNulo(Object objeto) {
		return objeto != null;
	}

	public static boolean estaVazio(Object valor) {
		return valor.toString().isEmpty();
	}
	
	public static boolean naoEstaNuloOuVazio(Object objeto) {
		Boolean isNaoEstaNuloOuVazio = objeto != null && !objeto.toString().isEmpty();
		if (objeto instanceof List<?>) {
			return isNaoEstaNuloOuVazio && !((List<?>) objeto).isEmpty();
		}
		return isNaoEstaNuloOuVazio;
	}
	
	public static boolean estaNuloOuVazio(Object valor) {
		return estaNulo(valor) || estaVazio(valor);
	}
	
	public static void verificarNulo(Object objeto, String mensagemErro) {
		if (objeto == null) {
			throw new RuntimeException(mensagemErro);
		}
	}
	
	public static boolean isListaNulaOuVazia(List<? extends Object> lista) {
		return estaNulo(lista) || isListaVazia(lista);
	}

	public static boolean isListaVazia(List<? extends Object> lista) {
		return lista.isEmpty();
	}

	public static boolean isListaComElementos(List<? extends Object> lista) {
		return !isListaVazia(lista);
	}

}