package cz.zcu.qwerty2.daburujanpu.data;

import com.badlogic.gdx.graphics.Color;

public class ColorPalette {

    //arne, ext palette from https://androidarts.com/palette/16pal.htm
    private static String hex_colors[] = {
            "#000000",
            "#9D9D9D",
            "#FFFFFF",
            "#BE2633",
            "#E06F8B",
            "#493C2B",
            "#A46422",
            "#EB8931",

            "#F7E26B",
            "#2F484E",
            "#44891A",
            "#A3CE27",
            "#1B2632",
            "#005784",
            "#31A2F2",
            "#B2DCEF",

            "#342A97",
            "#656D71",
            "#CCCCCC",
            "#732930",
            "#CB43A7",
            "#524F40",
            "#AD9D33",
            "#EC4700",

            "#FAB40B",
            "#115E33",
            "#14807E",
            "#15C2A5",
            "#225AF6",
            "#9964F9",
            "#F78ED6",
            "#F1B660"

    };
    public static Color colors[];

    static {
        colors = new Color[hex_colors.length];
        for ( int i=0;i<hex_colors.length;i++) {
            colors[i] = Color.valueOf(hex_colors[i]);
        }
    }
}
