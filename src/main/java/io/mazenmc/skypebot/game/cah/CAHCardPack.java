package io.mazenmc.skypebot.game.cah;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CAHCardPack {
    private List<CAHCard> cards;
    private HashMap<String, String> metadata;

    public CAHCardPack() {
        cards = new ArrayList<>();
        metadata = new HashMap<>();
    }

    public void addCard(String cardText, CAHCardType cahCardType) {
        cards.add(new CAHCard(cardText, cahCardType));
    }

    public void addMetadata(String dataName, String dataValue) {
        metadata.put(dataName, dataValue);
    }

    public List<CAHCard> cards() {
        return cards;
    }

    public void setCards(List<CAHCard> cards) {
        this.cards = cards;
    }

    public HashMap<String, String> metadata() {
        return metadata;
    }

    public void setMetadata(HashMap<String, String> metadata) {
        this.metadata = metadata;
    }
}