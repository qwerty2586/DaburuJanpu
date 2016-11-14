package cz.zcu.qwerty2.daburujanpu.net;

public class CommandQueue extends java.util.concurrent.ConcurrentLinkedQueue<Command> {

    @Override
    public boolean add(Command command) {
        boolean r =  super.add(command);
        synchronized (this) {
            this.notify();
        }
        return r;
    }
}
