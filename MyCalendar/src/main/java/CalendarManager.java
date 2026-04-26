
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CalendarManager {
    public List<Event> events;

    public CalendarManager() {
        this.events = new ArrayList<>();
    }

    public void ajouterEvent(Event e) {
        events.stream()
                .filter(exist -> conflit(exist, e))
                .findFirst()
                .ifPresent(exist -> { throw new ConflitEvenementException(exist, e); });
        events.add(e);
    }

    public List<Event> eventsDansPeriode(LocalDateTime debut, LocalDateTime fin) {
        return events.stream()
                .filter(e -> e.estDansPeriode(debut, fin))
                .collect(Collectors.toList());
    }

    public boolean conflit(Event e1, Event e2) {
        return e1.estEnConflit(e2) && e2.estEnConflit(e1);
    }

    public void afficherEvenements() {
        for (Event e : events) {
            System.out.println(e.description());
        }
    }

    // CalendarManager.java — ajouter la méthode
    public void supprimerEvent(EventId id) {
        events.removeIf(e -> e.id.equals(id));
    }
}