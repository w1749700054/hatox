package javassist;

import java.util.List;
import java.util.Map;

public interface IDao {
//    private String data="name";
//    String name=null;
    public String getData();
    public void addData(String name,String value);
    public int getN();
    public void setData(String data);
    public void setN(int n);
    public String sayHello(String name);
//    public int addInt(int n);

}
