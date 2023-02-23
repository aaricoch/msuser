package com.nttd.api.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankCardObject {

    private long IdBANKCARD;  
    private String cardnumber;
    private int pin;
    private String duedate;
    private int validationcode;

}
