package com.nttd.api;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;


import com.nttd.api.request.AuditRequest;
import com.nttd.api.response.AuditResponse;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;

@RegisterRestClient
@Path("/audit")
public interface AuditApi {
    


    @POST
    public Uni<AuditResponse> add( AuditRequest auditRequest);
}
