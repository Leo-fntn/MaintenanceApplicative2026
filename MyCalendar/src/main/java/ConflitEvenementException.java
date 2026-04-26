public class ConflitEvenementException extends RuntimeException {
    public ConflitEvenementException(Event e1, Event e2) {
        super("Conflit entre \"" + e1.title.valeur() + "\" et \"" + e2.title.valeur() + "\"");
    }
}