package com.example.webflux.chapter2;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class OperatorFlatMapTest {
    /**
     * Mono<Mono<T>> -> Mono<T>
     * Mono<Flux<T>> -> Flux<T>
     * Flux<Mono<T>> -> Flux<T>
     */

    @Test
    public void monoToFlux() {
        Mono<Integer> one = Mono.just(1);
        Flux<Integer> integerFlux = one.flatMapMany(x -> {
            return Flux.just(x, x + 1, x + 2);
        });
        integerFlux.subscribe(x -> System.out.println("data = " + x));
    }

    @Test
    public void testWebClientFlatMap() {
        // 데이터의 방출은 순서대로가 아닌 처리가 빠른 순으로 방출 3 -> 2 -> 1 순서
        Flux<String> flatMap = Flux.just(callWebClient("1단계 - 문제 이해하기", 1500),
                        callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                        callWebClient("3단계 - 최종 응답", 500))
                .flatMap(monoData -> {
                    return monoData;
                });

        flatMap.subscribe(x -> System.out.println("data = " + x));

        // 순서를 보장하려면 flatMapSequential
        Flux<String> flatMapSequential = Flux.just(callWebClient("1단계 - 문제 이해하기", 1500),
                        callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                        callWebClient("3단계 - 최종 응답", 500))
                .flatMapSequential(monoData -> {
                    return monoData;
                });

        flatMapSequential.subscribe(x -> System.out.println("FlatMap Sequential data = " + x));

        // merge도 flatMap과 유사 -> flatMapSequential과 동일하게 mergeSequnetial도 존재
        Flux<String> merge = Flux.merge(callWebClient("1단계 - 문제 이해하기", 1500),
                        callWebClient("2단계 - 문제 단계별로 풀어가기", 1000),
                        callWebClient("3단계 - 최종 응답", 500));
//                .map(~~~~~~); map에서 작업하면 flatMap과 동일한 구조

        merge.subscribe(x -> System.out.println("merge data = " + x));

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Mono<String> callWebClient(String request, long delay) {
        return Mono.defer(() -> {
            try {
                Thread.sleep(delay);
                return Mono.just(request + " -> 딜레이: " + delay);
            } catch (Exception e) {
                return Mono.empty();
            }
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
