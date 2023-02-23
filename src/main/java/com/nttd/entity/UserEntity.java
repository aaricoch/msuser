package com.nttd.entity;

import org.bson.codecs.pojo.annotations.BsonProperty;

import io.quarkus.mongodb.panache.common.MongoEntity;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MongoEntity(collection="BQMUSER")
public class UserEntity extends ReactivePanacheMongoEntity {

    @BsonProperty("code_customer")
    private long idcustomer;
    @BsonProperty("code_card")
    private long idcard;    
    private String cardnumber;
    private String password;
    private int pin;
    private String state;

    public UserEntity() {
    }

    public UserEntity(long idcustomer, long idcard, String cardnumber, String password,int pin,String state) {
        this.idcustomer = idcustomer;
        this.idcard = idcard;
        this.cardnumber = cardnumber;
        this.password = password;
        this.pin = pin;
        this.state = state;
    }

   
}
