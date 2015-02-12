package io.mazenmc.skypebot.game.uno;

import java.util.ArrayList;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<>();

    public Deck() {}

    public Card card(int index) {
        return cards.get(index);
    }

    public void removeCard(int index) {
        cards.remove(index);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public void draw(int amount, UnoGame game) {
        for (int i = 0; i < amount; i++)
            addCard(game.randomCard());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        cards.forEach((c) -> builder.append(c.toString()).append(' '));

        return builder.toString();
    }
}
