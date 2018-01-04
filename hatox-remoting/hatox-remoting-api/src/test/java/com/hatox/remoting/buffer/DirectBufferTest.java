package com.hatox.remoting.buffer;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DirectBufferTest {
    DirectChannelBuffer dcb=new DirectChannelBuffer(1024);
    @Test
    public  void capaticy_test(){
        assertEquals(dcb.capacity(),1024);
    }
    @Test
    public void setByte_test(){
        dcb.setByte(5, (byte) 124);
        assertEquals(dcb.getByte(5),(byte)124);
    }
    @Test
    public void setBytes_test(){
        {
            byte[] bytes = "hello world".getBytes();
            dcb.setBytes(2, bytes, 0, bytes.length);
            byte[] bs = new byte[bytes.length];
            dcb.getBytes(2, bs, 0, bytes.length);
            assertEquals(new String(bs), "hello world");
        }
        {
            DirectChannelBuffer buffer=new DirectChannelBuffer("hello world".length());
            buffer.setBytes("hello world".getBytes());
            dcb.setBytes(2,buffer,0,buffer.capacity());
            byte[] bs=new byte["hello world".length()];
            dcb.getBytes(2,bs,0,"hello world".length());
            assertEquals(new String(bs),"hello world");
            DirectChannelBuffer buffer2=new DirectChannelBuffer("hello world".length());
            byte[] bs1=new byte["hello world".length()];
            dcb.getBytes(2,buffer2,buffer2.capacity());
            buffer2.getBytes(0,bs1,0,bs1.length);
            assertEquals(new String(bs1),"hello world");
        }

    }

    @Test
    public void copy_test(){
        DirectChannelBuffer dcb=new DirectChannelBuffer(1024);
    }
}
