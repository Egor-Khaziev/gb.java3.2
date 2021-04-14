import Interface.AfterSuite;
import Interface.BeforeSuite;
import Interface.Command;
import Interface.Test;

public class TestingClass {

    @Command(args = "empty", priority = 9 )
    @BeforeSuite
    public  void start(String args){
        System.out.print(args);
        System.out.println(" BEFORE test");
    }

    @Command(args = "empty", priority = 1 )
    @Test
    public  void test1(String args){
        System.out.print(args);
        System.out.println(" is running");
    }

    @Command(args = "empty", priority = 3 )
    @Test
    public  void test2(String args){
        System.out.print(args);
        System.out.println(" is running");
    }

    @Command(args = "empty", priority = 2 )
    @Test
    public  void test3(String args){
        System.out.print(args);
        System.out.println(" is running");
    }

    @Command(args = "empty", priority = 9 )
    @AfterSuite(i = 1)
    public  void end(String args){
        System.out.print(args);
        System.out.println(" AFTER test");
    }

}
