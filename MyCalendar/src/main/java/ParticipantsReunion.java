public record ParticipantsReunion(String valeur) {
    public ParticipantsReunion {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le titre ne doit pas être vide");
        }
    }
}
