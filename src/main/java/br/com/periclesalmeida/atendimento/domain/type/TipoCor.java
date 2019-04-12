package br.com.periclesalmeida.atendimento.domain.type;

import java.util.HashMap;
import java.util.Map;

public enum TipoCor {

	AZUL("A"),
	VERMELHO("R"),
	AMARELO("M"),
	VERDE("V");

	private String value;

	private TipoCor(String value) {
		this.value = value;
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

	public String getNome() {
		return this.name();
	}

	public static Map<String, String> getMapValues() {
		Map<String, String> map = new HashMap<>();
		for (TipoCor item : TipoCor.values()) {
			map.put(item.getNome(), item.getValue());
		}
		return map;
	}

}
