package lew.bing.zhuru;

import javax.inject.Named;

/**
 * Created by 刘国兵 on 2016/10/28.
 */
public class NamedId implements ID<Named> {
    @Override
    public String name(Named named) {
        return "NAMED_"+named.value();
    }
}
