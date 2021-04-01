package TASK1;

import static java.lang.Thread.sleep;

public class Task1 {

    private static final Object monitor = new Object();
    ;

    static String value = "A";


    public static void main(String[] args) {


        Task1 t1 = new Task1();
        Task1 t2 = new Task1();
        Task1 t3 = new Task1();
        t1.thread("A");
        t2.thread("B");
        t3.thread("C");


    }

    private static void thread(String a) {

        Thread thread = new Thread(() -> {
            logic(a);
        });
        thread.start();
    }

    private static void logic(String a) {
        synchronized (monitor) {

            for (int i = 0; i < 5; i++) {
                if (value.equals(a)) {
                    System.out.print(a);

                    /** split */
                    if (value.equals("C")){
                        System.out.print(" ");
                    }

                    if (value.equals("A")) {
                        value = "B";
                    } else if (value.equals("B")) {
                        value = "C";
                    } else if (value.equals("C")) {
                        value = "A";
                    }


                } else {
                    i--;
                }

                /** sleep block */
                try {
                    monitor.notifyAll();
                    monitor.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }


            }
        }
    }
}



