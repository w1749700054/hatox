package com.hatox.remoting.buffer;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.min;

public class CompositeBuffer extends AbstractChannelBuffer{
    private static int DEFAULTCOMPONENTNUM=1<<4;
    private List<Component> components;
    private int maxComponentNum;
    private int nowComponentIndex;
    private int nowComponentWriterIndex;
    public CompositeBuffer(ChannelBuffer... buffers){
        this(buffers,DEFAULTCOMPONENTNUM);
    }
    public CompositeBuffer(ChannelBuffer[] buffers, int max){
        if(buffers==null){
            throw new NullPointerException("buffer is not null");
        }
        components=new LinkedList<Component>();
        int i=0;
        for(ChannelBuffer buffer:buffers){
            Component component=new Component(buffer);
            component.offset=i;
            component.endOffset=(i+=buffer.capacity());
            i++;
        }
        nowComponentIndex=0;
        maxComponentNum=max;
    }
    public CompositeBuffer addComponent(ChannelBuffer buffer){
        addComponent(buffer,components.size());
        return this;
    }
    public CompositeBuffer removeComponentNow(){
        return removeComponent(nowComponentIndex);
    }
    public CompositeBuffer removeComponent(int index){
        if(index<0||index>=components.size()){
            throw new IndexOutOfBoundsException("componentNum is out of");
        }

        Component removeComponent=components.remove(index);
        int removeCapacity=removeComponent.buffer.capacity();
        for(int i=index;i<components.size()-1;i++){
            Component c=components.get(i);
            c.offset=c.offset-removeCapacity;
            c.endOffset=c.endOffset-removeCapacity;
        }
        return this;
    }
    private void addComponent(ChannelBuffer buffer, int index) {
        if(index>maxComponentNum){
            throw new IndexOutOfBoundsException("out of max of components size");
        }
        if(index==components.size()){
            if(index==0){
                Component c=new Component(buffer);
                components.add(c);
                c.offset=0;
                c.endOffset=buffer.capacity();
            }else{
                Component c=new Component(buffer);
                Component prev=components.get(components.size()-1);
                c.offset=prev.endOffset+1;
                c.endOffset=c.offset+buffer.capacity();
            }
        }else{
            if(index==0){
                Component c=new Component(buffer);
                components.add(index,c);
                c.offset=0;
                c.endOffset=buffer.capacity();
                updateOffset(index,buffer.capacity());
            }else{
                Component c=new Component(buffer);
                Component prev=components.get(index-1);
                c.offset=prev.endOffset+1;
                c.endOffset=c.offset+buffer.capacity();
                updateOffset(index,buffer.capacity());
            }
        }
    }

    private void updateOffset(int index,int offset) {
        for(int i=index+1;i<components.size();i++){
            Component c=components.get(i);
            c.offset=c.offset+offset;
            c.endOffset=c.endOffset+offset;
        }
    }

    public int capacity() {
        Component c=components.get(components.size());
        return c.endOffset;
    }

    public ChannelBuffer copy(int index, int length) {
        return null;
    }

    public ChannelBufferFactory factory() {
        return null;
    }

    public byte getByte(int index) {
        if(components.size()==0){
            throw new IllegalStateException("buffer is not found");
        }
        toComponentIndex(index);
        Component c=components.get(nowComponentIndex);
        return c.buffer.getByte(index-c.endOffset);
    }
    private int toComponentIndex(int index){
        return toComponentIndex(index,false);
    }
    private int toComponentIndex(int index,boolean writeIndex){
        if(components.get(nowComponentIndex).offset<=index||index<components.get(nowComponentIndex).endOffset){
            return nowComponentIndex;
        }
        int start=0;
        int end=components.size()-1;
//        int end=components.get(components.size()-1).endOffset;
        while(start<end){
            int mid=(end-start)/2+start;
            if(index<components.get(mid).offset){
                end=mid;
            }else if(index>components.get(mid).endOffset){
                start=mid;
            }else {
                if(writeIndex) {
                    nowComponentWriterIndex=mid;
                }else{
                    nowComponentIndex=mid;
                }
                return mid;
            }
        }
        throw new IndexOutOfBoundsException("index out of bound");
    }

    public void getBytes(int index, byte[] dst, int dstIndex, int length) {
        if(components.size()==0){
            throw new IllegalStateException("buffer is not found");
        }
        int startIndex=toComponentIndex(index);
        Component c=components.get(nowComponentIndex);
        if(c.endOffset>index+length){
            c.buffer.getBytes(index-c.offset,dst,dstIndex,length);
        }else{
            int endIndex=toComponentIndex(index+length);
            for(int i=startIndex;i<endIndex;i++){
                Component component=components.get(i);
                int slength=min(component.endOffset-index,length-dstIndex);
                component.buffer.getBytes(index-component.offset,dst,dstIndex,slength);
                index=component.endOffset+1;
                dstIndex=slength+dstIndex;
            }
        }

    }

    public void getBytes(int index, ChannelBuffer buffer, int length) {
        if(components.size()==0){
            throw new IllegalStateException("buffer is not found");
        }
        int startIndex=toComponentIndex(index);
        Component c=components.get(nowComponentIndex);
        if(c.endOffset>index+length){
            c.buffer.getBytes(index-c.offset,buffer,length);
        }else{
            int endIndex=toComponentIndex(index+length);
            for(int i=startIndex;i<endIndex;i++){
                Component component=components.get(i);
                component.buffer.getBytes(index-component.offset,buffer,min(component.endOffset-index,length));
                index=component.endOffset+1;
                length=length-(component.endOffset-index);
            }
        }
    }

    public void setByte(int index, byte b) {
        if(components.size()==0){
            throw new IllegalStateException("buffer is not found");
        }
        toComponentIndex(index,true);
        Component c=components.get(nowComponentWriterIndex);
        c.buffer.setByte(b);
    }

    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        if(components.size()==0){
            throw new IllegalStateException("buffer is not found");
        }
        int startIndex=toComponentIndex(index,true);
        Component c=components.get(nowComponentWriterIndex);
        if(c.endOffset>index+length){
            c.buffer.setBytes(index-c.offset,src,srcIndex,length);
        }else{
            int endIndex=toComponentIndex(index+length);
            for(int i=startIndex;i<endIndex;i++){
                Component component=components.get(i);
                int slength=min(component.endOffset-index,length-srcIndex);
                component.buffer.setBytes(index-component.offset,src,srcIndex,slength);
                index=component.endOffset+1;
                srcIndex=slength+srcIndex;
            }
        }
    }

    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
        if(components.size()==0){
            throw new IllegalStateException("buffer is not found");
        }
        int startIndex=toComponentIndex(index,true);
        Component c=components.get(nowComponentWriterIndex);
        if(c.endOffset>index+length){
            c.buffer.getBytes(index-c.offset,src,length);
        }else{
            int endIndex=toComponentIndex(index+length);
            for(int i=startIndex;i<endIndex;i++){
                Component component=components.get(i);
                component.buffer.getBytes(index-component.offset,src,min(component.endOffset-index,length));
                index=component.endOffset+1;
                length=length-(component.endOffset-index);
            }
        }
    }

    public byte[] array() {
        if(components.size()==0){
            return new byte[0];
        }
        int endOffset=components.get(components.size()-1).endOffset;
        byte[] bytes=new byte[endOffset];
        getBytes(0,bytes,0,bytes.length);
        return bytes;
    }

    public int compareTo(ChannelBuffer o) {
        return 0;
    }


    class Component{
        ChannelBuffer buffer;
        int offset;
        int endOffset;
        int length;
        Component(ChannelBuffer buffer){
            this.buffer=buffer;
            length=buffer.capacity();
        }
    }
}
