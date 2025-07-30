package com.example.webflux.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reactive")
@Slf4j
public class ReactiveProgrammingExampleController {

    @GetMapping("/onenine/legecy")
    public Mono<List<Integer>> produceOneToNineLegacy() {
        return Mono.fromCallable(() -> {
            List<Integer> sink = new ArrayList<>();
            for (int i = 1; i <= 9; i++) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {}
                sink.add(i);
            }
            return sink;
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @GetMapping("/onenine/list")
    public Mono<List<Integer>> produceOneToNineList() {
        return Mono.defer(() -> {
            List<Integer> sink = new ArrayList<>();
            for (int i = 1; i <= 9; i++) {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {}
                sink.add(i);
            }
            return Mono.just(sink);
        }).subscribeOn(Schedulers.boundedElastic());
    }
}
