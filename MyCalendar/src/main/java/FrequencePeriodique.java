public record FrequencePeriodique(Integer valeur) {
    public FrequencePeriodique {
        if (valeur == null) {
            throw new IllegalArgumentException("La fréquence ne doit pas être vide");
        }
    }
}
