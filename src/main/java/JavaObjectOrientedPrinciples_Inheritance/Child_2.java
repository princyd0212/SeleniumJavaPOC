package JavaObjectOrientedPrinciples_Inheritance;

public class Child_2 extends Child_3{

    int a;

    public Child_2(int a) {
        super(a);
        this.a=a;
    }

    public int increment(){
        a = a +1;
        return a;
    }
    public int decrement(){
        a= a-1;
        return a;
    }
}
