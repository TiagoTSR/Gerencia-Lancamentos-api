package br.com.xdecodex.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.xdecodex.data.vo.v2.PessoaVOV2;
import br.com.xdecodex.model.Pessoa;

@Service
public class PessoaMapper {
	
	public PessoaVOV2 convertyEntityToVo(Pessoa pessoa) {
		PessoaVOV2 vo = new PessoaVOV2();
		vo.setCodigo(pessoa.getCodigo());
		vo.setNome(pessoa.getNome());
		vo.setBirthDay(new Date());
		vo.setEndereco(pessoa.getEndereco());
		vo.setAtivo(pessoa.getAtivo());
		return vo;
	}
	
	public Pessoa convertyVoToEntity(PessoaVOV2 pessoa) {
		Pessoa entity = new Pessoa();
		entity.setCodigo(pessoa.getCodigo());
		entity.setNome(pessoa.getNome());
		/*entity.setBirthDay(new Date());*/
		entity.setEndereco(pessoa.getEndereco());
		entity.setAtivo(pessoa.getAtivo());
		return entity;
	}
	
}