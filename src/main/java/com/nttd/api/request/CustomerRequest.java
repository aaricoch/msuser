package com.nttd.api.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerRequest {
    
    private String typeCustomer;
	private String numberDocument;
	private String name;
	private String lastname;

	
	public CustomerRequest(String typeCustomer, String numberDocument) {
		this.typeCustomer = typeCustomer;
		this.numberDocument = numberDocument;
	}

	

}
