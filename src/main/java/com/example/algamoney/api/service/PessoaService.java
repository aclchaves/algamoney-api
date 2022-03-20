package com.example.algamoney.api.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Pessoa;
import com.example.algamoney.api.repository.PessoaRespository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRespository pessoaRespository;

	
	public Pessoa atualizar(Long codigo, Pessoa pessoa) {		
		Optional<Pessoa> pessoaSalva = pessoaRespository.findById(codigo);
		if(pessoaSalva.isPresent()) {
			Pessoa pessoaTemp = pessoaSalva.get();
			BeanUtils.copyProperties(pessoa, pessoaTemp, "codigo");			
			return pessoaRespository.save(pessoaTemp);
		}else {
			throw new EmptyResultDataAccessException(1);
		}
	}

}
