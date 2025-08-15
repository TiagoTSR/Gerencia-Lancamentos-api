package br.com.xdecodex.data.vo.v1;

import java.io.Serializable;
import java.util.Objects;

import org.springframework.hateoas.RepresentationModel;

public class CidadeVO extends RepresentationModel<CidadeVO> implements Serializable {
	
	private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private EstadoVO estado;

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

    public EstadoVO getEstado() {
        return estado;
    }

    public void setEstado(EstadoVO estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CidadeVO other = (CidadeVO) obj;
		return Objects.equals(estado, other.estado) && Objects.equals(id, other.id) && Objects.equals(nome, other.nome);
	}
    
}
