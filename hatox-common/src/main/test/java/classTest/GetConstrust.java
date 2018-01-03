package classTest;

public class GetConstrust {
    public static void main(String[] args) throws NoSuchMethodException {
        UserDao.class.getConstructor(IDao.class);
    }
}
