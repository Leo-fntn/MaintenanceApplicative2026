public record NomClient(String valeur) {
    public NomClient {
        if (valeur == null || valeur.isBlank()) {
            throw new IllegalArgumentException("Le client ne doit pas être vide");
        }
    }
}
