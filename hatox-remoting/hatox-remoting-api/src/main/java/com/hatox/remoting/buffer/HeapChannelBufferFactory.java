package com.hatox.remoting.buffer;

import java.nio.ByteBuffer;

public class HeapChannelBufferFactory implements ChannelBufferFactory {
    static ChannelBufferFactory factory=new HeapChannelBufferFactory();
    private HeapChannelBufferFactory(){

    }
    public static ChannelBufferFactory getInstance(){
        return factory;
    }
    @Override
    public ChannelBuffer getBuffer(int capacity) {
        return null;
    }

    @Override
    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        return null;
    }

    @Override
    public ChannelBuffer getBuffer(ByteBuffer nioBuffer) {
        return null;
    }
}
