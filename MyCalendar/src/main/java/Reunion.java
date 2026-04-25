import java.time.LocalDateTime;

public class Reunion extends Event {
    public String lieu; // utilisé seulement pour REUNION
    public String participants; // séparés par virgules (pour REUNION uniquement)

    public Reunion(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, int dureeMinutes, String lieu, String participants) {
        super(title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.lieu = lieu;
        this.participants = participants;
    }

    public String description() {
        return "Réunion : " + title.valeur() + " à " + lieu + " avec " + participants;
    }

    public Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime dt = LocalDateTime.of(dateDebut.valeur(), heureDebut.valeur());
        return !dt.isBefore(debut) && !dt.isAfter(fin);
    }

    public Boolean estEnConflit(Event autre) {
        LocalDateTime dt = LocalDateTime.of(dateDebut.valeur(), heureDebut.valeur());
        LocalDateTime autredt = LocalDateTime.of(autre.dateDebut.valeur(), autre.heureDebut.valeur());
        LocalDateTime fin1 = dt.plusMinutes(dureeMinutes);
        LocalDateTime fin2 = autredt.plusMinutes(autre.dureeMinutes);
        return dt.isBefore(fin2) && fin1.isAfter(autredt);
    }
}
