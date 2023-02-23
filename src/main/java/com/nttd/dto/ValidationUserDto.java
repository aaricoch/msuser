package com.nttd.dto;



import com.fasterxml.jackson.annotation.JsonInclude;
import com.nttd.api.request.AuditRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
public class ValidationUserDto {
    
    private boolean respuesta;
    private long idcustomer;
    private long idcard;
    private int pin;
    private String duedate;
    private AuditRequest auditRequest;

    public ValidationUserDto(boolean respuesta) {
        this.respuesta = respuesta;
    }

    



    public ValidationUserDto(boolean respuesta, long idcard, int pin,String duedate) {
        this.respuesta = respuesta;
        this.idcard = idcard;
        this.pin = pin;
        this.duedate = duedate;
    }






    
    

}
