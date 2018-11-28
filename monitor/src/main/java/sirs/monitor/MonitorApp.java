package sirs.monitor;

public class MonitorApp {
    public static void main(String[] args) {
        Monitor monitor = new Monitor();

        // every x seconds, ping every server ( appservers, webserver, etc..)
        while(monitor.areServersUp()){
            try{
                System.out.println("Monitor: Servers are up.");
                Thread.sleep(1000);
            } catch(InterruptedException e){
                System.exit(-1);
            }
        }
    }
}
