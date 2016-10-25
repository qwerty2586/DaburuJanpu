package cz.zcu.qwerty2.daburujanpu.net;



public class NetCommand {
    public static final int CONNECT_TO_SERVER = 0;
    public int command;
    public Object attachement;

    public NetCommand(int command, Object attachement) {
        this.command = command;
        this.attachement = attachement;
    }
}
