package br.cnec.aula10.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

@Entity
@Table(name = "ALUNO")
public class Aluno {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty
	@Size(max = 100)
	@Column(name = "NOME", nullable = false, length = 100)
	private String nome;

	@Temporal(TemporalType.DATE)
	@Column(name = "DATA_NASCIMENTO")
	private Date dataNascimento;

	@Enumerated(EnumType.STRING)
	@Column(name = "SEXO")
	private Sexo sexo;

	public Long getId() {

		return id;
	}

	public void setId(Long id) {

		this.id = id;
	}

	public String getNome() {

		return nome;
	}

	public void setNome(String nome) {

		this.nome = nome;
	}

	public Date getDataNascimento() {

		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {

		this.dataNascimento = dataNascimento;
	}

	public Sexo getSexo() {

		return sexo;
	}

	public void setSexo(Sexo sexo) {

		this.sexo = sexo;
	}

	@Override
	public String toString() {

		return "Aluno [id=" + id + ", nome=" + nome + ", dataNascimento=" + dataNascimento + ", sexo=" + sexo + "]";
	}

}
