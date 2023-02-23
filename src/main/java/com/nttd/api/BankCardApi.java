package com.nttd.api;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import com.nttd.api.request.BankCardRequest;
import com.nttd.api.response.BankCardResponse;

import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@RegisterRestClient
@Path("/bankcard")
public interface BankCardApi {
    
    @GET
    public Uni<BankCardResponse> getAllBankCard(BankCardRequest bankCardRequest);

    @GET
    @Path("/{id}")
    public Uni<BankCardResponse> getBankCardById(long id);

}
