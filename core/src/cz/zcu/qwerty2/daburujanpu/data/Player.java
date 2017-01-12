package cz.zcu.qwerty2.daburujanpu.data;


import java.util.Arrays;

import cz.zcu.qwerty2.daburujanpu.net.Command;

public class Player {
    public static final int SPRITE_SIZE = 32;
    public int id = -1;
    public String name = "Unknown";
    public int color = 0;
    public boolean ready = false;


    public int frame = 0;
    public float x, y;

    public boolean inputup = false, inputl = false, inputp = false;

    public boolean falling = false, jumping = false, dead = false;
    public float speedx = 0, speedy = 0;
    public int maxstep = 0;

    private static int SPEED_LIMIT = 10;
    private static float SPEED_STEP = 1;
    private static float SLOW_STEP = .35f;
    private static float BOUNCE_MULTIPLIER = 1.5f;
    private static float R_EDGE = Level.SCREEN_WIDTH - Player.SPRITE_SIZE;
    private static float MIN_JUMP = 10;


    public Player() {
    }

    public Player(int id, String name, int color, boolean ready) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.ready = ready;
    }

    public String serialize() {
        String[] str_arr = {
                "" + id, name,
                "" + color,
                ready ? "1" : "0",
                "" + frame,
                "" + x,
                "" + y,
                inputup ? "1" : "0",
                inputl ? "1" : "0",
                inputp ? "1" : "0",
                falling ? "1" : "0",
                jumping ? "1" : "0",
                dead ? "1" : "0",
                "" + speedx,
                "" + speedy,
                "" + maxstep};
        return String.join("" + Command.DEFAULT_DELIMITER, Arrays.asList(str_arr));
    }

    public void deserialize(String s) {
        String[] str_arr = s.split("" + Command.DEFAULT_DELIMITER);
    }


    public int fromArgs(Command c, int i) {
        id = c.argInt(i++);
        name = c.args.get(i++);
        color = c.argInt(i++);
        ready = c.argInt(i++)==1;
        frame = c.argInt(i++);

        x = c.argFloat(i++);
        y = c.argFloat(i++);
        inputup = c.argInt(i++)==1;
        inputl = c.argInt(i++)==1;
        inputp = c.argInt(i++)==1;

        falling = c.argInt(i++)==1;
        jumping = c.argInt(i++)==1;
        dead = c.argInt(i++)==1;
        speedx = c.argFloat(i++);
        speedy = c.argFloat(i++);

        maxstep = c.argInt(i++);
        return i;
    }

    public void nextFrame(Level level) {
        if (dead) {
            frame++;
            return;
        }

        // HORIZONTALNI FYZIKA

        if (speedx < SPEED_LIMIT && inputp) speedx += SPEED_STEP;
        if (speedx > -SPEED_LIMIT && inputl) speedx -= SPEED_STEP;

        if (!(inputl || inputp)) speedx = speedx - Math.signum(speedx) * SLOW_STEP; // brzdeni

        if (-1 < speedx && speedx < 1) speedx = .0f; // dobrzdeni jinak se budde zpomalovat navzdy

        x += speedx;
        if (x < 0) {
            x = -x;
            speedx *= -BOUNCE_MULTIPLIER; // kdyz narazi o rantl tak se odrazi vetsi rychlosti
        }
        if (x > R_EDGE) {
            x = R_EDGE - (x - R_EDGE);
            speedx *= -BOUNCE_MULTIPLIER;
        }

        if (speedx < -SPEED_LIMIT * 2) speedx = -SPEED_LIMIT * 2; // tohle je maximalni rychlost
        if (speedx > SPEED_LIMIT * 2) speedx = SPEED_LIMIT * 2;

        // VERTIKALNI FYZIKA

        //nejdriv ceknem jesli jsme neprejeli prez okraj

        if (!jumping && !falling) {
            int teststep = (int) y / Level.STEP_DISTANCE;
            if (Level.STEPS[level.getStep(teststep)][(int) (x + SPRITE_SIZE / 2) / Level.BLOCK_SIZE] == 0) {
                falling = true; // prepadli sme
            }
        }


        if (!jumping && !falling && inputup) {
            speedy = Math.max(Math.abs(speedx), MIN_JUMP);
            jumping = true;
        }

        if (speedy < 0) falling = true;

        if (jumping && !falling) {
            if (inputup) {
                speedy -= SLOW_STEP;
            } else {
                speedy -= SLOW_STEP * 2; // kdyz nedrzime skakaci cudlik tak mame mensi skok, padame rychlejc
            }
        }

        if (falling) {
            speedy -= SLOW_STEP * 2; //volny pad
        }

        // KOLIZNI FYZIKA


        if (falling) {
            int teststep = (int) y / Level.STEP_DISTANCE; // zaokrouhnime na nejblizsi schod
            while (teststep * Level.STEP_DISTANCE > y + speedy) {
                if (Level.STEPS[level.getStep(teststep)][(int) (x + SPRITE_SIZE / 2) / Level.BLOCK_SIZE] > 0) {
                    speedy = 0;
                    jumping = false;
                    falling = false;
                    y = teststep * Level.STEP_DISTANCE;
                    maxstep = Math.max(teststep, maxstep); // updatneme skore
                    break;
                }

                teststep--;
            }
        }


        if (falling || jumping) y += speedy;


        frame++;


    }


}
