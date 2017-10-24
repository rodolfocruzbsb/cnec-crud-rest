package br.cnec.aula10.service;

import java.util.List;

import br.cnec.aula10.domain.Aluno;

public interface AlunoService {

	Aluno salvar(Aluno aluno);

	void deletar(Aluno aluno);

	void deletar(Long id);

	List<Aluno> buscarTodos();

	Aluno buscarPorId(Long id);

	boolean existe(Aluno user);

	List<Aluno> buscarPorNome(String nome);

}
