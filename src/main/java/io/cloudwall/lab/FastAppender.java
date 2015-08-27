package io.cloudwall.lab;

import net.openhft.lang.io.NativeBytes;

import javax.annotation.Nonnull;

/**
 * Experimental class to help demonstrate performance difference between putByte() and copyMemory() for small arrays.
 * To make the JIT assembler more readable this class has no bounds checking at all -- this is not a real-world or
 * safe example for use of Unsafe; it can core dump if mis-used.
 */
public abstract class FastAppender {

    protected final long address;
    private int limit;

    public FastAppender(long size) {
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

    protected abstract void putBytes(long startIndex, byte[] value);
}
