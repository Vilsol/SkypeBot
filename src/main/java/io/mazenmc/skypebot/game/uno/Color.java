package io.mazenmc.skypebot.game.uno;

import java.util.concurrent.ThreadLocalRandom;

public enum Color {
    RED,
    BLUE,
    GREEN,
    YELLOW,
    WILD;

    private int chance;

    Color() {
        chance = 20;
    }

    public static Color randomColor() {
        int rand = ThreadLocalRandom.current().nextInt(101);
        Color color = Color.RED;

        for (Color c : values()) {
            if(rand <= c.chance) {
                color = c;
                rand = c.chance;
            }
        }

        return color;
    }

    public int chance() {
        return chance;
    }

    public void setChance(int chance) {
        this.chance = chance;
        int total = 0;

        for (Color c : values()) {
            if(c != this)
                total += c.chance;
        }

        int diff = total + chance - 100;
        int toApply = diff / 4;

        for (Color c : values()) {
            if(c != this)
                c.chance += toApply;
        }
    }
}
