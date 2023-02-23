package com.nttd.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankCardRequest {

    private long getIdBANKCARD;
    private String cardnumber;
    private int pin;
    private String duedate;
    private int validationcode;

    public BankCardRequest(long getIdBANKCARD) {
        this.getIdBANKCARD = getIdBANKCARD;
    }

    public BankCardRequest(String cardnumber) {
        this.cardnumber = cardnumber;
    }

    

    
    
}
