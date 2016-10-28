package lew.bing.zhuru;

import javax.inject.Provider;

/**
 * Created by 刘国兵 on 2016/10/28.
 */
public class SingletonProvider<T> implements Provider<T> {

    private Provider<T> originProvider;
    private boolean get = false;
    private T result = null;

    public SingletonProvider(Provider<T> originProvider) {
        this.originProvider = originProvider;
    }

    @Override
    public T get() {
        if (!get && result == null) {
            get = true;
            result = originProvider.get();
        }
        return result;
    }
}
