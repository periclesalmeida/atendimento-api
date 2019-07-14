package br.com.periclesalmeida.atendimento.util;

import java.text.Normalizer;

public class StringUtil {
	
	public static String setarUpperCase(String campo) {
		return VerificadorUtil.naoEstaNulo(campo)? campo.toUpperCase() : campo;
	}
	
	public static String setarLowerCase(String campo) {
		return VerificadorUtil.naoEstaNulo(campo)? campo.toLowerCase() : campo;
	}
	
	public static String setarValorAhEsquerdaAteCompletarAhQuantidadeDeCaracteres(String string, String valor, Integer quantidadeCaracteres) {
		StringBuilder retorno = new StringBuilder();
		while (retorno.length() + string.length() < quantidadeCaracteres) {  
			retorno.append(valor);  
		}  
		retorno.append(string);
		return retorno.toString();  
	}
	
	public static String removerAcentos(String str) {
	    str = Normalizer.normalize(str, Normalizer.Form.NFD);
	    str = str.replaceAll("[^\\p{ASCII}]", "");
	    return str;
	}
	
}
