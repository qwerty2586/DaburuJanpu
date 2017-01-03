package cz.zcu.qwerty2.daburujanpu.data;

import java.util.Random;

public class Level {
    public static final int STEPS_TYPE_COUNT = 16;
    public static final int SEED_LENGTH = 256;
    public static final int EMPTY_STEP = 0;
    public static final int FULL_STEP =  1;
    public static final int FULL_STEP_EVERY_X = 50;
    public static final int STEP_COLORS_COUNT = 4;
    public static final int STEPS_WIDTH = 20;
    public static final int SCREEN_WIDTH = 640;
    public static final int SCREEN_HEIGHT = 480;
    public static final int STEP_DISTANCE = 64;
    public static final int BLOCK_SIZE = 32;

    private static final int E = 0;
    private static final int L = 1;
    private static final int M = 2;
    private static final int R = 3;


    public static final int[][] STEPS = {
            { E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { M,M,M,M,M, M,M,M,M,M, M,M,M,M,M, M,M,M,M,M, },

            { L,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, R,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, },

            { E,E,E,L,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, },
            { E,E,E,L,M, M,M,M,M,M, R,E,E,E,E, E,E,E,E,E, },
            { E,E,E,E,E, E,L,M,M,M, M,M,M,R,E, E,E,E,E,E, },
            { E,E,E,E,E, E,E,E,E,L, M,M,M,M,M, M,R,E,E,E, },
            { E,E,E,E,E, E,E,E,E,E, E,L,M,M,M, M,R,E,E,E, },

            { E,E,E,E,E, E,E,E,E,E, E,L,M,M,M, M,M,M,M,R, },
            { E,E,E,E,E, E,E,E,E,E, E,E,E,E,L, M,M,M,M,R, },
            { E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,L,M,M,R, },
            { L,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,L,M,M,R, },
            { E,E,E,E,L, M,M,M,M,M, M,M,M,M,M, M,R,E,E,E, },

            { E,E,E,L,M, M,M,M,M,M, M,M,M,M,M, R,E,E,E,E, },



            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, },

    };

    public static final int[][] STEPS16 = {
            { E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { M,M,M,M,M, M,M,M,M,M, M,M,M,M,M, M,M,M,M,M, M,M,M,M,M, M,M,M,M,M, M,M,M,M,M, M,M,M,M,M, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },

            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },

            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },

            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },

            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,R,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },
            { L,M,M,M,M, M,M,M,R,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, E,E,E,E,E, },

    };



    private int[] seed;

    public static int[] generateRandomSeed() {
        int[] r = new int[SEED_LENGTH];
        Random ran = new Random();
        for (int i=0;i<SEED_LENGTH;i++) {
            r[i] = ran.nextInt(STEPS_TYPE_COUNT - 2) + 2;
        }
        return r;
    }

    public Level(int[] seed) {
        this.seed = seed;
    }

    /**
     * vraci jaky druh schodiku je v levelu
     * - pozice pod nulou jsou prazdne
     * - kazda Xta je cely schod prez celou obrazovku
     * - jinak jsou generovany schodiky ze seedu a to nazledujicim zpusobem
     * -- prvnich SEED_LENGTH  je schodnych se seedem
     * --- getStep(0) vraci seed[0]
     * --- getStep(1) vraci seed[1]
     * --- getStep(2) vraci seed[2]
     * -- dalsich SEED_LENGTH se vraci vzdy obkrcmo az dojede na konec a zacne opakovat s offsetem
     * --- getStep(SEED_LENGTH+0) vraci seed[0]
     * --- getStep(SEED_LENGTH+1) vraci seed[2]
     * --- getStep(SEED_LENGTH+SEED_LENGTH/2+0) vraci seed[1]
     * --- getStep(SEED_LENGTH+SEED_LENGTH/2+0) vraci seed[3]
     * -- a tak podobne aby se kazdych SEED_LENGTH vyuzil cely seed
     *
     * @param step cislo schodiku
     * @return polozka ze seed
     */
    public int getStep(int step) {
        if (step<0) return EMPTY_STEP;
        if (step%FULL_STEP_EVERY_X == 0) return FULL_STEP;
        int offset = ( step / SEED_LENGTH ) % SEED_LENGTH ;
        int index = step % SEED_LENGTH;
        int x = ((index * (offset + 1))) %SEED_LENGTH ;
        int y = ((index * (offset + 1)) / SEED_LENGTH);
        int z = (x - y + SEED_LENGTH) % SEED_LENGTH;
        return seed[z];
    }

    /**
     * vraci barvu schodiku, generuje se ze seedu
     *
     * @param step krok
     * @return polozka ze seed
     */
    public int getStepColor(int step) {
        if (step<0) return 0; // osetreni zapornejch
        int part = step/FULL_STEP_EVERY_X;
        int color = (seed[part%SEED_LENGTH])%STEP_COLORS_COUNT;
        return color;
    }

}
