import java.time.LocalDateTime;

public class RdvPersonnel extends Event{
    public RdvPersonnel(TitreEvenement title, String proprietaire, LocalDateTime dateDebut, int dureeMinutes) {
        super(title, proprietaire, dateDebut, dureeMinutes);
    }

    public String description() {
        return "RDV : " + title.valeur() + " à " + dateDebut.toString();
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
