import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CalendarManagerTest {

    private static final LocalDateTime TIME = LocalDateTime.of(2026, 6, 15, 9, 0);
    private CalendarManager calendar;

    @BeforeEach
    void setUp() {
        calendar = new CalendarManager();
    }

    // Tests pour la description des évènements
    
    @Test
    void rdvPersonnelCommenceParRDV() {
        Event e = new Event("RDV_PERSONNEL", "Dentiste", "Leo", TIME, 30, "", "", 0);
        assertTrue(e.description().startsWith("RDV : "));
    }

    @Test
    void rdvPersonnelContientTitreEtDate() {
        Event e = new Event("RDV_PERSONNEL", "Dentiste", "Leo", TIME, 30, "", "", 0);
        assertTrue(e.description().contains("Dentiste"));
        assertTrue(e.description().contains(TIME.toString()));
    }

    @Test
    void reunionContientTitreLieuEtParticipants() {
        Event e = new Event("REUNION", "Sprint", "Leo", TIME, 60, "Salle B", "Leo, Ilan", 0);
        assertTrue(e.description().contains("Sprint"));
        assertTrue(e.description().contains("Salle B"));
        assertTrue(e.description().contains("Leo, Ilan"));
    }

    @Test
    void periodiqueMentionneFrequenceEtTitre() {
        Event e = new Event("PERIODIQUE", "Stand-up", "Leo", TIME, 0, "", "", 7);
        assertTrue(e.description().contains("Stand-up"));
        assertTrue(e.description().contains("7"));
    }

    @Test
    void typeInconnuRetourneStringVide() {
        // Comportement actuel documenté : aucun if ne matche → retourne ""
        Event e = new Event("INCONNU", "Truc", "Leo", TIME, 0, "", "", 0);
        assertEquals("", e.description());
    }

    // Tests d'ajouts d'évènements dans le calendrier

    @Test
    void calendrierVideAuDepart() {
        assertTrue(calendar.events.isEmpty());
    }

    @Test
    void ajouterUnEvenementIncrementeLaListe() {
        calendar.ajouterEvent("RDV_PERSONNEL", "Coiffeur", "Leo", TIME, 45, "", "", 0);
        assertEquals(1, calendar.events.size());
    }

    @Test
    void ajouterPlusieursEvenements() {
        calendar.ajouterEvent("RDV_PERSONNEL", "Coiffeur", "Leo", TIME, 45, "", "", 0);
        calendar.ajouterEvent("REUNION", "Revue", "Leo", TIME.plusHours(2), 60, "Salle A", "Leo", 0);
        calendar.ajouterEvent("PERIODIQUE", "Stand-up", "Leo", TIME.plusDays(1), 0, "", "", 1);
        assertEquals(3, calendar.events.size());
    }

    @Test
    void evenementAjouteConserveSesDonnees() {
        calendar.ajouterEvent("REUNION", "Démo", "Ilan", TIME, 90, "Salle C", "Ilan, Noah", 0);
        Event e = calendar.events.getFirst();
        assertEquals("REUNION",    e.type);
        assertEquals("Démo",       e.title);
        assertEquals("Ilan",      e.proprietaire);
        assertEquals(TIME,         e.dateDebut);
        assertEquals(90,           e.dureeMinutes);
        assertEquals("Salle C",    e.lieu);
        assertEquals("Ilan, Noah", e.participants);
    }

    // Tests pour la recherche d'évènements dans une période

    @Test
    void evenementDansLaPeriodeEstRetourne() {
        calendar.ajouterEvent("RDV_PERSONNEL", "Médecin", "Leo", TIME, 30, "", "", 0);
        List<Event> result = calendar.eventsDansPeriode(TIME.minusDays(1), TIME.plusDays(1));
        assertEquals(1, result.size());
    }

    @Test
    void evenementHorsPeriodeNestPasRetourne() {
        calendar.ajouterEvent("RDV_PERSONNEL", "Médecin", "Leo", TIME, 30, "", "", 0);
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(1), TIME.plusDays(3));
        assertTrue(result.isEmpty());
    }

    @Test
    void evenementSurLaBorneDebutEstInclus() {
        calendar.ajouterEvent("RDV_PERSONNEL", "Exact", "Leo", TIME, 30, "", "", 0);
        List<Event> result = calendar.eventsDansPeriode(TIME, TIME.plusHours(1));
        assertEquals(1, result.size());
    }

    @Test
    void evenementPeriodiqueApparaitSiUneOccurrenceTombeDansLaPeriode() {
        // départ BASE, fréquence 7j → occurrence à BASE+14 tombe dans [BASE+13, BASE+15]
        calendar.ajouterEvent("PERIODIQUE", "Hebdo", "Leo", TIME, 0, "", "", 7);
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(13), TIME.plusDays(15));
        assertEquals(1, result.size());
    }

    @Test
    void evenementPeriodiqueNApparaitPasQuandAucuneOccurrenceDansLaPeriode() {
        // fréquence 7j, cherche entre +1j et +3j → aucune occurrence
        calendar.ajouterEvent("PERIODIQUE", "Hebdo", "Leo", TIME, 0, "", "", 7);
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(1), TIME.plusDays(3));
        assertTrue(result.isEmpty());
    }

    // Tests pour la détection de conflits entre évènements

    @Test
    void deuxEvenementsQuiSeChevauchentSontEnConflit() {
        Event e1 = new Event("RDV_PERSONNEL", "A", "Leo", TIME, 60, "", "", 0);
        Event e2 = new Event("RDV_PERSONNEL", "B", "Leo", TIME.plusMinutes(30), 60, "", "", 0);
        assertTrue(calendar.conflit(e1, e2));
    }

    @Test
    void deuxEvenementsDisjointsSontSansConflit() {
        Event e1 = new Event("RDV_PERSONNEL", "A", "Leo", TIME, 60, "", "", 0);
        Event e2 = new Event("RDV_PERSONNEL", "B", "Leo", TIME.plusHours(2), 60, "", "", 0);
        assertFalse(calendar.conflit(e1, e2));
    }

    @Test
    void deuxEvenementsAdjacentsSontSansConflit() {
        Event e1 = new Event("RDV_PERSONNEL", "A", "Leo", TIME, 60, "", "", 0);
        Event e2 = new Event("RDV_PERSONNEL", "B", "Leo", TIME.plusHours(1), 60, "", "", 0);
        assertFalse(calendar.conflit(e1, e2));
    }

    @Test
    void conflitImpliquantUnPeriodiqueToujoursRetourneFalse() {
        Event e1 = new Event("RDV_PERSONNEL", "A", "Leo", TIME, 60, "", "", 0);
        Event e2 = new Event("PERIODIQUE", "Hebdo", "Leo", TIME, 0, "", "", 7);
        assertFalse(calendar.conflit(e1, e2));
        assertFalse(calendar.conflit(e2, e1));
    }
}