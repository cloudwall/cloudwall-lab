package io.cloudwall.lab;

import net.openhft.lang.io.NativeBytes;
import sun.misc.Unsafe;

import javax.annotation.Nonnull;

/**
 * Experimental class to help demonstrate performance difference between putByte() and copyMemory() for small arrays.
 */
public class FastAppender {
    private Unsafe unsafe = NativeBytes.UNSAFE;
    private static final int BYTES_OFFSET;
    static {
        BYTES_OFFSET = NativeBytes.UNSAFE.arrayBaseOffset(byte[].class);
    }

    private final long size;
    private final long address;
    private final long putByteCutoff;

    private int limit;

    public FastAppender(long size, int putByteCutoff) {
        this.size = size;
        this.putByteCutoff = putByteCutoff;
        this.address = unsafe.allocateMemory(size);
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
        if (limit > size) {
            throw new ArrayIndexOutOfBoundsException("exceeded maximum capacity of " + size + " bytes; " +
                    "attempted to add " + bytes.length + " bytes");
        }
        putBytes(oldLimit, bytes);
    }

    public void reset() {
        limit = 0;
    }

    private void putByte(long index, byte value) {
        assert((index >= 0) && (index < size));
        unsafe.putByte(address + index, value);
    }

    private void putBytes(long startIndex, byte[] value) {
        assert((startIndex >= 0) && ((startIndex + value.length) < size));
        if (value.length <= putByteCutoff) {
            for (int i = 0; i < value.length; i++) {
                unsafe.putByte(address + startIndex + i, value[i]);
            }
        } else {
            unsafe.copyMemory(value, BYTES_OFFSET, null, address + startIndex, value.length);
        }
    }
}
