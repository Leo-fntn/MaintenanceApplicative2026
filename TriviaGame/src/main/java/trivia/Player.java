package trivia;

public class Player {
    private String name;
    private int purse;
    private int place;
    private boolean inPenaltyBox;

    public Player(String name) {
        this.name = name;
        this.purse = 0;
        // Historique: dans Game, la position initiale est fixée à 1 lors de add()
        this.place = 1;
        this.inPenaltyBox = false;
    }

    public void addCoin() {
        purse++;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public void move(int roll) {
        this.place = this.place + roll;
        if (this.place > 12) this.place = this.place - 12;
    }

    public boolean isInPenaltyBox() {
        return inPenaltyBox;
    }

    public void setInPenaltyBox(boolean inPenaltyBox) {
        this.inPenaltyBox = inPenaltyBox;
    }

    public void enterPenaltyBox() {
        this.inPenaltyBox = true;
    }

    public String getName() {
        return name;
    }

    public int getPurse() {
        return purse;
    }
}
