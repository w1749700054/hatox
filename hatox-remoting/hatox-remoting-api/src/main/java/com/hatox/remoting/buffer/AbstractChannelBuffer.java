package com.hatox.remoting.buffer;

public abstract class AbstractChannelBuffer implements ChannelBuffer{
    private int readerIndex;
    private int writerIndex;
    private int makeWriteIndex;
    private int makeReadIndex;

    @Override
    public int readerIndex() {
        return readerIndex;
    }

    @Override
    public void readerIndex(int readerIndex) {
        readerIndex=readerIndex;
    }

    @Override
    public int writerIndex() {
        return writerIndex;
    }

    @Override
    public void clear(){
        readerIndex=writerIndex=0;
    }
    public int writableBytes(){
        return capacity()-writerIndex;
    }
    @Override
    public void discardReadBytes() {
        if(writerIndex<readerIndex){
            throw new IllegalStateException("readIndex is less then writeIndex");
        }
        readerIndex=0;
        writerIndex=writerIndex-readerIndex;
    }
    public void ensureWritableBytes(int writableBytes){
        if (writableBytes > writableBytes()) {
            throw new IndexOutOfBoundsException();
        }

    }

}
