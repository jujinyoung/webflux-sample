package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class FluxMonoErrorAndSignalTest {

    @Test
    public void testBasicSignal() {
        Flux.just(1, 2, 3, 4)
                .doOnNext(publishedData -> System.out.println(publishedData))
                .doOnComplete(() -> System.out.println("스티림 종료"))
                .doOnError(ex -> {
                    System.out.println("ex 에러 발생 = " + ex);
                })
                .subscribe(data -> System.out.println("data = " + data));
    }

    @Test
    public void testFluxMonoError() {
        try {
            Flux.just(1, 2, 3, 4)
                    .map(data -> {
                        // 에러가 발생할 경우 Exception은 스트림 내부에서 처리해야함.
                        if (data == 3) {
                            throw new RuntimeException();
                        }
                        return data;
                    })
                    .onErrorMap(ex -> new IllegalArgumentException())
                    .onErrorReturn(999)
                    .onErrorComplete()
                    .subscribe(data -> System.out.println("data = " + data));
        } catch (Exception e) {
            System.out.println("에러 발생");
        }
    }

    @Test
    public void testFluxMonoDotError() {
        Flux.just(1, 2, 3, 4)
                .flatMap(data -> {
                    if (data != 3) {
                        return Mono.just(data);
                    } else {
                        return Mono.error(new RuntimeException());
                    }
                }).subscribe(data -> System.out.println("data = " + data));
    }
}
