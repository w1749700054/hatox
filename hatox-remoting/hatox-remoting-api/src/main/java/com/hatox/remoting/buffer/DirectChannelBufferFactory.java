package com.hatox.remoting.buffer;

public class DirectChannelBufferFactory implements ChannelBufferFactory {
    private static ChannelBufferFactory factory=new DirectChannelBufferFactory();
    private DirectChannelBufferFactory(){
    }
    public static ChannelBufferFactory getInstance(){
        return factory;
    }
    @Override
    public ChannelBuffer getBuffer(int capacity) {
        return ChannelBuffers.directBuffer(capacity);
    }

    @Override
    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        return ChannelBuffers.directBuffer(array,offset,length);
    }
}
