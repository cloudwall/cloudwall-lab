package io.cloudwall.lab;

import org.openjdk.jmh.annotations.*;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@State(Scope.Benchmark)
@Fork(1)
@Warmup(iterations=5, time=1, timeUnit= TimeUnit.SECONDS)
@Measurement(iterations=20, time=1, timeUnit=TimeUnit.SECONDS)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@BenchmarkMode(Mode.AverageTime)
public class FastAppenderBenchmark {
    private FastAppender appenderAlwaysCopyMemory = new CopyMemoryFastAppender(32);
    private FastAppender appenderAlwaysPutBytes = new PutByteFastAppender(32);

    private String test4 = "foo!";
    private String test12 = "foo!!!!!!!!!";
    private String test32 = "foo!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
    private byte[] testAscii4 = test4.getBytes(Charset.forName("ASCII"));
    private byte[] testAscii12 = test12.getBytes(Charset.forName("ASCII"));
    private byte[] testAscii32 = test32.getBytes(Charset.forName("ASCII"));

    @Benchmark
    public void appendBaselineStringBuilder4Bytes() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(test4);
        assertEquals(4, sb.length());
    }

    @Benchmark
    public void appendBaselineStringBuilder12Bytes() {
        StringBuilder sb = new StringBuilder(64);
        sb.append(test12);
        assertEquals(12, sb.length());
    }

    @Benchmark
    public void appendBaselineStringBuilder32Bytes() {
        StringBuilder sb = new StringBuilder();
        sb.append(test32);
        assertEquals(32, sb.length());
    }

    @Benchmark
    public void appenderAlwaysCopyMemory4Bytes() {
        appenderAlwaysCopyMemory.append(testAscii4);
        assertEquals(4, appenderAlwaysCopyMemory.length());
        appenderAlwaysCopyMemory.reset();
    }

    @Benchmark
    public void appenderAlwaysCopyMemory12Bytes() {
        appenderAlwaysCopyMemory.append(testAscii12);
        assertEquals(12, appenderAlwaysCopyMemory.length());
        appenderAlwaysCopyMemory.reset();
    }

    @Benchmark
    public void appenderAlwaysCopyMemory32Bytes() {
        appenderAlwaysCopyMemory.append(testAscii32);
        assertEquals(32, appenderAlwaysCopyMemory.length());
        appenderAlwaysCopyMemory.reset();
    }

    @Benchmark
    public void appenderAlwaysPutBytes4Bytes() {
        appenderAlwaysPutBytes.append(testAscii4);
        assertEquals(4, appenderAlwaysPutBytes.length());
        appenderAlwaysPutBytes.reset();
    }

    @Benchmark
    public void appenderAlwaysPutBytes12Bytes() {
        appenderAlwaysPutBytes.append(testAscii12);
        assertEquals(12, appenderAlwaysPutBytes.length());
        appenderAlwaysPutBytes.reset();
    }

    @Benchmark
    public void appenderAlwaysPutBytes32Bytes() {
        appenderAlwaysPutBytes.append(testAscii32);
        assertEquals(32, appenderAlwaysPutBytes.length());
        appenderAlwaysPutBytes.reset();
    }
}