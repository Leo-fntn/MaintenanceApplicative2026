
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

    public Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime dt = LocalDateTime.of(dateDebut.valeur(), heureDebut.valeur());
        return !dt.isBefore(debut) && !dt.isAfter(fin);
    }

    public Boolean estEnConflit(Event autre) {
        LocalDateTime dt = LocalDateTime.of(dateDebut.valeur(), heureDebut.valeur());
        LocalDateTime autredt = LocalDateTime.of(autre.dateDebut.valeur(), autre.heureDebut.valeur());
        LocalDateTime fin1 = dt.plusMinutes(dureeMinutes.valeur());
        LocalDateTime fin2 = autredt.plusMinutes(autre.dureeMinutes.valeur());
        return dt.isBefore(fin2) && fin1.isAfter(autredt);
    }
}