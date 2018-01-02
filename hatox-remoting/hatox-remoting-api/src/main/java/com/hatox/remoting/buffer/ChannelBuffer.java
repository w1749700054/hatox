package com.hatox.remoting.buffer;

public interface ChannelBuffer extends Comparable<ChannelBuffer>{
    //获取buffer大小
    int capacity();
    //清空buffer
    void clear();
    //copy buffer
    ChannelBuffer copy();
    //copy buffer 从index开始，长度为length
    ChannelBuffer copy(int index,int length);
    //回收已读空间
    void discardReadBytes();
    //确保足够的空间
    void ensureWritableBytes(int writableBytes);
    //
    public boolean equals(Object o);
    //获取Buffer工厂
    ChannelBufferFactory factory();

    byte getByte(int index);

    void setByte(int index);

    void getBytes(int index,byte[] dst);

    void getBytes(int index,byte[]dst,int dstIndex,int length);

    void getBytes(int index,ChannelBuffer buffer);

    void getBytes(int index,ChannelBuffer buffer,int length);

    void setBytes(byte[] bytes);

    void markReaderIndex();

    void markWriterIndex();

    boolean readable();

    int readableBytes();

    byte readByte();

    void readBytes(byte[] dst);

    void readBytes(byte[] dst, int dstIndex, int length);

    int readerIndex();

    void readerIndex(int readerIndex);

    void setByte(int index, int value);

    void setBytes(int index, byte[] src);

    byte[] array();

    int writerIndex();

    void  writerIndex(int writerIndex);

    int writableBytes();

}
