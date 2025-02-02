package br.com.xdecodex.repositories.projection;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.xdecodex.model.TypeLaunch;

public class ResumeLaunch {

    private Long id;
    private String description;
    private LocalDate expirationDate;
    private LocalDate paymentDate;
    private BigDecimal value;
    private TypeLaunch type;
    private String category;
    private String person;

    public ResumeLaunch(Long id, String description, LocalDate expirationDate, LocalDate paymentDate,
            BigDecimal value, TypeLaunch type, String category, String person) {
        this.id = id;
        this.description = description;
        this.expirationDate = expirationDate;
        this.paymentDate = paymentDate;
        this.value = value;
        this.type = type;
        this.category = category;
        this.person = person;
    }

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

    public TypeLaunch getType() {
        return type;
    }

    public void setType(TypeLaunch type) {
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

}
