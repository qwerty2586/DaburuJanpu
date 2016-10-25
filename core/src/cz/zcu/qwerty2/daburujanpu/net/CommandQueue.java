package cz.zcu.qwerty2.daburujanpu.net;

public class CommandQueue extends java.util.concurrent.ConcurrentLinkedQueue<NetCommand> {

    @Override
    public boolean add(NetCommand netCommand) {
        boolean r =  super.add(netCommand);
        synchronized (this) {
            this.notify();
        }
        return r;
    }
}
