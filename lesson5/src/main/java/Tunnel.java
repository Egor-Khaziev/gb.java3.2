import java.util.concurrent.Semaphore;

public class Tunnel extends Stage {

    private static final Semaphore SEMAPHORE = new Semaphore(MainClass.CARS_COUNT/2, false);
    private static final boolean[] freePlaces = new boolean[MainClass.CARS_COUNT / 2];



    public Tunnel() {
        this.length = 80;
        this.description = "Тоннель " + length + " метров";
    }

    @Override
    public void go(Car c) {
        try {
            try {

                System.out.println(c.getName() + " готовится к этапу(ждет): " + description);
                SEMAPHORE.acquire();
                synchronized (freePlaces){
                    for(int i = 0 ; i< MainClass.CARS_COUNT / 2; i++){
                        if(!freePlaces[i]){
                            freePlaces[i] = true;
                            c.setTunnelPlaceNumber(i);
                            break;
                        }
                    }
                }

                System.out.println(c.getName() + " начал этап: " + description);
                Thread.sleep(length / c.getSpeed() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println(c.getName() + " закончил этап: " + description);
            }

                synchronized (freePlaces){
                freePlaces[c.getTunnelPlaceNumber()] = false;
                }
                SEMAPHORE.release();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
