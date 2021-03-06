package br.cnec.aula10.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.cnec.aula10.domain.Aluno;

public interface AlunoRepository extends JpaRepository<Aluno, Long> {

	@Query("select a from Aluno a where upper(a.nome) = upper(?1)")
	List<Aluno> findByName(String nome);

	@Query("select a from Aluno a where upper(a.nome) like :prefix%")
	List<Aluno> findByPrefix(@Param("prefix") String prefix);

}
