package io.mazenmc.skypebot.game.uno;

import com.samczsun.skype4j.user.User;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;

import java.util.Map;

public class Card {
    private final Color color;
    private final Face face;

    public Card(Color color, Face face) {
        this.color = color;
        this.face = face;
    }

    public Color color() {
        return color;
    }

    public Face face() {
        return face;
    }

    public void play(UnoGame game, User owner) {
        switch (color) {
            case WILD:
                try {
                    if (face == Face.WILD_DRAW_FOUR) {
                        Map.Entry<String, Deck> next = game.peek();

                        next.getValue().draw(4, game);
                        Resource.sendMessage(Utils.getDisplayName(Utils.getUser(next.getKey())) + " drew 4 cards!");
                    }

                    Resource.sendMessage("What color would you like to choose next, " + Utils.getDisplayName(owner) + "?");
                    Resource.sendMessage("Available colors: RED, BLUE, GREEN, YELLOW");
                    Resource.assignCallback(owner.getUsername(), (message) -> {
                        Color c = Color.valueOf(message.toUpperCase());

                        if (c == Color.WILD) {
                            Resource.sendMessage("That's an invalid card!");
                            play(game, owner);
                            return;
                        }

                        Face f = Face.randomFace(color);

                        game.next();
                        game.addCard(new Card(c, f));
                    });
                } catch (Exception ignored) {
                }

                break;

            default:
                if (face == Face.DRAW_TWO) {
                    game.next();

                    Map.Entry<String, Deck> current = game.current();
                    User user;

                    try {
                        user = Utils.getUser(current.getKey());

                        Resource.sendMessage(Utils.getDisplayName(user) + " drew 2 cards!");
                        current.getValue().draw(2, game);
                    } catch (Exception ignored) {
                    }
                }

                if (face == Face.REVERSE) {
                    game.reverse();
                    game.next();
                }

                if (face != Face.DRAW_TWO && face != Face.REVERSE)
                    game.next();

                game.addCard(this); // set current card to the top
        }
    }

    @Override
    public String toString() {
        return ((color == Color.WILD) ? "" : (color.name() + " ")) + face.name();
    }
}
