import Interface.AfterSuite;
import Interface.BeforeSuite;
import Interface.Command;
import Interface.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.LinkedList;
import java.util.List;

public class MainTEST {

    private static List<Method> commands;

    private static TestingClass testingClass = new TestingClass();



    public static void main(String[] args) {
        start(testingClass);
    }


    public static void start(TestingClass testClass) {

        commands = new LinkedList<>();

        Method[] methodArr = testClass.getClass().getDeclaredMethods();
//        for (Method m : testClass.getClass().getDeclaredMethods()){
//            if (m.isAnnotationPresent(Command.class)){
//            Command com = m.getAnnotation(Command.class);
//                System.out.println(com.priority());
//            }}

        try {
            getTestMethods(methodArr);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        startMethods();

    }



    private static void startMethods() {



        for (Method m: commands ) {
            try {
                m.setAccessible(true);
//                if (m.getParameterTypes().length ==0){
                    String args =  m.getName();

                m.invoke(testingClass, args);
//            }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

    }

    private static void getTestMethods(Method[] methodArr) throws ClassNotFoundException {
        oneMethod(methodArr, BeforeSuite.class, false);
        oneMethod(methodArr, Test.class, true);
        oneMethod(methodArr, AfterSuite.class, false);
    }

    public static void oneMethod(Method[] methodArr, Class classCompare, boolean manyMethods ) throws ClassNotFoundException {

        int SuiteCount = 0;
        for (Method m : methodArr) {
            if (m.isAnnotationPresent(classCompare)) {
                SuiteCount++;
                if (SuiteCount > 1 && manyMethods==false) {
                    throw new RuntimeException("to much \"BeforeSuite\" or \"AfterSuite\"");
                }
                commands.add(m);
            }
        }
        if (manyMethods && commands.size()>3) {
            sortCommands();
        }
    }

    private static void sortCommands() {
        boolean sort =  false;
        Method temp = null;
        while(!sort)
            sort = true;

        for (int i = 1; i<commands.size()-1;i++){
            sort = false;


                if (commands.get(i).getAnnotation(Command.class).priority() > commands.get(i+1).getAnnotation(Command.class).priority() ) {
                    temp = commands.get(i);
                    commands.set(i,commands.get(i+1));
                    commands.set(i+1,temp);
                }
            }
    }

}
