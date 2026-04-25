public record ProprietaireEvenement(String valeur) {
    public ProprietaireEvenement {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le proprietaire ne doit pas être vide");
        }
    }
}
