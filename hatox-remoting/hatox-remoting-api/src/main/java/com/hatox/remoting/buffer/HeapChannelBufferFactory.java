package com.hatox.remoting.buffer;

import java.nio.ByteBuffer;

public class HeapChannelBufferFactory implements ChannelBufferFactory {
    private static ChannelBufferFactory factory=new HeapChannelBufferFactory();
    private HeapChannelBufferFactory(){

    }
    public static ChannelBufferFactory getInstance(){
        return factory;
    }
    @Override
    public  ChannelBuffer getBuffer(int capacity) {
        return ChannelBuffers.getBuffer(capacity);
    }

    @Override
    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        return ChannelBuffers.getBuffer(array,offset,length);
    }
}
