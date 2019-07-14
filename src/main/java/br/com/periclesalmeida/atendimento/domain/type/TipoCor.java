package br.com.periclesalmeida.atendimento.domain.type;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TipoCor {

	AZUL("A","#3352FF"),
	VERMELHO("R", "#FF5733"),
	AMARELO("M", "#FFC133"),
	VERDE("V", "#39FF33");

	private String value;
	private String html;

	private TipoCor(String value, String html) {
		this.value = value;
		this.html = html;
	}

	public String getValue() {
		return this.value;
	}

	public static TipoCor parse(String value) {
		TipoCor tipoCor = null;
		for (TipoCor item : TipoCor.values()) {
			if (item.getValue().equals(value)) {
				tipoCor = item;
				break;
			}
		}
		return tipoCor;
	}

	public String getName() {
		return this.name();
	}

	public String getHtml() { return this.html;}
}
