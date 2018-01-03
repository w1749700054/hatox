package classTest.spi.example1;

public class Ext1Impl1 implements Ext1 {
    public String sayHello(String name) {
        return "hello"+Ext1Impl1.class.getSimpleName();
    }
}
