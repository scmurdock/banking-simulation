package com.getsimplex.steptimer.model;

import java.util.Date;

public class ATMTransaction {

    private Date transactionDate;
    private Long transactionId;
    private Integer atmLocationId;

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Integer getAtmLocationId() {
        return atmLocationId;
    }

    public void setAtmLocationId(Integer atmLocationId) {
        this.atmLocationId = atmLocationId;
    }
}
