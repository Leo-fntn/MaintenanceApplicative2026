import java.time.LocalDateTime;

public class Periodique extends Event {
    public int frequenceJours; // uniquement pour PERIODIQUE

    public Periodique(String title, String proprietaire, LocalDateTime dateDebut, int dureeMinutes, int frequenceJours) {
        super(title, proprietaire, dateDebut, dureeMinutes);
        this.type = "PERIODIQUE";
        this.frequenceJours = frequenceJours;
    }
}
