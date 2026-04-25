import java.time.LocalDateTime;

public class RdvPersonnel extends Event{
    public RdvPersonnel(String title, String proprietaire, LocalDateTime dateDebut, int dureeMinutes) {
        super(title, proprietaire, dateDebut, dureeMinutes);
        this.type = "RDV_PERSONNEL";
    }
}
