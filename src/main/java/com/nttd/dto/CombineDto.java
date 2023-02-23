package com.nttd.dto;

import com.nttd.api.response.AuditResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CombineDto {

    private Object principal;
    private AuditResponse audi;
    
}
