public record ParticipantsReunion(String valeur) {
    public ParticipantsReunion {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Les participants ne doit pas être vide");
        }
    }
}
