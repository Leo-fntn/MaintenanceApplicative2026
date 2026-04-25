
import java.time.LocalDateTime;

public abstract class Event {
    public String type;
    public String title;
    public String proprietaire;
    public LocalDateTime dateDebut;
    public int dureeMinutes;

    public Event(String title, String proprietaire, LocalDateTime dateDebut, int dureeMinutes) {
        this.title = title;
        this.proprietaire = proprietaire;
        this.dateDebut = dateDebut;
        this.dureeMinutes = dureeMinutes;
    }

    public String description() {
        String desc = "";
        if (type.equals("RDV_PERSONNEL")) {
            desc = "RDV : " + title + " à " + dateDebut.toString();
        } else if (type.equals("REUNION")) {
            desc = "Réunion : " + title + " à " + lieu + " avec " + participants;
        } else if (type.equals("PERIODIQUE")) {
            desc = "Événement périodique : " + title + " tous les " + frequenceJours + " jours";
        }
        return desc;
    }
}