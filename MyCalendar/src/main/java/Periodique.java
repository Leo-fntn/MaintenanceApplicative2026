import java.time.LocalDateTime;

public class Periodique extends Event {
    public int frequenceJours; // uniquement pour PERIODIQUE

    public Periodique(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, int dureeMinutes, int frequenceJours) {
        super(title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.frequenceJours = frequenceJours;
    }

    public String description() {
        return "Événement périodique : " + title.valeur() + " tous les " + frequenceJours + " jours";
    }

    public Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime temp = LocalDateTime.of(dateDebut.valeur(), heureDebut.valeur());
        while (temp.isBefore(fin)) {
            if (!temp.isBefore(debut)) {
                return true;
            }
            temp = temp.plusDays(frequenceJours);
        }
        return false;
    }

    public Boolean estEnConflit(Event autre) {
        return false;
    }
}