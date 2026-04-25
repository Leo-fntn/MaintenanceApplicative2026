import java.time.LocalDateTime;

public class RdvPersonnel extends Event{
    public RdvPersonnel(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, int dureeMinutes) {
        super(title, proprietaire, dateDebut, heureDebut, dureeMinutes);
    }

    public String description() {
        return "RDV : " + title.valeur() + " le " + dateDebut.valeur().toString() + " à " + heureDebut.valeur().toString();
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
