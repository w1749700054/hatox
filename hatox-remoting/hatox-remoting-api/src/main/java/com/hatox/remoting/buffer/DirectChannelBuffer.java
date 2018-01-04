package com.hatox.remoting.buffer;

import java.nio.ByteBuffer;

public class DirectChannelBuffer extends AbstractChannelBuffer {

    private ByteBuffer buffer;
    public DirectChannelBuffer(int capacity){
        this(ByteBuffer.allocateDirect(capacity));
    }
    public DirectChannelBuffer(ByteBuffer buffer){
        this.buffer=buffer;
    }
    public ByteBuffer getBuffer(){
        return buffer;
    }
    @Override
    public int capacity() {
        return buffer.capacity();
    }

    @Override
    public ChannelBuffer copy(int index, int length) {
        ByteBuffer src;
        try {
            src = (ByteBuffer) buffer.duplicate().position(index).limit(index + length);
        }catch (IllegalStateException e){
            throw new IndexOutOfBoundsException();
        }
        ByteBuffer dst=buffer.isDirect()?ByteBuffer.allocate(length): ByteBuffer.allocateDirect(length);
        dst.put(src);
        dst.clear();
        return new DirectChannelBuffer(dst);
    }

    @Override
    public ChannelBufferFactory factory() {
        return null;
    }

    @Override
    public byte getByte(int index) {
        return buffer.get(index);
    }

    @Override
    public void getBytes(int index, byte[] dst, int dstIndex, int length) {
        ByteBuffer data;
        try {
            data = (ByteBuffer) buffer.duplicate().position(index).limit(index + length);
        }catch (Exception e){
            throw new IllegalStateException("buffer is not a byteBuffer type");
        }

        data.get(dst,dstIndex,length);
    }

    @Override
    public void getBytes(int index, ChannelBuffer cb, int length) {
        cb=new DirectChannelBuffer((ByteBuffer) this.buffer.duplicate().position(index).limit(length));
    }

    @Override
    public void setByte(int index, byte b) {
        buffer.put(index,b);
    }

    @Override
    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        ByteBuffer data=buffer.duplicate();
        data.position(index).limit(index+length);
        data.put(src,srcIndex,length);
    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
        if(src instanceof HeapChannelBuffer){
            setBytes(index,src.array(),srcIndex,length);
        }else if(src instanceof DirectChannelBuffer){
            DirectChannelBuffer dcb= (DirectChannelBuffer) src;
            ByteBuffer data=buffer.duplicate();
            data.position(index).limit(index+length);
            ByteBuffer byteBuffer=dcb.buffer.duplicate();
            byteBuffer.position(srcIndex).limit(srcIndex+length);
            data.put(byteBuffer);
        }
    }

    @Override
    public byte[] array() {
        buffer.put((byte) 124);
        return buffer.array();
    }

    @Override
    public int compareTo(ChannelBuffer o) {
        return buffer.compareTo(buffer);
    }
}
