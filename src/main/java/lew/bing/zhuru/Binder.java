package lew.bing.zhuru;

import javax.inject.Provider;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘国兵 on 2016/10/27.
 */
class Binder<T> {

    static String DEFAULT_BIND_NAME = "DEFAULT_BIND_NAME";

    private Class<T> tClass;

    private Map<String,Provider<T>> providers;

    Binder(Class<T> tClass) {
        this.tClass = tClass;
        providers = new HashMap<>();
    }

    Binder<T> bind(String name, Provider<T> provider) {
        providers.put(name,provider);
        return this;
    }



    public Binder<T> bind(Provider<T> provider) {
        return bind(DEFAULT_BIND_NAME,provider);
    }

    T get(String name) {
        if (providers.containsKey(name)) {
            return providers.get(name).get();
        }
        throw new RuntimeException("尚未绑定");
    }

    T get() {
        return get(DEFAULT_BIND_NAME);
    }


    public Class<T> gettClass() {
        return tClass;
    }
}
