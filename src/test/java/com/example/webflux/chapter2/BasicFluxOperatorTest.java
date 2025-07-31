package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicFluxOperatorTest {

    @Test
    public void testFluxFromData() {
        Flux.just(1,2,3,4)
                .subscribe(x -> System.out.println("data = " + x));

        List<Integer> basicList = List.of(1, 2, 3, 4);
        Flux.fromIterable(basicList)
                .subscribe(x -> System.out.println("data = " + x));
    }

    @Test
    public void testFluxFromFuction() {
        Flux.defer(() -> {
            return Flux.just(1,2,3,4);
        }).subscribe(x -> System.out.println("data from defer = " + x));

        Flux.create(sink -> {
            sink.next(1);
            sink.next(2);
            sink.next(3);
            sink.complete();
        }).subscribe(x -> System.out.println("data from sink = " + x));
    }

    @Test
    public void testSinkDetail() {
        Flux.<String>create(sink -> {
            AtomicInteger counter = new AtomicInteger(0);
            recursiveFunction(sink);
        })
                .contextWrite(Context.of("counter", new AtomicInteger(0)))
                .subscribe(x -> System.out.println("data from recursive = " + x));
    }

    public void recursiveFunction(FluxSink<String> sink) {
        AtomicInteger counter = sink.contextView().get("counter");
        if (counter.incrementAndGet() < 10) {   // ++int
            sink.next("sink count " + counter);
            recursiveFunction(sink);
        } else {
            sink.complete();
        }
    }

    /**
     * Mono -> Flux변환 flatMapMany
     * Flux -> Mono변환 collectList
     */
    @Test
    public void testFluxCollectList() {
        Mono<List<Integer>> listMono = Flux.just(1, 2, 3, 4, 5)
                .map(x -> x * 2)
                .filter(x -> x % 4 == 0)
                .collectList();

        listMono.subscribe(x -> System.out.println("collectList가 변환한 list data = " + x));
    }
}
