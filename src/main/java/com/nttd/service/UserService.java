package com.nttd.service;


import com.nttd.api.response.AuditResponse;
import com.nttd.dto.ValidationUserDto;
import com.nttd.entity.UserEntity;

import io.smallrye.mutiny.Uni;


public interface UserService {

    Uni<AuditResponse> addAuditar();
    
    public Uni<ValidationUserDto> getValidationCard(String cardnumber);

    public Uni<ValidationUserDto> getValidationCustomer(String tipocliente,
                                                        String customernumber);

    public Uni<Boolean> getAccesoMulticanal(String cardnumber,
                                                        String password);

    public Uni<UserEntity> addUser(UserEntity userEntity);

    public Uni<UserEntity> updateUser(String id,UserEntity userEntity);

    public Uni<UserEntity> deleteUser(String id);

}
