package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@SpringBootTest
public class BasicMonoOperatorTest {

    @Test
    public void startMonoFromData() {
        Mono.just(1).subscribe(x -> System.out.println("data = " + x));

        //ex. 사소한 에러 났을때 로그를 남기고 Mono를 전파
        Mono.empty().subscribe(x -> System.out.println("empty data = " + x));
    }

    /**
     * fromCallable -> 동기적인 객체를 반환할 때 사용
     * defer -> Mono를 반환하고 싶을 때 사용
     */
    @Test
    public void startMonoFromFunction() {
        Mono<String> monoFromCallable = Mono.fromCallable(() -> {
            return callRestTemplate("안녕!!");
        }).subscribeOn(Schedulers.boundedElastic());

        Mono<String> MonoFromDefer = Mono.defer(() -> {
            return callWebClient("안녕!");
        });
        MonoFromDefer.subscribe();
    }

    /**
     * defer의 필요성 테스트
     */
    @Test
    public void testDeferNecessity() {
        Mono.defer(() -> {
            String a = "안녕";
            String b = "하세";
            String c = "요";
            return callWebClient(a + b + c);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<String> callWebClient(String request) {
        return Mono.just(request + "callWebClient");
    }

    public String callRestTemplate(String request) {
        return request + "callRestTemplate 응답";
    }

    // 데이터 흐름 / 가공 / 구독
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

    /**
     * Mono에서 데이터 방출수가 많아져서 Flux로 변경하고 싶을 경우 -> flatMapMany
     */
    @Test
    public void monoToFlux() {
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(x -> {
            return Flux.just(x, x + 1, x + 2);
        });
        integerFlux.subscribe(x -> System.out.println("data = " + x));
    }
}
