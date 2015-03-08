package io.mazenmc.skypebot.game.uno;

import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import io.mazenmc.skypebot.game.Game;
import io.mazenmc.skypebot.utils.Resource;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UnoGame implements Game {

    private boolean finished;
    private Map<String, Deck> decks = new LinkedHashMap<>(); // TODO change to queue
    private int playerPosition = 0;
    private Card topCard;

    public UnoGame() {
        topCard = randomCard();
    }

    public Deck deckFor(User user) {
        return decks.get(user.getId());
    }

    public int position() {
        return playerPosition;
    }

    public void next() {
        if (playerPosition >= (decks.size() - 1)) {
            playerPosition = 0;
            return;
        }

        playerPosition++;

        for (Map.Entry<String, Deck> entry : decks.entrySet()) {
            try {
                User user = Skype.getUser(entry.getKey());

                user.send("Your deck:");
                user.send(entry.getValue().toString());
            } catch (SkypeException ignored) {
            }
        }

        // TODO more messages
    }

    public Map.Entry<String, Deck> current() {
        return new LinkedList<>(decks.entrySet()).get(playerPosition);
    }

    public Map.Entry<String, Deck> peek() {
        return new LinkedList<>(decks.entrySet()).get((playerPosition >= (decks.size() - 1)) ? 0 : playerPosition + 1);
    }

    public void reverse() {
        List<Map.Entry<String, Deck>> old = new LinkedList<>(decks.entrySet());

        decks = new LinkedHashMap<>();

        for (int i = old.size() - 1; i >= 0; i--) {
            Map.Entry<String, Deck> entry = old.get(i);

            decks.put(entry.getKey(), entry.getValue());
        }
    }

    public Card topCard() {
        return topCard;
    }

    public Card addCard(Card card) {
        Card old = topCard;

        topCard = card;
        Resource.sendMessage("Card in play " + card.toString());

        return old;
    }

    public Card randomCard() {
        Color c = Color.randomColor();

        return new Card(c, Face.randomFace(c));
    }
}
