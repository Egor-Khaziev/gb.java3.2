public class Car implements Runnable {
    private static int CARS_COUNT;

    private static Object ready = new Object();
    private static int readyCount;
    private static boolean roadIsStart = false;

    private static int finishedCars;

    public int getTunnelPlaceNumber() {
        return tunnelPlaceNumber;
    }

    public void setTunnelPlaceNumber(int tunnelPlaceNumber) {
        this.tunnelPlaceNumber = tunnelPlaceNumber;
    }

    private int tunnelPlaceNumber = 0;

    static {
        readyCount = 0;
        CARS_COUNT = 0;
        finishedCars = 0;
    }

    private Race race;
    private int speed;
    private String name;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public Car(Race race, int speed) {
        this.race = race;
        this.speed = speed;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }

    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int) (Math.random() * 800));

            this.waitToStart();

            System.out.println(this.name + " готов");
        } catch (Exception e) {
            e.printStackTrace();
        }
            roadIsStart = true;
        try {
            this.waitToStart();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        if (finishedCars == 0) {
            System.out.println(name + " WIN");
        }
        if (MainClass.CARS_COUNT == ++finishedCars){
            MainClass.roadWait(true);
        }
    }

    private void waitToStart() throws InterruptedException {
        synchronized (ready) {
            if (MainClass.CARS_COUNT == ++readyCount) {
                if (roadIsStart){
                    MainClass.roadWait(roadIsStart);
                    ready.wait(50);
                }
                ready.notifyAll();
                readyCount=0;
            } else {
                ready.wait();
            }
        }
    }
}
