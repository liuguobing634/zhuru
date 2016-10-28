package lew.bing.zhuru;

import java.lang.annotation.Annotation;

/**
 * Created by 刘国兵 on 2016/10/28.
 */
public interface ID<Qualifier extends Annotation> {

    String name(Qualifier qualifier);

}
