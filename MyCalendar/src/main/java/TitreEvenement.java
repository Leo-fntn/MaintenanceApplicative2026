public record TitreEvenement(String valeur) {
    public TitreEvenement {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le titre ne doit pas être vide");
        }
    }
}
