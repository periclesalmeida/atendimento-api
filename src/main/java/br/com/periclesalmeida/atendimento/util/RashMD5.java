package br.com.periclesalmeida.atendimento.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class RashMD5 {
	
	private static final String ERRO_AO_GERAR_RASH = "Erro ao gerar rash.";
	private static final String COMPLETAR_RASH_MD5_COM_ZERO = "0";
	private static final String TIPO_RASH_MD5 = "MD5";

	public static String gerarRash (String string){
		if(string != null){
			try {
				MessageDigest md = MessageDigest.getInstance(TIPO_RASH_MD5);
			    BigInteger hash = new BigInteger(1, md.digest(string.getBytes()));
			    String senhaCriptografada = hash.toString(16);
			    if (senhaCriptografada.length() %2 != 0) {
			    	senhaCriptografada = COMPLETAR_RASH_MD5_COM_ZERO + senhaCriptografada;  
			    }
			    return senhaCriptografada;
			}catch (Exception e) {
				throw new RuntimeException(ERRO_AO_GERAR_RASH);
			}
		}
	    return null;
	}  


}
