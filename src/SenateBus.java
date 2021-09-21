import java.util.concurrent.Semaphore;


public class SenateBus {

    public static int riders = 0;
    public static Semaphore mutex = new Semaphore(1);
    public static Semaphore multiplex = new Semaphore(5);
    public static Semaphore bus = new Semaphore(0);
    public static Semaphore allBoard = new Semaphore(0);


    public static void main(String[] args) {
        Rider rider1 = new Rider();
        Rider rider2 = new Rider();
        Rider rider3 = new Rider();
        Rider rider4 = new Rider();
        Rider rider5 = new Rider();
        Rider rider6 = new Rider();
        Rider rider7 = new Rider();
        Bus bus1 =  new Bus();
        Bus bus2 =  new Bus();

        rider1.start();
        rider2.start();
        rider3.start();
        bus1.start();
        rider4.start();
        rider5.start();
        bus2.start();
        rider6.start();
        rider7.start();

    }

    static class Rider extends Thread{

        @Override
        public void run() {
            try {
                multiplex.acquire();
                mutex.acquire();
                riders++;
                mutex.release();

                System.out.println("Rider "+ this.getId()+" is waiting");
                bus.acquire();
                multiplex.release();

                System.out.println("Rider "+ this.getId()+" is boarding");

                riders--;

                if (riders==0){
                    allBoard.release();
                } else {
                    bus.release();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static class Bus extends Thread{

        @Override
        public void run() {
            try {
                mutex.acquire();
                if (riders>0) {
                    System.out.println("Bus "+this.getId()+" arrived");
                    bus.release();
                    allBoard.acquire();
                }
                mutex.release();
                System.out.println("Bus "+this.getId()+" is leaving");

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

