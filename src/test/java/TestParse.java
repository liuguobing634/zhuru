import lew.bing.zhuru.Container;
import org.junit.Test;

import javax.inject.Provider;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by 刘国兵 on 2016/10/27.
 */

public class TestParse {

    @Test
    public void test1() throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Container container = new Container();
        container.registerClass(Interface1.class).instanceOf(Interface1Impl.class);
        container.registerClass(Interface2.class).instanceOf(Interface2Impl.class);
        container.registerClass(Interface3.class).instanceOf(Interface3Impl.class);
        container.registerClass(String.class).named("name").providedBy(() -> "刘国兵");
        container.getImpl(Interface2.class).hello();
    }

}

interface Interface1 {
    void say(String welcome);
}

interface Interface2 {
    void hello();
}




