package com.nttd.api;

 
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.nttd.api.request.CustomerRequest;
import com.nttd.api.response.CustomerResponse;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RegisterRestClient
@Path("/customer")
public interface CustomerApi {
    

   @GET
   public Uni<CustomerResponse> getAllCustomer(CustomerRequest customerRequest);

}
