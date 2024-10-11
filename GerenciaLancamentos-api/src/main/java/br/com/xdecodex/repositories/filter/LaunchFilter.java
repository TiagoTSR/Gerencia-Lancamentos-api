package br.com.xdecodex.repositories.filter;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class LaunchFilter {

	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expirationDateFrom;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate expiryDateBy;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getExpirationDateFrom() {
		return expirationDateFrom;
	}

	public void setExpirationDateFrom(LocalDate expirationDateFrom) {
		this.expirationDateFrom = expirationDateFrom;
	}

	public LocalDate getExpiryDateBy() {
		return expiryDateBy;
	}

	public void setExpiryDateBy(LocalDate expiryDateBy) {
		this.expiryDateBy = expiryDateBy;
	}

}