import java.time.LocalDateTime;

public class Periodique extends Event {
    public FrequencePeriodique frequenceJours; // uniquement pour PERIODIQUE

    public Periodique(EventId id, TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes, FrequencePeriodique frequenceJours) {
        super(id, title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.frequenceJours = frequenceJours;
    }

    public Periodique(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes, FrequencePeriodique frequenceJours) {
        super(title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.frequenceJours = frequenceJours;
    }

    public String description() {
        return "Événement périodique : " + title.valeur() + " tous les " + frequenceJours.valeur() + " jours";
    }

    @Override
    public Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        // il reste un if, possible de le retirer en construisant un booléen qui passe à true si la condition est valide
        // et qui reste à true ensuite : bool = bool || condition
        // mais inutile et pas opti
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