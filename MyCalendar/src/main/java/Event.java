
import java.time.LocalDateTime;

public abstract class Event {
    public String type;
    public String title;
    public String proprietaire;
    public LocalDateTime dateDebut;
    public int dureeMinutes;

    public Event(String title, String proprietaire, LocalDateTime dateDebut, int dureeMinutes) {
        this.title = title;
        this.proprietaire = proprietaire;
        this.dateDebut = dateDebut;
        this.dureeMinutes = dureeMinutes;
    }

    public abstract String description();

    public abstract Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin);
}