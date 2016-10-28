package lew.bing.zhuru;

import lew.bing.zhuru.exceptions.NoConstructorsError;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

/**
 * Created by 刘国兵 on 2016/10/27.
 */
class ParseClass<T,K extends T> implements Provider<T> {

    private Class<T> tClass;
    private Class<K> implClass;

    private Container container;

    ParseClass(Class<T> tClass,Class<K> implClass,Container container) {
        this.tClass = tClass;
        this.container = container;
        this.implClass = implClass;
    }

    @SuppressWarnings("unchecked")
    private   T newInstance()
            throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (!container.hasRegistered(tClass)) {
            return null;
        }
        Class<K> implement = implClass;
        Constructor[] constructors = implement.getConstructors();
        if (constructors.length == 0){
            //没有构造器
            throw new NoConstructorsError();
        }
        T instance = null;
        for (Constructor<K> constructor:constructors) {
            if (constructor.getParameterCount() == 0) {
                instance = constructor.newInstance();
                break;
            }
            if (constructor.isAnnotationPresent(Inject.class)) {
                //获得此构造函数的参数
                Object[] params = new Object[constructor.getParameterCount()];
                int j = 0;

                for (Parameter p: constructor.getParameters()){


                    params[j++] = getValue(p,p.getType());
                }
                instance = constructor.newInstance(params);
                break;
            }
        }
        if (instance != null) {
            initFields(instance,implement);
            initMethod(instance,implement);
            return instance;
        }
        return null;
    }

    private void initFields(T instance, Class<K> kClass)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for (Field field : kClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Inject.class)) {
                Object value = getValue(field,field.getType());
                field.setAccessible(true);
                field.set(instance,value);
                field.setAccessible(false);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initMethod(T instance, Class<K> kClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        for (Method method : kClass.getMethods()) {
            if (method.isAnnotationPresent(Inject.class)) {
                Object[] params = new Object[method.getParameterCount()];
                int j = 0;
                for (Parameter parameter: method.getParameters()) {
                    params[j++] =  getValue(parameter,parameter.getType());
                }
                method.invoke(instance,params);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Object getValue(AnnotatedElement accessibleObject,Class tClass) {
        for (Annotation annotation:accessibleObject.getAnnotations()){
            Class annotationType = annotation.annotationType();
            if (annotationType.isAnnotationPresent(Qualifier.class)) {
                String name = container.getName(annotationType,annotation);
                return container.getImplByName(tClass,name);
            }
        }
        return container.getImpl(tClass);
    }

    @Override
    public T get() {
        try {
            return newInstance();
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
            throw new RuntimeException("无法获取该类实例");
        }
    }
}
