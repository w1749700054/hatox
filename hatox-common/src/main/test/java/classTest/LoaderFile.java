package classTest;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class LoaderFile {
    public static void main(String[] args) throws IOException {

//        URL urls=LoaderFile.class.getClassLoader().getResource("META-INF/dubbo/intenel/extension.ExtensionFactory");
        ClassLoader loader=LoaderFile.class.getClassLoader();
        System.out.println(LoaderFile.class.getClassLoader().getResource("dubbo/intenel"));
//        while(urls.hasMoreElements()){
//            System.out.println("ok");
//        }
//        System.out.println("error");
    }
}
