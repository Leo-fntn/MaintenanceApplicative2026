import java.time.LocalDateTime;

public class Periodique extends Event {
    public FrequencePeriodique frequenceJours; // uniquement pour PERIODIQUE

    public Periodique(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes, FrequencePeriodique frequenceJours) {
        super(title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.frequenceJours = frequenceJours;
    }

    public String description() {
        return "Événement périodique : " + title.valeur() + " tous les " + frequenceJours + " jours";
    }

    @Override
    public Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        LocalDateTime temp = LocalDateTime.of(dateDebut.valeur(), heureDebut.valeur());
        while (temp.isBefore(fin)) {
            if (!temp.isBefore(debut)) {
                return true;
            }
            temp = temp.plusDays(frequenceJours.valeur());
        }
        return false;
    }

    @Override
    public Boolean estEnConflit(Event autre) {
        return false;
    }
}