package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class SchedulerTest {

    @Test
    public void testBasicFluxMono() {
        // Mono는 0~1개만 처리가능
        Mono.<Integer>just(2)
                .map(data -> {
                    System.out.println("map Thread Name1 = " + Thread.currentThread().getName());
                    return data * 2;
                })
                .publishOn(Schedulers.parallel())
                .filter(data -> {
                    System.out.println("map Thread Name2 = " + Thread.currentThread().getName());
                    return data % 4 == 0;
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(x -> System.out.println("Mono가 구독한 data! = " + x));
    }
}
