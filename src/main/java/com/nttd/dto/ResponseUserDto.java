package com.nttd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
public class ResponseUserDto {
    
    private boolean respuesta;
    private String mensaje;
    private String iduser;

    


    public ResponseUserDto() {
    }


    public ResponseUserDto(boolean respuesta) {
        this.respuesta = respuesta;
    }    

    
}
