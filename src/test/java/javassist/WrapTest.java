package javassist;

import com.hatox.common.javassist.Wrap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WrapTest {
    @Test
    public void wrap_test(){
        Wrap wrap=Wrap.getWrap(IDao.class);
        IDao dao=new DaoImpl();

        assertEquals(wrap.getPropertyValue(dao,"data"),"name");
        assertEquals(wrap.getPropertyValue(dao,"n"),9);
        wrap.setPropertyValue(dao,"data","you name");
        assertEquals(wrap.getPropertyValue(dao,"data"),"you name");
        wrap.setPropertyValue(dao,"n",new Integer(4));
        assertEquals(wrap.getPropertyValue(dao,"n"),4);
        wrap.invoke(dao,"addData",new Class[]{String.class,String.class},new String[]{"xiaoming","xuesheng"});
        assertEquals(wrap.invoke(dao,"sayHello",new Class[]{String.class},new String[]{"wangmy"}),"hellowangmy");
        wrap.invoke(dao,"setN",new Class[]{Integer.class},new Integer[]{9});
        assertEquals(wrap.getPropertyValue(dao,"n"),9);

    }
}
