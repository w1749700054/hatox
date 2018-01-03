package classTest.spi.example1;


import com.hatox.common.spi.SPI;

@SPI("impl1")
public interface Ext1 {
    public String sayHello(String name);
}
