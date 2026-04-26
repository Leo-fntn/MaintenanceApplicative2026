
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class Event {
    public EventID id;
    public TitreEvenement title;
    public ProprietaireEvenement proprietaire;
    public DateEvenement dateDebut;
    public HeureDebut heureDebut;
    public DureeEvenement dureeMinutes;

    public Event(EventID id, TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes) {
        this.id = id;
        this.title = title;
        this.proprietaire = proprietaire;
        this.dateDebut = dateDebut;
        this.heureDebut = heureDebut;
        this.dureeMinutes = dureeMinutes;
    }

    public Event(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes) {
        this(new EventID(UUID.randomUUID().toString()), title, proprietaire, dateDebut, heureDebut, dureeMinutes);
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