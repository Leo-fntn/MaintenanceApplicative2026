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
        Event e = new RdvPersonnel(new TitreEvenement("Dentiste"), new ProprietaireEvenement("Léo"), TIME, 30);
        assertTrue(e.description().startsWith("RDV : "));
    }

    @Test
    void rdvPersonnelContientTitreEtDate() {
        Event e = new RdvPersonnel(new TitreEvenement("Dentiste"), new ProprietaireEvenement("Léo"), TIME, 30);
        assertTrue(e.description().contains("Dentiste"));
        assertTrue(e.description().contains(TIME.toString()));
    }

    @Test
    void reunionContientTitreLieuEtParticipants() {
        Event e = new Reunion(new TitreEvenement("Sprint"), new ProprietaireEvenement("Léo"), TIME, 60, "Salle B", "Léo, Ilan");
        assertTrue(e.description().contains("Sprint"));
        assertTrue(e.description().contains("Salle B"));
        assertTrue(e.description().contains("Léo, Ilan"));
    }

    @Test
    void periodiqueMentionneFrequenceEtTitre() {
        Event e = new Periodique(new TitreEvenement("Stand-up"), new ProprietaireEvenement("Léo"), TIME, 0, 7);
        assertTrue(e.description().contains("Stand-up"));
        assertTrue(e.description().contains("7"));
    }

    // Tests d'ajouts d'évènements dans le calendrier

    @Test
    void calendrierVideAuDepart() {
        assertTrue(calendar.events.isEmpty());
    }

    @Test
    void ajouterUnEvenementIncrementeLaListe() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Coiffeur"), new ProprietaireEvenement("Léo"), TIME, 45));
        assertEquals(1, calendar.events.size());
    }

    @Test
    void ajouterPlusieursEvenements() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Coiffeur"), new ProprietaireEvenement("Léo"), TIME, 45));
        calendar.ajouterEvent(new Reunion(new TitreEvenement("Revue"), new ProprietaireEvenement("Léo"), TIME.plusHours(2), 60, "Salle A", "Léo"));
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Stand-up"), new ProprietaireEvenement("Léo"), TIME.plusDays(1), 0, 1));
        assertEquals(3, calendar.events.size());
    }

    @Test
    void evenementAjouteConserveSesDonnees() {
        calendar.ajouterEvent(new Reunion(new TitreEvenement("Démo"), new ProprietaireEvenement("Ilan"), TIME, 90, "Salle C", "Ilan, Noah"));
        Reunion e = (Reunion) calendar.events.getFirst();
        assertEquals(new TitreEvenement("Démo"),       e.title);
        assertEquals(new ProprietaireEvenement("Ilan"),      e.proprietaire);
        assertEquals(TIME,         e.dateDebut);
        assertEquals(90,           e.dureeMinutes);
        assertEquals("Salle C",    e.lieu);
        assertEquals("Ilan, Noah", e.participants);
    }

    // Tests pour la recherche d'évènements dans une période

    @Test
    void evenementDansLaPeriodeEstRetourne() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Médecin"), new ProprietaireEvenement("Léo"), TIME, 30));
        List<Event> result = calendar.eventsDansPeriode(TIME.minusDays(1), TIME.plusDays(1));
        assertEquals(1, result.size());
    }

    @Test
    void evenementHorsPeriodeNestPasRetourne() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Médecin"), new ProprietaireEvenement("Léo"), TIME, 30));
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(1), TIME.plusDays(3));
        assertTrue(result.isEmpty());
    }

    @Test
    void evenementSurLaBorneDebutEstInclus() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Médecin"), new ProprietaireEvenement("Léo"), TIME, 30));
        List<Event> result = calendar.eventsDansPeriode(TIME, TIME.plusHours(1));
        assertEquals(1, result.size());
    }

    @Test
    void evenementPeriodiqueApparaitSiUneOccurrenceTombeDansLaPeriode() {
        // départ BASE, fréquence 7j → occurrence à BASE+14 tombe dans [BASE+13, BASE+15]
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), TIME, 0, 7));
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(13), TIME.plusDays(15));
        assertEquals(1, result.size());
    }

    @Test
    void evenementPeriodiqueNApparaitPasQuandAucuneOccurrenceDansLaPeriode() {
        // fréquence 7j, cherche entre +1j et +3j → aucune occurrence
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), TIME, 0, 7));
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(1), TIME.plusDays(3));
        assertTrue(result.isEmpty());
    }

    // Tests pour la détection de conflits entre évènements

    @Test
    void deuxEvenementsQuiSeChevauchentSontEnConflit() {
        Event e1 = new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), TIME, 60);
        Event e2 = new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), TIME.plusMinutes(30), 60);
        assertTrue(calendar.conflit(e1, e2));
    }

    @Test
    void deuxEvenementsDisjointsSontSansConflit() {
        Event e1 = new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), TIME, 60);
        Event e2 = new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), TIME.plusHours(2), 60);
        assertFalse(calendar.conflit(e1, e2));
    }

    @Test
    void deuxEvenementsAdjacentsSontSansConflit() {
        Event e1 = new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), TIME, 60);
        Event e2 = new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), TIME.plusHours(1), 60);
        assertFalse(calendar.conflit(e1, e2));
    }

    @Test
    void conflitImpliquantUnPeriodiqueToujoursRetourneFalse() {
        Event e1 = new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), TIME, 60);
        Event e2 = new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), TIME, 0, 7);
        assertFalse(calendar.conflit(e1, e2));
        assertFalse(calendar.conflit(e2, e1));
    }
}