package javassist;

public class DaoImpl  implements IDao{
    public  String data="name";
    private int n=9;

    public String getData() {
        return data;
    }

    public void addData(String name, String value) {
        int a=0;
        Object o=getN();
        System.out.println(name+"  :"+value);
    }
    public int getN(){
        return n;
    }

    public void setData(String data) {
        this.data=data;
    }

    public void setN(int n) {
        this.n=n;
    }

    public String sayHello(String name) {
        return "hello"+name;
    }

}
