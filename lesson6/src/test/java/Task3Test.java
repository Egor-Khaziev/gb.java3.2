import Task3.Task3;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(value = Parameterized.class)
public class Task3Test {

    private static Task3 task3 = null;;

    int[] arrays;
    boolean result;

    public Task3Test(int[] arrays, boolean result){
        this.arrays = arrays;
        this.result = result;
    }

    @Parameterized.Parameters
    public static Collection parametrsList(){
        return Arrays.asList(new Object[][]{
                        {new int[]{1},false},
                        {new int[]{4},false},
                        {new int[]{1,4},true},
                        {new int[]{5},false},
                        {new int[]{1,2,3,4},true},
                        {new int[]{0,1,3,5,4,8},true},
                        {new int[]{0,2,3,5,7,8},false},
                }
        );
    }

    @Before
    public  void init(){
        task3 = new Task3();
        System.out.println("init class");

    }

    @After
    public  void destroyTest(){
        task3 = null;
        System.out.println("done");
    }

    @Test
    public void test(){
        boolean res = task3.isFourAndIsOne(arrays);
        Assert.assertEquals(result,res);
    }
}
