package com.nttd.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuditRequest {

    private String aplicacion;
    private String usuarioAplicacion;
    private String usuarioSesion;
    private String codigoTransaccion;
    private String fechaTransaccion;
    private String mensaje;
    private String request;
    private String response;

    
    public AuditRequest(String aplicacion, String usuarioAplicacion, String usuarioSesion,String mensaje) {
        this.aplicacion = aplicacion;
        this.usuarioAplicacion = usuarioAplicacion;
        this.usuarioSesion = usuarioSesion;
        this.mensaje = mensaje;
    }


    
    
}
