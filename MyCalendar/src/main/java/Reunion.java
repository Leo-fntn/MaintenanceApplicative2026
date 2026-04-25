import java.time.LocalDateTime;

public class Reunion extends Event {
    public String lieu; // utilisé seulement pour REUNION
    public String participants; // séparés par virgules (pour REUNION uniquement)

    public Reunion(String title, String proprietaire, LocalDateTime dateDebut, int dureeMinutes, String lieu, String participants) {
        super(title, proprietaire, dateDebut, dureeMinutes);
        this.type = "REUNION";
        this.lieu = lieu;
        this.participants = participants;
    }
}
