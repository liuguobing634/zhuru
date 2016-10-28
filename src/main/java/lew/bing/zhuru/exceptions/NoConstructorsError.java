package lew.bing.zhuru.exceptions;

/**
 * Created by 刘国兵 on 2016/10/27.
 */
public class NoConstructorsError extends RuntimeException {

    public NoConstructorsError() {
        super("没有声明公开的构造器");
    }
}
