import java.time.LocalTime;

public record HeureDebut(LocalTime valeur) {
    public HeureDebut {
        if (valeur == null) {
            throw new IllegalArgumentException("L'heure de début ne doit pas être vide");
        }
    }
}
