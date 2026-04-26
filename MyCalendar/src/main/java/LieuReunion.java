public record LieuReunion(String valeur) {
    public LieuReunion {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le lieu ne doit pas être vide");
        }
    }
}
