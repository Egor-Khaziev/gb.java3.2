package Server;




import static java.lang.Thread.sleep;

public class ServerApp {

    public static void main(String[] args) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Client1.Client.main(args);
            }
        }).start();

        new MyServer();



    }

}
