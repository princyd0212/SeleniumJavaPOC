package JavaObjectOrientedPrinciples_Inheritance;

import org.testng.annotations.Test;

public class Child extends Parent{

    @Test
    public void TestRun(){
        int a = 4;
        Child_2 Child_2 = new Child_2(a);
        ChildDemo();
        System.out.println(Child_2.increment());
        System.out.println(Child_2.decrement());


        System.out.println(Child_2.multiplyThree());
    }
}
