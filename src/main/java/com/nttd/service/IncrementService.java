package com.nttd.service;

import com.nttd.redis.Increment;
import com.nttd.service.IncrementService;
import io.smallrye.mutiny.Uni;



public interface IncrementService {

    public Uni<Increment> add(Increment increment);

    public Uni<String> get(String key);
}
