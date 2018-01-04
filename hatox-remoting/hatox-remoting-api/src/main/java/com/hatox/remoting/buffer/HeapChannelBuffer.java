package com.hatox.remoting.buffer;

public class HeapChannelBuffer extends AbstractChannelBuffer{
    private final byte[] array;
    public HeapChannelBuffer(int capacity){
        this(new byte[capacity],0,0);
    }
    public HeapChannelBuffer(byte[] bytes) {
        this(bytes,0,bytes.length);
    }

    public HeapChannelBuffer(byte[] bytes, int readerIndex, int writeIndex) {
        if(bytes==null){
            throw new NullPointerException("bytes");
        }
        this.array=bytes;
        setIndex(readerIndex,writeIndex);
    }

    @Override
    public int capacity() {
        return array.length;
    }

    @Override
    public ChannelBuffer copy(int index, int length) {
        byte[] bytes=new byte[length];
        System.arraycopy(array,readerIndex(),bytes,0,length);
        return new HeapChannelBuffer(bytes);
    }

    @Override
    public ChannelBufferFactory factory() {
        return null;
    }

    @Override
    public byte getByte(int index) {
        return array[index];
    }

    @Override
    public void getBytes(int index, byte[] dst, int dstIndex, int length) {
        System.arraycopy(array,index,dst,dstIndex,length);
    }

    @Override
    public void getBytes(int index, ChannelBuffer buffer, int length) {
        if(buffer instanceof HeapChannelBuffer){
            getBytes(index,((HeapChannelBuffer) buffer).array,0,length);
        }
    }

    @Override
    public void setByte(int index, byte b) {
        array[index]=b;
    }
    @Override
    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        System.arraycopy(src,srcIndex,array,index,length);
    }

    @Override
    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
        if(src instanceof HeapChannelBuffer){
            setBytes(index,((HeapChannelBuffer) src).array,srcIndex,length);
        }
    }

    @Override
    public byte[] array() {
        return array;
    }

    @Override
    public int compareTo(ChannelBuffer o) {
        return 0;
    }
}
