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

    public String description() {
        return "Réunion : " + title + " à " + lieu + " avec " + participants;
    }

    public Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        return !dateDebut.isBefore(debut) && !dateDebut.isAfter(fin);
    }

    public Boolean estEnConflit(Event autre) {
        LocalDateTime fin1 = dateDebut.plusMinutes(dureeMinutes);
        LocalDateTime fin2 = autre.dateDebut.plusMinutes(autre.dureeMinutes);
        return dateDebut.isBefore(fin2) && fin1.isAfter(autre.dateDebut);
    }
}
