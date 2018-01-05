package com.hatox.remoting.buffer;

import java.nio.ByteBuffer;
import java.util.List;

public class CompositeBuffer {
    private int readerIndex;
    private int writerIndex;
    private List<Component> components;
    class Component{
        ChannelBuffer buffer;
        int offset;
        int endOffset;
        int length;
        Component(ChannelBuffer buffer){
            this.buffer=buffer;
            length=buffer.readableBytes();
        }
    }
}
