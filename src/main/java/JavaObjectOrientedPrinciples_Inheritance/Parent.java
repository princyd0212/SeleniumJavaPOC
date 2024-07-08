package JavaObjectOrientedPrinciples_Inheritance;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class Parent {
    public void ChildDemo(){
        System.out.println("Inheritance");
    }

    @BeforeMethod
    public void BeforeRun(){
        System.out.println("Before run the method");
    }
    @AfterMethod
    public void AfterRun(){
        System.out.println("After run the method");
    }
}
