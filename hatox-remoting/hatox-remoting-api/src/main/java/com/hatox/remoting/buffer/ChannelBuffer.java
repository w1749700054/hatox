package com.hatox.remoting.buffer;

public interface ChannelBuffer extends Comparable<ChannelBuffer>{
    //获取buffer大小
    int capacity();
    //清空buffer
    void clear();
    //copy buffer
    ChannelBuffer copy();
    //copy buffer 从index开始，长度为lengt
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

    void getBytes(int index,byte[] dst);

    void getBytes(int index,byte[]dst,int dstIndex,int length);

    void getBytes(int index,ChannelBuffer buffer);

    void getBytes(int index,ChannelBuffer buffer,int length);

    void markReaderIndex();

    void markWriterIndex();

    boolean readable();

    int readableBytes();

    byte readByte();

    void readBytes(byte[] dst);

    void readBytes(byte[] dst, int dstIndex, int length);

    int readerIndex();

    void readerIndex(int readerIndex);

    void setBytes(byte[] bytes);

    void setByte(byte b);

    void setByte(int index, byte b);

    void setBytes(int index, byte[] src);

    void setBytes(int index, byte[] src, int srcIndex, int length);

    void setBytes(int index, ChannelBuffer src);

    void setBytes(int index, ChannelBuffer src, int length);

    void  setBytes(ChannelBuffer src,int srcIndex,int length);

    void setBytes(int index,ChannelBuffer src,int srcIndex,int length);

    byte[] array();

    int writerIndex();

    void  writerIndex(int writerIndex);

    int writableBytes();

    void setIndex(int readerIndex,int writerIndex);

    void writeByte(byte b);

    void writeBytes(byte[] src);

    void writeBytes(byte[] src,int srcIndex,int length);

    void writeBytes(ChannelBuffer src);

    void writeBytes(ChannelBuffer src,int srcIndex,int length);

}
