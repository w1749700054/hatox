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

    public void writerIndex(int writerIndex){
        this.writerIndex=writerIndex;
    }
    @Override
    public void clear(){
        readerIndex=writerIndex=0;
    }
    public int writableBytes(){
        return capacity()-writerIndex;
    }

    @Override
    public ChannelBuffer copy() {
        return copy(readerIndex,readableBytes());
    }

    public byte readByte(){
        return getByte(readerIndex++);
    }
    public void readBytes(byte[] dst, int dstIndex, int length){
        getBytes(readerIndex,dst,dstIndex,length);
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
    public  void getBytes(int index,byte[] dst){
        getBytes(index,dst,0,dst.length);
    }
    public void getBytes(int index,ChannelBuffer buffer){
        getBytes(index,buffer,buffer.writableBytes());
    }
    public void markReaderIndex(){
        readerIndex=makeReadIndex;
    }
    public void markWriterIndex(){
        readerIndex=makeWriteIndex;
    }
    public boolean readable(){
        return writerIndex>readerIndex;
    }
    public int readableBytes(){
        return writerIndex-readerIndex;
    }
    public void readBytes(byte[] dst){
        readBytes(dst,0,dst.length);
    }
    public void setByte(byte b){
        setByte(writerIndex++,b);
    }
    public void  setBytes(byte[] src){
        setBytes(writerIndex,src);
        writerIndex=writerIndex+src.length;
    }
    public void setBytes(int index,byte[] src){
        setBytes(index,src,0,src.length);
    }
    public void setBytes(int index, ChannelBuffer src){
        setBytes(index,src,src.readableBytes());
    }
    public void setBytes(int index,ChannelBuffer src,int length){
        if(length>readableBytes()){
            throw new IndexOutOfBoundsException();
        }
        setBytes(index,src,0,length);
        src.readerIndex(src.readerIndex()+length);
    }
    public void setIndex(int readerIndex,int writerIndex){
        this.readerIndex=readerIndex;
        this.writerIndex=writerIndex;
    }

}
