package br.com.periclesalmeida.atendimento.util.exception;

public class NegocioException extends RuntimeException {

	public NegocioException(String mensagemErro) {
		super(mensagemErro);
	}
}