import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by 刘国兵 on 2016/10/27.
 */
public class Interface2Impl implements Interface2{

    private Interface1 interface1;

    @Inject
    private Interface3 interface3;

    private String name;

    public String getName() {
        return name;
    }

    @Inject
    public void setName(@Named("name") String name) {
        this.name = name;
    }

    @Inject
    public Interface2Impl(Interface1 interface1) {
        this.interface1 = interface1;
    }

    @Override
    public void hello() {
        interface3.logNow();
        System.out.println(name);
        interface1.say("hello");
        interface3.logNow();
    }
}
