import java.time.LocalDate;

public record DateEvenement(LocalDate valeur) {
    public DateEvenement {
        if (valeur == null) {
            throw new IllegalArgumentException("La date ne doit pas être vide");
        }
    }
}
