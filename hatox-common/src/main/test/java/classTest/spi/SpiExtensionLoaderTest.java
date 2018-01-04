package classTest.spi;

import classTest.spi.example1.Ext1;
import classTest.spi.example1.Ext1Impl1;
import com.hatox.common.spi.ExtensionLoader;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class SpiExtensionLoaderTest {
    @Test
    public void spiExt1_test() throws InstantiationException, IllegalAccessException {
        Ext1 ext1= ExtensionLoader.getExtensionLoader(Ext1.class).getDefaultExtension();
        assertThat(ext1, instanceOf(Ext1Impl1.class));
    }
    public  void spiExt1_null_test(){

    }
}
