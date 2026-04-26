public record EventId(String valeur) {
    public EventId {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("L'id ne doit pas être vide");
        }
    }
}
