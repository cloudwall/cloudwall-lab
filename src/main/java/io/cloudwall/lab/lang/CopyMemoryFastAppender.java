package io.cloudwall.lab.lang;

import net.openhft.lang.io.NativeBytes;

/**
 * Appender that uses Unsafe's copyMemory operation for all byte[] appends.
 */
public class CopyMemoryFastAppender extends FastAppender {
    private static final int BYTES_OFFSET;
    static {
        BYTES_OFFSET = NativeBytes.UNSAFE.arrayBaseOffset(byte[].class);
    }

    public CopyMemoryFastAppender(long size) {
        super(size);
    }

    @Override
    protected void putBytes(long startIndex, byte[] value) {
        NativeBytes.UNSAFE.copyMemory(value, BYTES_OFFSET, null, address + startIndex, value.length);
    }
}
