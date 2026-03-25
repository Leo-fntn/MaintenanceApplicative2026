import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CalendarManagerTest {

    @Test
    void ajouteUnEvenementEtLeRetrouveDansPeriode() {
        CalendarManager calendar = new CalendarManager();
        LocalDateTime debut = LocalDateTime.of(2026, 3, 25, 10, 0);

        calendar.ajouterEvent("RDV_PERSONNEL", "Sprint planning", "Leo", debut, 60, "", "", 0);

        List<Event> events = calendar.eventsDansPeriode(debut.minusDays(1), debut.plusDays(1));

        assertEquals(1, events.size());
        assertEquals("Sprint planning", events.get(0).title);
    }

    @Test
    void genereUneDescriptionSpecifiquePourUnRdvPersonnel() {
        Event event = new Event(
                "RDV_PERSONNEL",
                "Dentiste",
                "Leo",
                LocalDateTime.of(2026, 4, 2, 9, 30),
                30,
                "",
                "",
                0
        );

        String description = event.description();

        assertTrue(description.startsWith("RDV : Dentiste"));
    }
}

