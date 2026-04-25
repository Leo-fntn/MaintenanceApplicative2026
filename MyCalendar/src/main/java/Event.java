
import java.time.LocalDateTime;

public abstract class Event {
    public TitreEvenement title;
    public ProprietaireEvenement proprietaire;
    public DateEvenement dateDebut;
    public HeureDebut heureDebut;
    public DureeEvenement dureeMinutes;

    public Event(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes) {
        this.title = title;
        this.proprietaire = proprietaire;
        this.dateDebut = dateDebut;
        this.heureDebut = heureDebut;
        this.dureeMinutes = dureeMinutes;
    }

    public abstract String description();

    public abstract Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin);

    public abstract Boolean estEnConflit(Event autre);
}