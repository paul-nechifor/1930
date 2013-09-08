package ro.minimul.romania1930.util;

public class Timer {
    private long value;
    private final long max;
    
    public Timer(long max) {
        this.value = (long) (Math.random() * max);
        this.max = max;
    }
    
    public boolean triggered(long add) {
        value += add;
        if (value > max) {
            value %= max;
            return true;
        }
        return false;
    }
}
