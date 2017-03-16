package com.ksy.jmh.benchmark;

import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.junit.Test;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

/**
 * Created by 1003918 on 2017. 3. 16..
 */
public class MainTest {

    @State(Scope.Thread)
    public static class BenchmarkState
    {
        List<Integer> list;

        @Setup(Level.Trial)
        public void initialize() {
            list = new LinkedList<>();
        }

        @TearDown(Level.Iteration)
        public void tearDown() {
            list.clear();
        }

    }


    @Test
    public void test1() throws Exception {
        Options options = new OptionsBuilder()
                                .include(this.getClass().getName() + ".benchmark1")
                                .mode(Mode.AverageTime)
                                .timeUnit(TimeUnit.MILLISECONDS)
                                .warmupTime(TimeValue.seconds(1))
                                .warmupIterations(2)
                                .measurementTime(TimeValue.seconds(1))
                                .measurementIterations(5)
                                .threads(1)
                                .forks(1)
                                .shouldFailOnError(true)
                                .shouldDoGC(true)
                                .build();

        double result = new Runner(options)
                                .runSingle()
                                .getPrimaryResult()
                                .getScore();

        assertThat(result).isLessThan(3.0d);
    }


    @Test
    public void test2() throws RunnerException {
        Options options = new OptionsBuilder()
                                .include(this.getClass().getName() + ".benchmark2")
                                .mode(Mode.AverageTime)
                                .timeUnit(TimeUnit.MILLISECONDS)
                                .warmupTime(TimeValue.seconds(1))
                                .warmupIterations(2)
                                .measurementTime(TimeValue.seconds(1))
                                .measurementIterations(5)
                                .threads(1)
                                .forks(1)
                                .shouldFailOnError(true)
                                .shouldDoGC(true)
                                .build();

        double result = new Runner(options)
                                .runSingle()
                                .getPrimaryResult()
                                .getScore();

        assertThat(result).isLessThan(2.0d);
    }



    @Benchmark
    public void benchmark1 (BenchmarkState state, Blackhole blackhole) {
        List<Integer> list = state.list;

        for(int i=0; i<100; i++)
            blackhole.consume(list.add(i));
    }


    @Benchmark
    public void benchmark2 (BenchmarkState state, Blackhole blackhole) {
        List<Integer> list = state.list;

        for(int i=0; i<100; i++)
            blackhole.consume(list.add(i));
    }
}

