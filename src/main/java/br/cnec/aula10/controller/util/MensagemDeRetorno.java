package br.cnec.aula10.controller.util;

public class MensagemDeRetorno {

	private String errorMessage;

	public MensagemDeRetorno(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

}
