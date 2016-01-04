package io.mazenmc.skypebot.game.cah;

import com.skype.ChatMessage;
import com.skype.Skype;
import com.skype.SkypeException;
import com.skype.User;
import io.mazenmc.skypebot.engine.bot.Command;
import io.mazenmc.skypebot.engine.bot.Module;
import io.mazenmc.skypebot.engine.bot.ModuleManager;
import io.mazenmc.skypebot.game.BaseGame;
import io.mazenmc.skypebot.game.GameLang;
import io.mazenmc.skypebot.game.GameManager;
import io.mazenmc.skypebot.game.GameState;
import io.mazenmc.skypebot.utils.Resource;
import io.mazenmc.skypebot.utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

// special thanks to @stuntguy3000 for writing the original crappy code for this
// PS. GET A LICENSE ON YOUR PROJECTS, FOOL.
public class CardsAgainstHumanity extends BaseGame implements Module {
    private HashMap<String, CAHCardPack> allCardPacks = new HashMap<>();
    private List<CAHCard> blackCards = new ArrayList<>();
    private String cardCzar;
    private List<CAHCardPack> chosenDecks = new ArrayList<>();
    private boolean continueGame = true;
    private CAHCard currentBlackCard;
    private boolean czarChoosing = false;
    private List<CzarOption> czarOptions = new ArrayList<>();
    private LinkedHashMap<String, Boolean> extrasPacks = new LinkedHashMap<>();
    private HashMap<String, LinkedList<CAHCard>> playedCards = new HashMap<>();
    private int playerOrderIndex = 0;
    private int round = 1;
    public int secondsSincePlay = 0;
    private HashMap<String, List<CAHCard>> userCards = new HashMap<>();
    private List<CAHCard> whiteCards = new ArrayList<>();
    private boolean countingDown = false;
    int attempts = 0;
    CAHJoinTask task;

    // Init Class
    public CardsAgainstHumanity() {
        setState(GameState.WAITING_FOR_PLAYERS);
        setMinPlayers(3);
        loadPacks();

        for (int i = 1; i < 6; i++) {
            extrasPacks.put("Extra " + i, false);
        }

        ModuleManager.registerModule(CardsAgainstHumanity.class);
    }

    @Override
    public String description() {
        return "Cards Against Humanity.\n" +
                "Please select your card pack by doing @pack <number>\n" +
                "You may add extras by doing @extra <number>\n" +
                "Once you've selected your card pack, you can do @cahstart to start the join timer\n" +
                "To join CAH, just do @join\n" +
                "To leave, do @leave";
    }

    @Override
    public String name() {
        return "CAH";
    }

    @Override
    public void end() {
        ModuleManager.removeModule(CardsAgainstHumanity.class);
    }

    public boolean checkPlayers(boolean forcePlay) {
        if (minPlayers() > activePlayers().size()) {
            sendToAll(GameLang.ERROR_NOT_ENOUGH_PLAYERS);
            return false;
        }

        if (state() == GameState.CHOOSING || forcePlay) {
            int toPlay = activePlayers().size() - 1; // exclude czar
            if (playedCards.size() == toPlay || forcePlay) {
                if (!forcePlay) {
                    for (Map.Entry<String, LinkedList<CAHCard>> cardPlay : playedCards.entrySet()) {
                        if (cardPlay.getValue().size() < currentBlackCard.blanks()) {
                            return false;
                        }
                    }
                }

                setState(GameState.INGAME);

                String[] blackCardSplit = currentBlackCard.getRawText().split("_");

                for (Map.Entry<String, LinkedList<CAHCard>> playerCards : playedCards.entrySet()) {
                    int segmentID = 0;
                    int cardCount = 0;
                    CzarOption czarOption = new CzarOption(Skype.getUser(playerCards.getKey()));
                    StringBuilder modifiedBlackCard = new StringBuilder();
                    modifiedBlackCard.append(blackCardSplit[segmentID]);

                    for (CAHCard playerCard : playerCards.getValue()) {
                        segmentID++;
                        cardCount++;
                        modifiedBlackCard.append("*");
                        modifiedBlackCard.append(playerCard.getText());
                        modifiedBlackCard.append("*");
                        if (segmentID < blackCardSplit.length) {
                            modifiedBlackCard.append(blackCardSplit[segmentID]);
                        }
                    }

                    if (cardCount != currentBlackCard.blanks()) {
                        continue;
                    }

                    modifiedBlackCard.append("\n");
                    czarOption.setText(modifiedBlackCard.toString());
                    czarOptions.add(czarOption);
                }

                StringBuilder options = new StringBuilder();

                Collections.shuffle(czarOptions);

                int index = 1;
                for (CzarOption czarOption : czarOptions) {
                    czarOption.setOptionNumber(index);
                    options.append(czarOption.text());
                    index++;
                }

                if (index == 1) {
                    sendToAll(GameLang.GAME_CAH_NOPLAYERS);
                    new CAHRoundDelay(this);

                    return true;
                }

                sendToAll(GameLang.GAME_CAH_ALLPLAYED);
                sendToAll("\n" + options.toString());
                send(cardCzar, createCzarKeyboard());
                czarChoosing = true;
            }
        }
        return true;
    }

    private String createCzarKeyboard() {
        StringBuilder keyboard = new StringBuilder();

        keyboard.append(currentBlackCard.getText()).append("\n");

        for (CzarOption czarOption : czarOptions) {
            keyboard.append(czarOption.optionNumber())
                    .append(". ")
                    .append(czarOption.text())
                    .append("\n");
        }

        keyboard.append("You may select your card by using @select <number> in the group chat");

        return keyboard.toString();
    }

    private String createUserKeyboard(String user) {
        List<CAHCard> cards = userCards.get(user);
        StringBuilder keyboard = new StringBuilder();

        keyboard.append(currentBlackCard.getText()).append("\n");

        int index = 1;

        for (CAHCard card : cards) {
            keyboard.append(index)
                    .append(". ")
                    .append(card.getText())
                    .append("\n");
            index++;
        }

        keyboard.append("You may select your card by using @select <number> in the group chat");

        return keyboard.toString();
    }

    private void fillHands() {
        for (String user : activePlayers()) {
            giveWhiteCard(user, 10);
        }
    }

    @Override
    public void onSecond() {
        secondsSincePlay++;

        if (!czarChoosing) {
            if (secondsSincePlay == 60) {
                sendToAll(GameLang.GAME_CAH_TIMEWARNING);
            } else if (secondsSincePlay == 80) {
                sendToAll(GameLang.GAME_CAH_TIMENOTICE);
                checkPlayers(true);
            }
        } else {
            secondsSincePlay = 0;
        }
    }

    @Override
    public void removePlayer(String username) {
        super.removePlayer(username);

        if (username.equals(cardCzar) && checkPlayers(false)) {
            nextRound();
        }

        if (state() == GameState.INGAME && activePlayers().size() < 3) {
            sendToAll("There are no longer enough players to continue CAH! Ending game...");
            GameManager.instance().stopGame();
        }
    }

    @Override
    public void startGame() {
        if (chosenDecks.isEmpty()) {
            chosenDecks.add(allCardPacks.get("cah.x1.cards")); // if no decks were explicitly chosen, choose the first one
        }

        setState(GameState.INGAME);
        StringBuilder stringBuilder = new StringBuilder();

        for (CAHCardPack pack : chosenDecks) {
            for (CAHCard card : pack.cards()) {
                if (card.cahCardType() == CAHCardType.WHITE) {
                    whiteCards.add(card);
                } else if (card.cahCardType() == CAHCardType.BLACK) {
                    blackCards.add(card);
                }
            }

            stringBuilder.append(pack.metadata().get("name")).append(", ");
        }

        sendToAll(String.format(GameLang.GAME_CAH_ACTIVECARDS, stringBuilder.toString().substring(0, stringBuilder.length() - 2)));

        Collections.shuffle(whiteCards);
        Collections.shuffle(blackCards);

        fillHands();
        nextRound();
    }

    private void giveWhiteCard(String user, int amount) {
        List<CAHCard> playerCardDeck = userCards.get(user);

        if (playerCardDeck == null) {
            playerCardDeck = new ArrayList<>();
        }

        for (int i = 0; i < amount; i++) {
            playerCardDeck.add(whiteCards.remove(0));
        }

        userCards.put(user, playerCardDeck);
    }

    private boolean isPlaying(String user) {
        return activePlayers().contains(user);
    }

    // Load Card Packs from Jar resources
    public void loadPacks() {
        List<String> knownPacks = new ArrayList<>();

        for (int i = 1; i < 4; i++) {
            knownPacks.add("cah.v" + i + ".cards");
        }

        for (int i = 1; i < 6; i++) {
            knownPacks.add("cah.x" + i + ".cards");
        }

        for (String name : knownPacks) {
            InputStream is = getClass().getResourceAsStream("/" + name);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            CAHCardPack cahCardPack = new CAHCardPack();
            String packLine;
            CAHPackProperty cahPackProperty = null;

            try {
                while ((packLine = reader.readLine()) != null) {
                    switch (packLine) {
                        case "___METADATA___": {
                            cahPackProperty = CAHPackProperty.METADATA;
                            continue;
                        }
                        case "___BLACK___": {
                            cahPackProperty = CAHPackProperty.BLACKCARDS;
                            continue;
                        }
                        case "___WHITE___": {
                            cahPackProperty = CAHPackProperty.WHITECARDS;
                            continue;
                        }
                        default: {
                            if (cahPackProperty != null) {
                                switch (cahPackProperty) {
                                    case METADATA: {
                                        if (packLine.contains(": ")) {
                                            String[] packData = packLine.split(": ");
                                            cahCardPack.addMetadata(packData[0], packData[1]);
                                        }
                                        continue;
                                    }
                                    case BLACKCARDS: {
                                        cahCardPack.addCard(packLine.replaceAll("~", "\n"), CAHCardType.BLACK);
                                        continue;
                                    }
                                    case WHITECARDS: {
                                        cahCardPack.addCard(packLine.replaceAll("~", "\n"), CAHCardType.WHITE);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            allCardPacks.put(name, cahCardPack);
        }
    }

    // Play the next round
    public void nextRound() {
        secondsSincePlay = 0;
        if (!continueGame) {
            GameManager.instance().stopGame();
        } else {
            for (String user : activePlayers()) {
                int size = userCards.get(user).size();
                giveWhiteCard(user, 10 - size);
            }

            playerOrderIndex++;

            if (playerOrderIndex >= activePlayers().size()) {
                playerOrderIndex = 0;
            }

            czarOptions.clear();
            playedCards.clear();
            czarChoosing = false;
            blackCards.add(currentBlackCard);
            currentBlackCard = blackCards.remove(0);

            cardCzar = activePlayers().get(playerOrderIndex);
            sendToAll(String.format(GameLang.GAME_CAH_STARTROUND_CZAR, round, cardCzar));

            setState(GameState.CHOOSING);

            sendToAll(currentBlackCard.getText());

            activePlayers().stream().filter((s) -> !cardCzar.equals(s)).forEach((user) -> send(user, createUserKeyboard(user)));

            if (currentBlackCard.blanks() > 1) {
                sendToAll(String.format(GameLang.GAME_CAH_WHITECARDS, currentBlackCard.blanks()));
            }

            round++;
        }
    }

    private boolean playCard(String sender, String message) {
        if (czarChoosing) {
            if (sender.equals(cardCzar)) {
                try {
                    int number = Integer.parseInt(message.split(" ")[0]);
                    if (!tryCzar(number)) {
                        sendToAll(GameLang.ERROR_INVALID_SELECTION);
                    }
                } catch (Exception ignored) {
                }
                return true;
            } else {
                System.out.println("not the czar");
                return false;
            }
        }

        if (state() != GameState.CHOOSING) {
            System.out.println("not choosing time");
            return false;
        }

        if (sender.equals(cardCzar)) {
            return false;
        }

        int number;
        List<CAHCard> userCards = this.userCards.get(sender);

        try {
            number = Integer.parseInt(message.split(" ")[0]);
        } catch (NumberFormatException e) {
            return false;
        }

        if (number < 0 || userCards == null || number > userCards.size()) {
            return false;
        }

        CAHCard cahCard = userCards.get(number - 1);
        LinkedList<CAHCard> cards = new LinkedList<>();

        if (playedCards.containsKey(sender)) {
            cards = playedCards.get(sender);
        }

        int cardsNeeded = currentBlackCard.blanks();

        if (cards.size() < cardsNeeded) {
            for (CAHCard userCard : cards) {
                if (userCard.getText().equals(cahCard.getText())) {
                    send(sender, GameLang.ERROR_ALREADY_PLAYED_CARD);
                    return true;
                }
            }

            cards.add(cahCard);
            playedCards.put(sender, cards);

            // Remove from players hand
            new ArrayList<>(userCards).forEach((userCard) -> {
                if (userCard.getText().equals(cahCard.getText())) {
                    userCards.remove(userCard);
                }
            });

            if (cards.size() == cardsNeeded) {
                sendToAll(String.format(GameLang.GAME_CAH_USERPLAY, sender));
                checkPlayers(false);
            } else {
                send(sender, createUserKeyboard(sender));
                send(sender, String.format(GameLang.GAME_CAH_PLAY_MORE, cardsNeeded - cards.size()));
            }
        } else {
            send(sender, GameLang.GAME_GENERAL_NOT_TURN);
        }
        return true;
    }

    boolean tryCzar(int number) {
        if (czarChoosing) {
            User winner = null;
            for (CzarOption czarOption : czarOptions) {
                if (czarOption.optionNumber() == number) {
                    winner = czarOption.owner();
                    czarChoosing = false;
                    break;
                }
            }

            if (winner != null) {
                sendToAll(String.format(GameLang.GAME_CAH_WIN_ROUND, String.valueOf(number), winner.getId()));
                incrementScore(winner.getId());

                String gameWinner = null;
                for (String user : activePlayers()) {
                    if (scoreFor(user) >= 10) {
                        gameWinner = user;
                    }
                }

                if (gameWinner != null) {
                    continueGame = false;
                    GameManager.instance().stopGame();
                } else {
                    new CAHRoundDelay(this);
                }
                return true;
            }
        }

        return false;
    }

    CAHCardPack base() {
        return Utils.optionalGet(allCardPacks.entrySet().stream()
                .filter((e) -> chosenDecks.contains(e.getValue()) && e.getKey().contains("v"))
                .map(Map.Entry::getValue)
                .findFirst());
    }

    /*                                             Commands Section                                               */

    public static CardsAgainstHumanity current() {
        return (CardsAgainstHumanity) GameManager.instance().current();
    }

    @Command(name = "pack")
    public static void pack(ChatMessage message, Integer number) {
        CardsAgainstHumanity cah = current();
        CAHCardPack pack = cah.allCardPacks.get("cah.v" + number + ".cards");
        CAHCardPack base = cah.base();

        if (cah.countingDown) {
            Resource.sendMessage(message, "No modifications may be made after countdown started!");
            return;
        }

        if (pack == null) {
            Resource.sendMessage(message, "That base pack does not exist!");
            return;
        }

        if (base != null) {
            cah.chosenDecks.remove(base); // remove old base pack
            return;
        }

        cah.chosenDecks.add(pack);
        Resource.sendMessage(message, "That base pack has been selected!");
    }

    @Command(name = "extra")
    public static void extra(ChatMessage message, Integer number) {
        CardsAgainstHumanity cah = current();
        CAHCardPack pack = cah.allCardPacks.get("cah.x" + number + ".cards");

        if (cah.countingDown) {
            Resource.sendMessage(message, "No modifications may be made after countdown started!");
            return;
        }

        if (pack == null) {
            Resource.sendMessage(message, "That extra pack does not exist!");
            return;
        }

        if (cah.chosenDecks.contains(pack)) {
            Resource.sendMessage(message, "CAH already contains this extra pack!");
            return;
        }

        cah.chosenDecks.add(pack);
        Resource.sendMessage(message, "That extra pack has been added!");
    }

    @Command(name = "cahstart")
    public static void start(ChatMessage message) {
        CardsAgainstHumanity cah = current();
        cah.countingDown = true;
        new CAHJoinTask(cah);
        Resource.sendMessage(message, "Timer has started!");
    }

    @Command(name = "join")
    public static void join(ChatMessage message) throws SkypeException {
        String senderId = message.getSenderId();
        CardsAgainstHumanity cah = current();

        if (cah.isPlaying(senderId)) {
            Resource.sendMessage(message, "You are already in the game!");
            return;
        }

        cah.addPlayer(message.getSender());
        Resource.sendMessage(message, "You have joined the current CAH game!");
    }

    @Command(name = "leave")
    public static void leave(ChatMessage message) throws SkypeException {
        String senderId = message.getSenderId();
        CardsAgainstHumanity cah = current();

        if (!cah.isPlaying(senderId)) {
            Resource.sendMessage(message, "You are not in the game! Yay!");
            return;
        }

        cah.removePlayer(senderId);
        Resource.sendMessage(message, "You have successfully left the game");
    }

    @Command(name = "select")
    public static void select(ChatMessage message, Integer card) throws SkypeException {
        String senderId = message.getSenderId();
        CardsAgainstHumanity cah = current();

        if (!cah.isPlaying(senderId)) {
            Resource.sendMessage(message, "You are not in the game! Too bad, too sad!");
            return;
        }

        if (!cah.playCard(senderId, String.valueOf(card))) {
            Resource.sendMessage(message, "Yeah.. about playing that card, I wasn't really able to do that.");
        }
    }

    @Command(name = "forcestart", admin = true)
    public static void forceStart(ChatMessage message) throws SkypeException {
        CardsAgainstHumanity cah = current();

        if (cah.activePlayers().size() < 3) {
            Resource.sendMessage(message, "There are not enough players! You idiot!");
            return;
        }

        if (cah.task != null) {
            cah.task.cancel();
            cah.task = null;
        }

        cah.sendToAll("Let the games begin!");
        cah.startGame();
    }

    @Command(name = "cahlist")
    public static void cahList(ChatMessage message) {
        StringBuilder builder = new StringBuilder();

        for (String s : current().activePlayers()) {
            builder.append(s).append(", ");
        }

        Resource.sendMessage(message, builder.toString());
    }
}

class CzarOption {
    private int optionNumber;
    private User owner;
    private String text;

    CzarOption(User owner) {
        this.owner = owner;
    }

    public int optionNumber() {
        return optionNumber;
    }

    public void setOptionNumber(int optionNumber) {
        this.optionNumber = optionNumber;
    }

    public User owner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String text() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

class CAHRoundDelay extends TimerTask {

    private CardsAgainstHumanity cardsAgainstHumanity;

    public CAHRoundDelay(CardsAgainstHumanity cardsAgainstHumanity) {
        this.cardsAgainstHumanity = cardsAgainstHumanity;
        this.cardsAgainstHumanity.secondsSincePlay = 0;
        new Timer().schedule(this, 60000);
    }

    @Override
    public void run() {
        if (cardsAgainstHumanity.state() == GameState.INGAME) {
            cardsAgainstHumanity.nextRound();
        }
    }
}

class CAHJoinTask extends TimerTask {
    private CardsAgainstHumanity cardsAgainstHumanity;

    public CAHJoinTask(CardsAgainstHumanity cah) {
        this.cardsAgainstHumanity = cah;
        new Timer().schedule(this, 60000);
        cardsAgainstHumanity.task = this;
    }

    @Override
    public void run() {
        if (cardsAgainstHumanity.activePlayers().size() <= 3) {
            if (cardsAgainstHumanity.attempts < 3) {
                cardsAgainstHumanity.attempts++;
                cardsAgainstHumanity.sendToAll("Not enough players to start CAH, trying again.\nAttempt: " + cardsAgainstHumanity.attempts +
                        "\nResending description...");
                cardsAgainstHumanity.sendToAll(cardsAgainstHumanity.description());
                new CAHJoinTask(cardsAgainstHumanity);
            } else {
                cardsAgainstHumanity.sendToAll("Attempt 3 has been reached! Cancelling CAH.");
                GameManager.instance().stopGame();
            }
            return;
        }

        cardsAgainstHumanity.sendToAll("Let the games begin!");
        cardsAgainstHumanity.startGame();
        cardsAgainstHumanity.task = null;
    }
}