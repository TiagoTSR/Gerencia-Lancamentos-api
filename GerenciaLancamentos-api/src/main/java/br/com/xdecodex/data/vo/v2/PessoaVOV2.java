package br.com.xdecodex.data.vo.v2;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.xdecodex.model.Endereco;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;

public class PessoaVOV2 {

	private Long codigo;
	private String nome;
	private Date birthDay;

	@Embedded
	private Endereco endereco;
	private Boolean ativo;

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}
	
	@JsonIgnore
	@Transient
	public boolean isInativo() {
		return !this.ativo;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ativo, birthDay, codigo, endereco, nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PessoaVOV2 other = (PessoaVOV2) obj;
		return Objects.equals(ativo, other.ativo) && Objects.equals(birthDay, other.birthDay)
				&& Objects.equals(codigo, other.codigo) && Objects.equals(endereco, other.endereco)
				&& Objects.equals(nome, other.nome);
	}
	
}