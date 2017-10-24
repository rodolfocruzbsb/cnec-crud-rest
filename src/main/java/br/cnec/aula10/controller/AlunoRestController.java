package br.cnec.aula10.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.cnec.aula10.controller.util.MensagemDeRetorno;
import br.cnec.aula10.domain.Aluno;
import br.cnec.aula10.service.AlunoService;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@RequestMapping("/api/alunos")
public class AlunoRestController {

	public static final Logger logger = LoggerFactory.getLogger(AlunoRestController.class);

	@Autowired
	private AlunoService service;

	@GetMapping(value = "/")
	public ResponseEntity<List<Aluno>> buscarTodos() {

		List<Aluno> users = service.buscarTodos();

		if (users.isEmpty()) {

			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Aluno>>(users, HttpStatus.OK);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> buscarAlunoPorId(@PathVariable("id") long id) {

		logger.info("Buscando Aluno com id {}", id);

		Aluno user = service.buscarPorId(id);

		if (user == null) {

			logger.error("Aluno com id {} não encontrado.", id);

			return new ResponseEntity(new MensagemDeRetorno("Aluno com id " + id + " não encontrado"), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Aluno>(user, HttpStatus.OK);
	}

	@PostMapping(value = "/")
	public ResponseEntity<?> criarAluno(@RequestBody Aluno user, UriComponentsBuilder ucBuilder) {

		logger.info("Criando Aluno : {}", user);

		if (service.existe(user)) {

			logger.error("Não foi possível criar. Um Aluno com o nome {} já existe", user.getNome());

			return new ResponseEntity(new MensagemDeRetorno("Não foi possível criar. Um Aluno com o nome " + user.getNome() + " já existe."), HttpStatus.CONFLICT);
		}

		service.salvar(user);

		HttpHeaders headers = new HttpHeaders();

		headers.setLocation(ucBuilder.path("/api/alunos/{id}").buildAndExpand(user.getId()).toUri());

		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> atualizarAluno(@PathVariable("id") long id, @RequestBody Aluno user) {

		logger.info("Atualizando Aluno com id {}", id);

		Aluno currentAluno = service.buscarPorId(id);

		if (currentAluno == null) {
			logger.error("Não foi possível atualiza. Aluno com id {} não encontrado.", id);
			return new ResponseEntity(new MensagemDeRetorno("Não foi possível atualiza. Aluno com id " + id + " não encontrado."), HttpStatus.NOT_FOUND);
		}

		currentAluno.setNome(user.getNome());
		currentAluno.setDataNascimento(user.getDataNascimento());
		currentAluno.setSexo(user.getSexo());

		service.salvar(currentAluno);
		return new ResponseEntity<Aluno>(currentAluno, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deletarUsuario(@PathVariable("id") long id) {

		logger.info("Buscando & Deletando Aluno com id {}", id);

		Aluno user = service.buscarPorId(id);
		if (user == null) {
			logger.error("Não foi possível deletar. Aluno com id {} não encontrado.", id);
			return new ResponseEntity(new MensagemDeRetorno("Não foi possível deletar. Aluno com id " + id + " não encontrado."), HttpStatus.NOT_FOUND);
		}
		service.deletar(id);
		return new ResponseEntity<Aluno>(HttpStatus.NO_CONTENT);
	}

}
