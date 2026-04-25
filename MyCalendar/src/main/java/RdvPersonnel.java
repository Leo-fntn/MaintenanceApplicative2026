import java.time.LocalDateTime;

public class RdvPersonnel extends Event{
    public RdvPersonnel(String title, String proprietaire, LocalDateTime dateDebut, int dureeMinutes) {
        super(title, proprietaire, dateDebut, dureeMinutes);
        this.type = "RDV_PERSONNEL";
    }

    public String description() {
        return "RDV : " + title + " à " + dateDebut.toString();
    }

    public Boolean estDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        return !dateDebut.isBefore(debut) && !dateDebut.isAfter(fin);
    }
}
