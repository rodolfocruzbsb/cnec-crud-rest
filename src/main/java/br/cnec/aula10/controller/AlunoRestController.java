package br.cnec.aula10.controller;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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
import br.cnec.aula10.domain.Sexo;
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

		List<Aluno> alunos = service.buscarTodos();

		if (alunos.isEmpty()) {

			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Aluno>>(alunos, HttpStatus.OK);
	}

	@GetMapping(value = "/carga/{prefixo}")
	public ResponseEntity<List<Aluno>> carga(@PathVariable("prefixo") String prefixo) {

		if (prefixo != null && prefixo.length() > 0) {

			for (int i = 0; i < 10; i++) {
				try {

					Aluno aluno = criarAlunoParaCarga(prefixo, i);

					if (!service.existe(aluno)) {
						
						service.salvar(aluno);
					}
				} catch (Exception ignore) {

					logger.info("Erro ao cadastrar aluno via carga: " + ignore.getMessage());
				}
			}
		}

		List<Aluno> alunos = service.buscarPorPrefixo(prefixo);

		if (alunos.isEmpty()) {

			return new ResponseEntity(HttpStatus.NO_CONTENT);
		}

		return new ResponseEntity<List<Aluno>>(alunos, HttpStatus.OK);
	}

	private Aluno criarAlunoParaCarga(String prefixo, int i) {

		Aluno aluno = new Aluno();

		aluno.setNome(prefixo.toUpperCase() + "_" + ( i + 1 ));

		LocalDate data = null;

		if (i < 5) {

			// Maior de idade
			data = LocalDate.of(LocalDate.now().getYear() - ( 18 + i ), LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
		} else {

			// menor de idade
			data = LocalDate.of(LocalDate.now().getYear() - i, LocalDate.now().getMonthValue(), LocalDate.now().getDayOfMonth());
		}

		aluno.setDataNascimento(Date.from(data.atStartOfDay(ZoneId.systemDefault()).toInstant()));

		aluno.setSexo(i % 2 == 0 ? Sexo.M : Sexo.F);
		return aluno;
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<?> buscarAlunoPorId(@PathVariable("id") long id) {

		logger.info("Buscando Aluno com id {}", id);

		Aluno aluno = service.buscarPorId(id);

		if (aluno == null) {

			logger.error("Aluno com id {} não encontrado.", id);

			return new ResponseEntity(new MensagemDeRetorno("Aluno com id " + id + " não encontrado"), HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Aluno>(aluno, HttpStatus.OK);
	}

	@PostMapping(value = "/")
	public ResponseEntity<?> criarAluno(@RequestBody Aluno aluno, UriComponentsBuilder ucBuilder) {

		logger.info("Criando Aluno : {}", aluno);

		if (service.existe(aluno)) {

			logger.error("Não foi possível criar. Um Aluno com o nome {} já existe", aluno.getNome());

			return new ResponseEntity(new MensagemDeRetorno("Não foi possível criar. Um Aluno com o nome " + aluno.getNome() + " já existe."), HttpStatus.CONFLICT);
		}

		service.salvar(aluno);

		HttpHeaders headers = new HttpHeaders();

		headers.setLocation(ucBuilder.path("/api/alunos/{id}").buildAndExpand(aluno.getId()).toUri());

		return new ResponseEntity<String>(headers, HttpStatus.CREATED);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<?> atualizarAluno(@PathVariable("id") long id, @RequestBody Aluno aluno) {

		logger.info("Atualizando Aluno com id {}", id);

		Aluno currentAluno = service.buscarPorId(id);

		if (currentAluno == null) {
			logger.error("Não foi possível atualiza. Aluno com id {} não encontrado.", id);
			return new ResponseEntity(new MensagemDeRetorno("Não foi possível atualiza. Aluno com id " + id + " não encontrado."), HttpStatus.NOT_FOUND);
		}

		currentAluno.setNome(aluno.getNome());
		currentAluno.setDataNascimento(aluno.getDataNascimento());
		currentAluno.setSexo(aluno.getSexo());

		service.salvar(currentAluno);
		return new ResponseEntity<Aluno>(currentAluno, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> deletarUsuario(@PathVariable("id") long id) {

		logger.info("Buscando & Deletando Aluno com id {}", id);

		Aluno aluno = service.buscarPorId(id);
		if (aluno == null) {
			logger.error("Não foi possível deletar. Aluno com id {} não encontrado.", id);
			return new ResponseEntity(new MensagemDeRetorno("Não foi possível deletar. Aluno com id " + id + " não encontrado."), HttpStatus.NOT_FOUND);
		}
		service.deletar(id);
		return new ResponseEntity<Aluno>(HttpStatus.NO_CONTENT);
	}

}
