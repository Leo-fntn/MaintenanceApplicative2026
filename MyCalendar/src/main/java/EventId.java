public record EventId(String valeur) {
    public EventId {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le titre ne doit pas être vide");
        }
    }
}
