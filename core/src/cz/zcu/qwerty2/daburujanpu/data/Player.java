package cz.zcu.qwerty2.daburujanpu.data;

public class Player {
    public int id = -1;
    public String name = "Unknown";
    public int color = -1;
    public boolean ready = false;

    public Player() {
    }

    public Player(int id, String name, int color, boolean ready) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.ready = ready;
    }


}
