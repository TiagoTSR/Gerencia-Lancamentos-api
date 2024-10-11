package br.com.xdecodex.data.vo.v2;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.xdecodex.model.Address;
import jakarta.persistence.Embedded;
import jakarta.persistence.Transient;

public class PersonVOV2 {

	private Long id;
	private String name;
	private Date birthDay;

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

	public Date getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(Date birthDay) {
		this.birthDay = birthDay;
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
		return Objects.hash(enabled, birthDay, id, address, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PersonVOV2 other = (PersonVOV2) obj;
		return Objects.equals(enabled, other.enabled) && Objects.equals(birthDay, other.birthDay)
				&& Objects.equals(id, other.id) && Objects.equals(address, other.address)
				&& Objects.equals(name, other.name);
	}
	
}