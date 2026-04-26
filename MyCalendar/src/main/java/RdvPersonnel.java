import java.time.LocalDateTime;

public class RdvPersonnel extends Event{
    public RdvPersonnel(EventId id, TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes) {
        super(id, title, proprietaire, dateDebut, heureDebut, dureeMinutes);
    }

    public RdvPersonnel(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes) {
        super(title, proprietaire, dateDebut, heureDebut, dureeMinutes);
    }

    public String description() {
        return "RDV : " + title.valeur() + " le " + dateDebut.valeur().toString() + " à " + heureDebut.valeur().toString();
    }
}
