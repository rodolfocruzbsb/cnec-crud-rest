package br.cnec.aula10.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cnec.aula10.dao.AlunoRepository;
import br.cnec.aula10.domain.Aluno;

@Service("alunoService")
public class AlunoServiceImpl implements AlunoService {

	@Autowired
	private AlunoRepository repository;

	@Override
	public Aluno salvar(Aluno aluno) {

		return repository.save(aluno);
	}

	@Override
	public void deletar(Aluno aluno) {

		repository.delete(aluno);
	}

	@Override
	public void deletar(Long id) {

		Aluno aluno = this.buscarPorId(id);

		repository.delete(aluno);
	}

	@Override
	public List<Aluno> buscarTodos() {

		return repository.findAll();
	}

	@Override
	public Aluno buscarPorId(Long id) {

		return repository.findOne(id);
	}

	@Override
	public List<Aluno> buscarPorNome(String nome) {

		return repository.findByName(nome);
	}

	@Override
	public boolean existe(Aluno user) {

		return user != null && !this.buscarPorNome(user.getNome()).isEmpty();
	}
}
