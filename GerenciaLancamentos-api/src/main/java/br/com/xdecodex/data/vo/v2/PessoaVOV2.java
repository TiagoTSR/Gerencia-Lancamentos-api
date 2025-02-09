package br.com.xdecodex.data.vo.v2;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.xdecodex.model.Endereco;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;

public class PessoaVOV2 {

	private Long id;
	private String nome;
	private Date dataNascimento;

	@Embedded
	private Endereco endereco;
	private Boolean enabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	@JsonIgnore
	@Transient
	public boolean isInenabled() {
		return !this.enabled;
	}

	@Override
	public int hashCode() {
		return Objects.hash(enabled, dataNascimento, id, endereco, nome);
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
		return Objects.equals(enabled, other.enabled) && Objects.equals(dataNascimento, other.dataNascimento)
				&& Objects.equals(id, other.id) && Objects.equals(endereco, other.endereco)
				&& Objects.equals(nome, other.nome);
	}
	
}