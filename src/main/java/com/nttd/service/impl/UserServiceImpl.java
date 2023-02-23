package com.nttd.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import com.nttd.api.AuditApi;
import com.nttd.api.BankCardApi;
import com.nttd.api.CustomerApi;
import com.nttd.api.request.AuditRequest;
import com.nttd.api.request.BankCardRequest;
import com.nttd.api.request.CustomerRequest;
import com.nttd.api.response.AuditResponse;
import com.nttd.api.response.BankCardResponse;
import com.nttd.dto.ValidationUserDto;
import com.nttd.entity.UserEntity;
import com.nttd.redis.Increment;
import com.nttd.service.IncrementService;
import com.nttd.service.UserService;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class UserServiceImpl  implements UserService {

    @Inject
    IncrementService  incrementService;

    @RestClient
    BankCardApi bankCardApi;

    @RestClient
    CustomerApi customerApi;

    @ConfigProperty(name="valor.code.exitoso")
    int code_ok;
   
    @ConfigProperty(name="valor.code.creacion")
    int code_create;

    @ConfigProperty(name="valor.flag.tarjeta")
    String flag_tarjeta;

    @ConfigProperty(name="valor.flag.cliente")
    String flag_customer;    

    @ConfigProperty(name="valor.flag.activo")
    String flag_activo;    

    @ConfigProperty(name="valor.flag.inactivo")
    String flag_inactivo;    
   
    @RestClient
    AuditApi auditApi;

    @ConfigProperty(name="valor.app.aplicacion")
    String valor_aplicacion; 

    @ConfigProperty(name="valor.app.useraplicacion")
    String valor_useraplicacion; 

    @ConfigProperty(name="valor.app.usersesion")
    String valor_usersesion; 


    public String ofuscar(String valor){
        if (valor == null || 2 > valor.length()) {
            return "**************";
        }
        return "************"+valor.substring(valor.length() - 2);

    }

    @Override
    public Uni<AuditResponse> addAuditar() {

        AuditRequest auditoria = new AuditRequest(valor_aplicacion, valor_useraplicacion, 
        valor_usersesion,"getValidationCard --> false");  
      

        return  auditApi.add(auditoria).onItem().call( (obj) -> {
                Increment incremento = Increment.builder()
                .key("id1").value("25")
                .build();

                return incrementService.add(incremento).flatMap(a ->{

                        return Uni.createFrom().item(obj);
                });



        })  ;
        
    }






    @Override
    public Uni<ValidationUserDto> getValidationCard(String cardnumber) {
       
    
        BankCardRequest bcrq =  new BankCardRequest(cardnumber);
        return bankCardApi.getAllBankCard(bcrq).onItem().                
            transform(rsp -> {
                ValidationUserDto validation = new ValidationUserDto(false);
                //Auditoria
                AuditRequest auditoria = new AuditRequest(valor_aplicacion, valor_useraplicacion, 
                                                valor_usersesion,"getValidationCard --> false");                                
                    if(rsp != null && rsp.getCode() == code_ok){
                        validation.setRespuesta(true);                        
                        validation.setIdcard(rsp.getBankCardEntity().getIdBANKCARD());
                        validation.setPin(rsp.getBankCardEntity().getPin());
                        validation.setDuedate(rsp.getBankCardEntity().getDuedate());
                        auditoria.setRequest("cardnumber:"+ofuscar(cardnumber.toString()));
                        auditoria.setMensaje("getValidationCard --> true");
                        auditoria.setResponse("bankcard:"+ofuscar(rsp.getBankCardEntity().getIdBANKCARD()+"")+"-"+
                                              "-pin:"+ofuscar(rsp.getBankCardEntity().getPin()+"")+"-"+
                                              "-duedate:"+ofuscar(rsp.getBankCardEntity().getDuedate()+"")+"-");
                    }
                    validation.setAuditRequest(auditoria);
                  return validation;
                
            }).call((rslfinal)->{
                return auditApi.add(rslfinal.getAuditRequest()).flatMap(r->{
                    rslfinal.setAuditRequest(null);
                    return Uni.createFrom().item(rslfinal);
                });

            });

               
    }

    @Override
    public Uni<ValidationUserDto>  getValidationCustomer(String tipocliente,
                                                            String customernumber) {
   
        CustomerRequest crq =  new CustomerRequest(tipocliente,customernumber);
        return customerApi.getAllCustomer(crq).onItem().                
            transform(rsp -> {
                ValidationUserDto validation = new ValidationUserDto(false);
                //Auditoria
                AuditRequest auditoria = new AuditRequest(valor_aplicacion, valor_useraplicacion, 
                                                valor_usersesion,"getValidationCustomer --> false");
                    if(rsp != null && rsp.getCode() == code_ok){
                        validation.setRespuesta(true);
                        validation.setIdcustomer(rsp.getCustomer().getIdCustomer());
                        auditoria.setRequest("customer:"+tipocliente+"-"+ofuscar(customernumber.toString()));
                        auditoria.setMensaje("getValidationCustomer --> true");
                        auditoria.setResponse("idcustomer:"+ofuscar(rsp.getCustomer().getIdCustomer()+""));
                    } 
                    validation.setAuditRequest(auditoria);                    
                return validation; 
            }).call((rslfinal)->{
                return auditApi.add(rslfinal.getAuditRequest()).flatMap(r->{
                    rslfinal.setAuditRequest(null);
                    return Uni.createFrom().item(rslfinal);
                });

            });
    }

    @Override
    public Uni<Boolean> getAccesoMulticanal(String cardnumber, String password){
        Map<String, Object> params = new HashMap<>();
        params.put("cardnumber", cardnumber);
        params.put("password", password);
        params.put("state", flag_activo);
        return UserEntity.find("cardnumber = :cardnumber and password = :password and state = :state",
                    params).count().flatMap(count ->{
                        return Uni.createFrom().item(count.longValue() > 0);
            });

    }
   
    @Override
    public Uni<UserEntity> addUser(UserEntity userEntity) {

        Uni<BankCardResponse> responsecard = bankCardApi.getBankCardById(userEntity.getIdcard());
        return responsecard.flatMap(r->{
                
            // validar que el pin sea de la tarjeta y tenga 6 digitos
            if( r.getBankCardEntity().getPin() == userEntity.getPin() &&
                userEntity.getPassword().length() == 6){ 

                    Map<String, Object> params = new HashMap<>();
			        params.put("code_customer", userEntity.getIdcustomer());
			        params.put("code_card", userEntity.getIdcard());
                    params.put("state", flag_activo);
                    // validar que no tenga duplicidad de registro multicanal.
                    Uni<Long> countCard = UserEntity
                                .find("code_customer = :code_customer and code_card = :code_card and state = :state",
                                      params).count();
                    
                    return countCard.flatMap(count ->{
                        if(count.longValue() > 0){
                            return Uni.createFrom().item(new UserEntity());
                        }else{
                            userEntity.setState(flag_activo);
                            return userEntity.persist();
                        } 
                    });

                }else return Uni.createFrom().item(new UserEntity());
            }).call((user)->{
                //Auditoria
                AuditRequest auditoria = new AuditRequest(valor_aplicacion, valor_useraplicacion, 
                                                valor_usersesion,"addUser --> true");
                auditoria.setRequest("idcustomer:"+ofuscar(userEntity.getCardnumber())+
                                        "-pass:"+ofuscar(userEntity.getPassword())+
                                        "-pin:"+ofuscar(userEntity.getPin()+"")+"-");
                auditoria.setResponse(user.toString());  
                return auditApi.add(auditoria).flatMap(r->{
                    return Uni.createFrom().item(user);
                });



            });
    }

   
    @Override
    public Uni<UserEntity> updateUser(String id, UserEntity userEntity) {
        Uni<UserEntity> postUni = UserEntity.findById(new ObjectId(id));
        return postUni
                .onItem().transform(post -> {
                    post.setPassword(userEntity.getPassword());                    
                    return post;
                }).call((post) -> {

                        //Auditoria
                        AuditRequest auditoria = new AuditRequest(valor_aplicacion, valor_useraplicacion, 
                        valor_usersesion,"updateUser --> true");
                        auditoria.setRequest("idOnject:"+id+"-");
                        auditoria.setResponse(post.toString());  
                        return auditApi.add(auditoria).flatMap(r->{
                            return post.persistOrUpdate();
                        });
                });
    }

    @Override
    public Uni<UserEntity> deleteUser(String id){
        Uni<UserEntity> postUni = UserEntity.findById(new ObjectId(id));
        return postUni
                .onItem().transform(post -> {
                    post.setState(flag_inactivo);                    
                    return post;
                }).call((post) ->{ //Auditoria
                            AuditRequest auditoria = new AuditRequest(valor_aplicacion, valor_useraplicacion, 
                            valor_usersesion,"deleteUser --> true");
                            auditoria.setRequest("idOnject:"+id+"-");
                            auditoria.setResponse(post.toString());  
                            return auditApi.add(auditoria).flatMap(r->{
                                return post.persistOrUpdate();
                            });
                    }  );
    }
}
