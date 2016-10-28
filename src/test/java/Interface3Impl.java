import java.time.LocalDateTime;

/**
 * Created by 刘国兵 on 2016/10/27.
 */
public class Interface3Impl implements Interface3 {

    public Interface3Impl() {
        System.out.println("init int3");
    }

    @Override
    public void logNow() {
        System.out.println(LocalDateTime.now());
    }
}
