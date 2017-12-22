package javassist;

import com.hatox.common.javassist.Proxy;
import org.junit.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class ProxyTest {
    @Test
    public void proxy_test(){
        Proxy proxy=Proxy.getProxy(IDao.class,IService.class);
        IDao instance= (IDao) proxy.newInstance(new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if("getData".equals(method.getName())){
                    assertEquals(args.length,0);
                }else if("setData".equals(method.getName())){
                    assertEquals(args[0],"name");
                    assertEquals(args[1],"value");
                }
                return null;
            }

        });
        IService service= (IService) proxy.newInstance(new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                if("getName".equals(method.getName())){
                    assertEquals(args.length,0);
                }else if("addName".equals(method.getName())){
                    assertEquals(args[0],"ming");
                }
                return null;
            }

        });
        instance.getData();
        instance.setData("name","value");
        service.getName();
        service.addName("ming");
    }
}
