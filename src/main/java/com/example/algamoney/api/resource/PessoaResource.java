package com.example.algamoney.api.resource;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.event.RecursoCriadoEvent;
import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRespository;

@RestController
@RequestMapping("/pessoas")
public class PessoaResource {
	
	@Autowired
	PessoaRespository pessoaRespository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Pessoa> listar(){		
		return pessoaRespository.findAll();		
	}
	
	@GetMapping("/{codigoId}")
	public ResponseEntity<Pessoa> buscarPeloCodigo( @PathVariable Long codigoId) {
		Optional<Pessoa> pessoa = pessoaRespository.findById(codigoId);
		
		return pessoa.isPresent() ? 
				ResponseEntity.ok(pessoa.get()) : ResponseEntity.notFound().build();
		
	}
	
	@PostMapping	
	public ResponseEntity<Pessoa> criar(@Valid @RequestBody Pessoa pessoa,
			HttpServletResponse response) {
		Pessoa PessoaSalva = pessoaRespository.save(pessoa);
		
	publisher.publishEvent(new RecursoCriadoEvent(this, response, PessoaSalva.getCodigo()));
		
		return ResponseEntity.status(HttpStatus.CREATED).body(PessoaSalva);
	}
	
	@DeleteMapping("/{codigoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover( @PathVariable Long codigoId) {
		pessoaRespository.deleteById(codigoId);
	}

}
