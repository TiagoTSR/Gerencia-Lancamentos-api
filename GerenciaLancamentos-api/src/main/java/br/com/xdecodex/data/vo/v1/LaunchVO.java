package br.com.xdecodex.data.vo.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.xdecodex.model.Category;
import br.com.xdecodex.model.Person;
import br.com.xdecodex.model.TypeLaunch;
import br.com.xdecodex.serialization.converter.LocalDateDeserializer;
import br.com.xdecodex.serialization.converter.LocalDateSerializer;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LaunchVO extends RepresentationModel<LaunchVO> implements Serializable {
	
	@JsonIgnore
    private List<Link> links;

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String description;
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate expirationDate;
	
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate paymentDate;

	private BigDecimal value;

	private String observation;

	@Enumerated(EnumType.STRING)
	private TypeLaunch type;

	private Category category;

	private Person person;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}

	public TypeLaunch getType() {
		return type;
	}

	public void setType(TypeLaunch type) {
		this.type = type;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
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
		LaunchVO other = (LaunchVO) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
