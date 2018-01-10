package com.hatox.remoting.buffer;

public class DynamicChannelBufferFactory implements ChannelBufferFactory {
    private static final ChannelBufferFactory factory=new DynamicChannelBufferFactory();

    private DynamicChannelBufferFactory(){

    }
    public static ChannelBufferFactory getInstance(){
        return factory;
    }
    @Override
    public ChannelBuffer getBuffer(int capacity) {
       return  ChannelBuffers.dynamicBuffer(capacity);
    }

    @Override
    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        return null;
    }
}
