package javassist;

public class DaoImpl  implements IDao{
    public  String data="name";
    private int n=9;

    public String getData() {
        return data;
    }

    public void setData(String name, String value) {
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

}
