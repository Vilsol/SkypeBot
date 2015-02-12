package io.mazenmc.skypebot.game.uno;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import io.mazenmc.skypebot.utils.Resource;

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
        switch(color) {
            case WILD:
                try {
                    if (face == Face.WD4) {
                        Map.Entry<String, Deck> next = game.peek();

                        next.getValue().draw(4, game);
                        Resource.sendMessage(Skype.getUser(next.getKey()).getDisplayName() + " drew 4 cards!");
                    }

                    Resource.sendMessage("What color would you like to choose next, " + owner.getDisplayName() + "?");
                    Resource.sendMessage("Available colors: RED, BLUE, GREEN, YELLOW");
                    Resource.assignCallback(owner.getId(), (message) -> {
                        Color c = Color.valueOf(message.toUpperCase());

                        if(c == Color.WILD) {
                            Resource.sendMessage("That's an invalid card!");
                            play(game, owner);
                            return;
                        }

                        Face f = Face.randomFace(color);

                        game.next();
                        game.addCard(new Card(c, f));
                    });
                } catch (SkypeException ignored) {}

                break;

            default:
                if (face == Face.D2) {
                    game.next();

                    Map.Entry<String, Deck> current = game.current();
                    User user;

                    try {
                        user = Skype.getUser(current.getKey());

                        Resource.sendMessage(user.getDisplayName() + " drew 2 cards!");
                        current.getValue().draw(2, game);
                    } catch (SkypeException ignored) {}
                }

                if (face == Face.REVERSE) {
                    game.reverse();
                    game.next();
                }

                if(face != Face.D2 && face != Face.REVERSE)
                    game.next();

                game.addCard(this); // set current card to the top
        }
    }

    @Override
    public String toString() {
        return ((color == Color.WILD) ? "" : (color.name() + " ")) + face.name();
    }
}
