package br.com.xdecodex.mapper.custom;

import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.xdecodex.data.vo.v2.PessoaVOV2;
import br.com.xdecodex.model.Pessoa;

@Service
public class PessoaMapper {
	
	public PessoaVOV2 convertyEntityToVo(Pessoa pessoa) {
		PessoaVOV2 vo = new PessoaVOV2();
		vo.setId(pessoa.getId());
		vo.setNome(pessoa.getNome());
		vo.setDataNascimento(new Date());
		vo.setEndereco(pessoa.getEndereco());
		vo.setEnabled(pessoa.getEnabled());
		return vo;
	}
	
	public Pessoa convertyVoToEntity(PessoaVOV2 pessoa) {
		Pessoa entity = new Pessoa();
		entity.setId(pessoa.getId());
		entity.setNome(pessoa.getNome());
		/*entity.setDataNascimento(new Date());*/
		entity.setEndereco(pessoa.getEndereco());
		entity.setEnabled(pessoa.getEnabled());
		return entity;
	}
	
}