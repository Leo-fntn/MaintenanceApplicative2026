public record DureeEvenement(Integer valeur) {
    public DureeEvenement {
        if (valeur == null) {
            throw new IllegalArgumentException("La durée ne doit pas être vide");
        }
    }
}
