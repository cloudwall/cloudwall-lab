package io.cloudwall.lab.lang;

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

        int numEightByteLoops = value.length / 8;
        int remainder = value.length % 8;

        for (int i = 0; i < numEightByteLoops; i += 8) {
            NativeBytes.UNSAFE.putByte(baseAddress + i, value[i]);
            NativeBytes.UNSAFE.putByte(baseAddress + i + 1, value[i]);
            NativeBytes.UNSAFE.putByte(baseAddress + i + 2, value[i]);
            NativeBytes.UNSAFE.putByte(baseAddress + i + 3, value[i]);
            NativeBytes.UNSAFE.putByte(baseAddress + i + 4, value[i]);
            NativeBytes.UNSAFE.putByte(baseAddress + i + 5, value[i]);
            NativeBytes.UNSAFE.putByte(baseAddress + i + 6, value[i]);
            NativeBytes.UNSAFE.putByte(baseAddress + i + 7, value[i]);
        }

        baseAddress += 8 * numEightByteLoops;
        for (int i = 0; i < remainder; i++) {
            NativeBytes.UNSAFE.putByte(baseAddress + i, value[i]);
        }
    }
}
