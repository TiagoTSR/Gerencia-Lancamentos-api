package br.com.xdecodex.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.xdecodex.model.TypeLaunch;

public class LaunchStatisticDay {

    private TypeLaunch type;

    private LocalDate day;

    private BigDecimal total;

    public LaunchStatisticDay(TypeLaunch type, LocalDate day, BigDecimal total) {
        this.type = type;
        this.day = day;
        this.total = total;
    }

    public TypeLaunch getType() {
        return type;
    }

    public void setType(TypeLaunch type) {
        this.type = type;
    }

    public LocalDate getDay() {
        return day;
    }

    public void setDay(LocalDate day) {
        this.day = day;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

}
