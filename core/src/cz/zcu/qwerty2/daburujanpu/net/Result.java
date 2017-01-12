package cz.zcu.qwerty2.daburujanpu.net;

public class Result implements Comparable<Result> {
    public int id;
    public String name;
    public int score;
    public int maxStep;
    public boolean left;

    @Override
    public int compareTo(Result o) {
        return Integer.valueOf(score).compareTo(o.score);
    }

}
