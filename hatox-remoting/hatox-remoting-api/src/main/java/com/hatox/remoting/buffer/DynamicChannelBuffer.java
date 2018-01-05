package com.hatox.remoting.buffer;

public class DynamicChannelBuffer extends AbstractChannelBuffer{
    ChannelBufferFactory factory;
    private ChannelBuffer buffer;
    public DynamicChannelBuffer(int estimatedLength){
        this(estimatedLength, HeapChannelBufferFactory.getInstance());
    }

    public DynamicChannelBuffer(int estimatedLength, ChannelBufferFactory instance) {
        if(estimatedLength<0){
            throw new IllegalArgumentException("estimatedLength is not less than 0");
        }
        if (instance==null){
            throw new NullPointerException("channelBufferFactory is not null");
        }
        factory=instance;
        buffer=instance.getBuffer(estimatedLength);
    }
    public void ensureWritableBytes(int writableBytes){
        if(writableBytes<writableBytes()){
            return;
        }
        int newCapacity;
        if (capacity()==0){
            newCapacity=1;
        }else{
            newCapacity=capacity();
        }
        while(newCapacity<writableBytes){
            newCapacity<<=1;
        }
        ChannelBuffer newBuffer=factory.getBuffer(newCapacity);
        newBuffer.setBytes(buffer,0,writerIndex());
        buffer=newBuffer;
    }
    @Override
    public int capacity() {
        return buffer.capacity();
    }

    @Override
    public ChannelBuffer copy(int index, int length) {
        return buffer.copy(index,length);
    }

    @Override
    public ChannelBufferFactory factory() {
        return null;
    }

    @Override
    public byte getByte(int index) {
        return buffer.getByte(index);
    }

    @Override
    public void getBytes(int index, byte[] dst, int dstIndex, int length) {
        buffer.getBytes(index,dst,dstIndex,length);
    }

    @Override
    public void getBytes(int index, ChannelBuffer buffer, int length) {
        buffer.getBytes(index,buffer,length);
    }

    @Override
    public void setByte(int index, byte b) {
        ensureWritableBytes(1);
        buffer.setByte(b);
    }

    @Override
    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        ensureWritableBytes(length);
        buffer.setBytes(index,src,srcIndex,length);
    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
        ensureWritableBytes(length);
        buffer.setBytes(index,src,srcIndex,length);
    }

    @Override
    public byte[] array() {
        return buffer.array();
    }

    @Override
    public int compareTo(ChannelBuffer o) {
        return 0;
    }
}
