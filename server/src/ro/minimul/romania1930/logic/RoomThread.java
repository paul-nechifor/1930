package ro.minimul.romania1930.logic;

class RoomThread extends Thread {
    private final Room room;
    private volatile boolean keepRunning = false;

    public RoomThread(Room room) {
        this.room = room;
    }
    
    @Override 
    public void run() {
        keepRunning = true;
        
        while (keepRunning) {
            try {
                Thread.sleep(50);
                room.tick();
            } catch (InterruptedException ex) {
            }
        }
    }

    void stopRunning() {
        keepRunning = false;
        interrupt();
    }
}