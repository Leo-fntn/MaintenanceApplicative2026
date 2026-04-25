public record FrequencePeriodique(Integer valeur) {
    public FrequencePeriodique {
        if (valeur == null) {
            throw new IllegalArgumentException("La durée ne doit pas être vide");
        }
    }
}
