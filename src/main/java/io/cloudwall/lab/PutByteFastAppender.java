package io.cloudwall.lab;

import net.openhft.lang.io.NativeBytes;

/**
 * Appender that uses Unsafe's putByte operation for all byte[] appends.
 */
public class PutByteFastAppender extends FastAppender {
    public PutByteFastAppender(long size) {
        super(size);
    }

    @Override
    protected void putBytes(long startIndex, byte[] value) {
        long baseAddress = address + startIndex;
        for (int i = 0; i < value.length; i++) {
            NativeBytes.UNSAFE.putByte(baseAddress + i, value[i]);
        }
    }
}
