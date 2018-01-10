package com.hatox.remoting.buffer;

import java.io.IOException;

public class ChannelBuffers {
    private final static ChannelBuffer EMPTY_BUFFER=new HeapChannelBuffer(0);
    public static ChannelBuffer getBuffer(int capacity) {
        if(capacity<0){
            throw new IllegalStateException("capacity is not less than 0");
        }
        if (capacity==0){
            return EMPTY_BUFFER;
        }

        return new HeapChannelBuffer(capacity);
    }

    public static ChannelBuffer getBuffer(byte[] array, int offset, int length) {
       if(array==null){
           throw new NullPointerException("array==null");
       }
       byte[] dst=new byte[length];
       System.arraycopy(array,offset,dst,0,length);
        return new HeapChannelBuffer(dst);
    }

    public static ChannelBuffer directBuffer(int capacity) {
        if(capacity<0){
            throw new IllegalStateException("capacity<0");
        }
        if(capacity==0){
            return EMPTY_BUFFER;
        }
        return new DirectChannelBuffer(capacity);
    }

    public static ChannelBuffer directBuffer(byte[] array, int offset, int length) {
        if(array==null){
            throw new NullPointerException("array==null");
        }
        if(offset<0){
            throw new IndexOutOfBoundsException("offset ("+offset+")");
        }
        if(length==0){
            return EMPTY_BUFFER;
        }
        if(array.length<offset+length){
            throw new IndexOutOfBoundsException("array.length<offset+length");
        }
        ChannelBuffer buffer=directBuffer(length);
        buffer.writeBytes(array,offset,length);
        return buffer;
    }

    public static ChannelBuffer dynamicBuffer(int capacity) {
        if (capacity < 0) {
            throw new IllegalStateException("capacity<0");
        }
        if (capacity == 0) {

        }
        return null;
    }
}
