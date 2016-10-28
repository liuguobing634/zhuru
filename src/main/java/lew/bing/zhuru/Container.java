package lew.bing.zhuru;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Qualifier;
import javax.inject.Singleton;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 刘国兵 on 2016/10/27.
 */
public class Container {

    //利用一个容器来存储类
    private Map<Class<?>,Binder<?>> mapper;
    private Map<Class<? extends Annotation>,ID> qualifiers;


    public Container() {
        mapper = new HashMap<>();
        qualifiers = new HashMap<>();
        registerQualifier(Named.class,new NamedId());
    }

    @SuppressWarnings("unchecked")
    public <T> _binder<T> registerClass(Class<T> inter) {
        Binder<T> binder;
        if (mapper.containsKey(inter)) {
            binder= (Binder<T>) mapper.get(inter);
        }else {
            binder = new Binder<>(inter);
            mapper.put(inter,binder);
        }
        return new _binder<>(binder);
    }

    public <T extends Annotation> void registerQualifier(Class<T> annotationClass,
                                                         ID<T> id) {
        qualifiers.put(annotationClass,id);
    }

    @SuppressWarnings("unchecked")
    <T extends Annotation> ID<T> findQualifier(Class<T> annotationClass) {
        return qualifiers.get(annotationClass);
    }

    <T extends Annotation> String getName(Class<T> annotationClass,T annotation) {
        if (qualifiers.containsKey(annotationClass))
            return findQualifier(annotationClass).name(annotation);
        return Binder.DEFAULT_BIND_NAME;
    }

    @SuppressWarnings("unchecked")
    public <T> T getImpl(Class<T> inter) {
        return (T) mapper.get(inter).get();
    }

    public <T> T getImplByName(Class<T> inter,String name) {
        return (T) mapper.get(inter).get(name);
    }

    public boolean hasRegistered(Class inter) {
        return mapper.containsKey(inter);
    }

    public class _binder<T> {

        private Binder<T> binder;
        private String name = Binder.DEFAULT_BIND_NAME;

        private boolean singleton;

        _binder(Binder<T> binder) {
            this.binder = binder;
        }

        private void setName(String name) {
            this.name = name;
        }

        public _binder<T> named(final String name) {
            withAnnotate(new Named(){

                @Override
                public Class<? extends Annotation> annotationType() {
                    return Named.class;
                }

                @Override
                public String value() {
                    return name;
                }
            });
            return this;
        }

        @SuppressWarnings("unchecked")
        public _binder<T> withAnnotate(Annotation annotate) {
            Class annotationType = annotate.annotationType();
            if (annotationType.isAnnotationPresent(Qualifier.class)) {
                setName(Container.this.getName(annotationType,annotate));
            }
            return this;
        }

        public _binder<T> singleton(boolean singleton) {
            this.singleton = singleton;
            return this;
        }

        public _binder<T> providedBy(Provider<T> provider) {
            if (singleton) {
                provider = new SingletonProvider<>(provider);
            }
            binder.bind(name,provider);
            return this;
        }

        public <K extends T> _binder<T> instanceOf(Class<K> impl) {
            for (Annotation annotation:impl.getAnnotations()) {
                withAnnotate(annotation);
            }
            if (impl.isAnnotationPresent(Singleton.class)) {
                this.singleton(true);
            }
            return providedBy(new ParseClass<>(binder.gettClass(),impl,Container.this));
        }

    }

}
