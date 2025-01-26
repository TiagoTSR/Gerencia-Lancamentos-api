package br.com.xdecodex.dto;

import java.math.BigDecimal;

import br.com.xdecodex.model.Category;

public class LaunchStatisticCategory {

	private Category category;

	private BigDecimal total;

	public LaunchStatisticCategory(Category category, BigDecimal total) {
		this.category = category;
		this.total = total;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}
}
