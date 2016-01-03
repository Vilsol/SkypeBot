package io.mazenmc.skypebot.game.cah;


class CAHCard {
    private int blanks = 0;
    private CAHCardType cahCardType;
    private String text;

    public CAHCard(String text, CAHCardType cahCardType) {
        this.text = text;
        this.cahCardType = cahCardType;

        for (char character : text.toCharArray()) {
            if (character == '_') {
                ++blanks;
            }
        }
    }

    public String getRawText() {
        return text;
    }

    public String getText() {
        return text.replace("_", "______");
    }

    public int blanks() {
        return blanks;
    }

    public void setBlanks(int blanks) {
        this.blanks = blanks;
    }

    public CAHCardType cahCardType() {
        return cahCardType;
    }

    public void setCahCardType(CAHCardType cahCardType) {
        this.cahCardType = cahCardType;
    }

    public void setText(String text) {
        this.text = text;
    }
}