package br.com.xdecodex.data.vo.v1;

import java.io.Serializable;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.xdecodex.model.Address;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PersonVO extends RepresentationModel<PersonVO> implements Serializable {
	
	@JsonIgnore
    private List<Link> links;
	
	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;

	@Embedded
	private Address address;
	private Boolean enabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
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
		PersonVO other = (PersonVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}