package io.cloudwall.lab;

import net.openhft.lang.io.NativeBytes;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;

/**
 * Experimental class to help demonstrate performance difference between putByte() and copyMemory() for small arrays.
 * To make the JIT assembler more readable this class has no bounds checking at all -- this is not a real-world or
 * safe example for use of Unsafe; it can core dump if mis-used.
 */
public class FastAppender {
    private static final int BYTES_OFFSET;
    static {
        BYTES_OFFSET = NativeBytes.UNSAFE.arrayBaseOffset(byte[].class);
    }

    private final long address;
    private final long putByteCutoff;

    private int limit;

    public FastAppender(long size, int putByteCutoff) {
        this.putByteCutoff = putByteCutoff;
        this.address = NativeBytes.UNSAFE.allocateMemory(size);
        for (long i = 0; i < size; i++) {
            putByte(i, (byte)0);
        }
        reset();
    }

    public long length() {
        return limit;
    }

    public void append(@Nonnull byte[] bytes) {
        int oldLimit = limit;
        limit += bytes.length;
        putBytes(oldLimit, bytes);
    }

    public void reset() {
        limit = 0;
    }

    private void putByte(long index, byte value) {
        NativeBytes.UNSAFE.putByte(address + index, value);
    }

    private void putBytes(long startIndex, byte[] value) {
        if (value.length <= putByteCutoff) {
            long baseAddress = address + startIndex;
            for (int i = 0; i < value.length; i++) {
                NativeBytes.UNSAFE.putByte(baseAddress + i, value[i]);
            }
        } else {
            NativeBytes.UNSAFE.copyMemory(value, BYTES_OFFSET, null, address + startIndex, value.length);
        }
    }
}
