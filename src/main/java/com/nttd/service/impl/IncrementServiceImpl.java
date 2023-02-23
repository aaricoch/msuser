package com.nttd.service.impl;

import com.nttd.redis.Increment;
import com.nttd.service.IncrementService;

import io.quarkus.redis.datasource.ReactiveRedisDataSource;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class IncrementServiceImpl implements IncrementService  {

    @Inject
    ReactiveRedisDataSource reactiveRedisDataSource;

    @Override
    public Uni<Increment> add(Increment increment) {
        return this.reactiveRedisDataSource.value(String.class).set(increment.getKey(), increment.getValue())
                .flatMap((v) -> Uni.createFrom().item(increment));
    }
    
    @Override
    public Uni<String> get(String key) {
        return this.reactiveRedisDataSource.value(String.class).get(key);
    }
   
}
