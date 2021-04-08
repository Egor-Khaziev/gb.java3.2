import org.junit.*;
import Task2.*;

public class Task2Test {

    private static Task2 task2;

    int[] a = new int[]{1,2,3,4,5,6,7};
    int[] b = new int[]{1,4,3,4,5,6,7};
    int[] c = new int[]{1,24,34,4,54,64,74};
    int[] d = new int[]{1,2,3,0,5,6,4};

    @BeforeClass
    public static void initTest(){
        task2 = new Task2();
        System.out.println("init class");
    }

    @AfterClass
    public static void destroyTest(){
        task2 = null;
        System.out.println("done");
    }



    @Test
    public void test(){
        int[] result = task2.lastFour(a);
        Assert.assertArrayEquals(new int[]{5,6,7}, result);
    }
    @Test
    public void test2(){
        int[] result = task2.lastFour(b);
        Assert.assertArrayEquals(new int[]{5,6,7}, result);
    }
    @Test
    public void test3(){
        int[] result = task2.lastFour(c);
        Assert.assertArrayEquals(new int[]{54,64,74}, result);
    }
    @Test
    public void test4(){
        int[] result = task2.lastFour(d);
        Assert.assertArrayEquals(null, result);
    }

}