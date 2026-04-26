import java.time.LocalDateTime;

public class Reunion extends Event {
    public LieuReunion lieu; // utilisé seulement pour REUNION
    public ParticipantsReunion participants; // séparés par virgules (pour REUNION uniquement)

    public Reunion(EventID id, TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes, LieuReunion lieu, ParticipantsReunion participants) {
        super(id, title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.lieu = lieu;
        this.participants = participants;
    }

    public Reunion(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes, LieuReunion lieu, ParticipantsReunion participants) {
        super(title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.lieu = lieu;
        this.participants = participants;
    }

    public String description() {
        return "Réunion : " + title.valeur() + " à " + lieu.valeur() + " avec " + participants.valeur();
    }
}
