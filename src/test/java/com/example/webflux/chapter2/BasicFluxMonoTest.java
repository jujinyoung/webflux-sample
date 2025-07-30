package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest
public class BasicFluxMonoTest {

    @Test
    public void testBasicFluxMono() {
        // Flux는 여러개의 데이터를 처리가능
        Flux.just(1, 2, 3, 4, 5)
                .map(x -> x * 2)
                .filter(x -> x % 4 == 0)
                .subscribe(x -> System.out.println("Flux가 구독한 data! = " + x));

        // Mono는 0~1개만 처리가능
        Mono.just(2)
                .map(x -> x * 2)
                .filter(x -> x % 4 == 0)
                .subscribe(x -> System.out.println("Mono가 구독한 data! = " + x));
    }

    @Test
    public void testFluxMonoBlock() {
        Mono<String> justString = Mono.just("String");
        String string = justString.block();
        System.out.println("string = " + string);
    }
}
