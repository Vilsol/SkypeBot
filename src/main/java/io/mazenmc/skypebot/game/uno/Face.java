package io.mazenmc.skypebot.game.uno;

import java.util.concurrent.ThreadLocalRandom;

public enum Face {
    ZERO,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE,
    REVERSE,
    D2,
    WILD,
    WD4;

    public static Face randomFace(Color color) {
        switch (color) {
            case RED:
            case BLUE:
            case GREEN:
            case YELLOW:
                return values()[ThreadLocalRandom.current().nextInt(-1, 12)];

            case WILD:
                return values()[ThreadLocalRandom.current().nextInt(11, 14)];
        }

        return Face.ZERO;
    }
}
