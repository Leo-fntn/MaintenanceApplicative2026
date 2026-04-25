import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CalendarManagerTest {

    private static final DateEvenement DATE = new DateEvenement(LocalDate.of(2026, 6, 15));
    private static final HeureDebut HEURE = new HeureDebut(LocalTime.of(9, 0));
    private static final LocalDateTime TIME = LocalDateTime.of(2026, 6, 15, 9, 0);
    private CalendarManager calendar;

    @BeforeEach
    void setUp() {
        calendar = new CalendarManager();
    }

    // Tests pour la description des évènements

    @Test
    void rdvPersonnelCommenceParRDV() {
        Event e = new RdvPersonnel(new TitreEvenement("Dentiste"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(30));
        assertTrue(e.description().startsWith("RDV : "));
    }

    @Test
    void rdvPersonnelContientTitreEtDate() {
        Event e = new RdvPersonnel(new TitreEvenement("Dentiste"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(30));
        assertTrue(e.description().contains("Dentiste"));
        assertTrue(e.description().contains(DATE.valeur().toString()));
    }

    @Test
    void reunionContientTitreLieuEtParticipants() {
        Event e = new Reunion(new TitreEvenement("Sprint"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60), "Salle B", "Léo, Ilan");
        assertTrue(e.description().contains("Sprint"));
        assertTrue(e.description().contains("Salle B"));
        assertTrue(e.description().contains("Léo, Ilan"));
    }

    @Test
    void periodiqueMentionneFrequenceEtTitre() {
        Event e = new Periodique(new TitreEvenement("Stand-up"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(0), 7);
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
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Coiffeur"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(45)));
        assertEquals(1, calendar.events.size());
    }

    @Test
    void ajouterPlusieursEvenements() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Coiffeur"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(45)));
        calendar.ajouterEvent(new Reunion(new TitreEvenement("Revue"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusHours(2)), new DureeEvenement(60), "Salle A", "Léo"));
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Stand-up"), new ProprietaireEvenement("Léo"), new DateEvenement(DATE.valeur().plusDays(1)), HEURE, new DureeEvenement(0), 1));
        assertEquals(3, calendar.events.size());
    }

    @Test
    void evenementAjouteConserveSesDonnees() {
        calendar.ajouterEvent(new Reunion(new TitreEvenement("Démo"), new ProprietaireEvenement("Ilan"), DATE, HEURE, new DureeEvenement(90), "Salle C", "Ilan, Noah"));
        Reunion e = (Reunion) calendar.events.getFirst();
        assertEquals(new TitreEvenement("Démo"),          e.title);
        assertEquals(new ProprietaireEvenement("Ilan"),    e.proprietaire);
        assertEquals(DATE,                                 e.dateDebut);
        assertEquals(HEURE,                                e.heureDebut);
        assertEquals(new DureeEvenement(90),               e.dureeMinutes);
        assertEquals("Salle C",                            e.lieu);
        assertEquals("Ilan, Noah",                         e.participants);
    }

    // Tests pour la recherche d'évènements dans une période

    @Test
    void evenementDansLaPeriodeEstRetourne() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Médecin"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(30)));
        List<Event> result = calendar.eventsDansPeriode(TIME.minusDays(1), TIME.plusDays(1));
        assertEquals(1, result.size());
    }

    @Test
    void evenementHorsPeriodeNestPasRetourne() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Médecin"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(30)));
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(1), TIME.plusDays(3));
        assertTrue(result.isEmpty());
    }

    @Test
    void evenementSurLaBorneDebutEstInclus() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("Médecin"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(30)));
        List<Event> result = calendar.eventsDansPeriode(TIME, TIME.plusHours(1));
        assertEquals(1, result.size());
    }

    @Test
    void evenementPeriodiqueApparaitSiUneOccurrenceTombeDansLaPeriode() {
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(0), 7));
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(13), TIME.plusDays(15));
        assertEquals(1, result.size());
    }

    @Test
    void evenementPeriodiqueNApparaitPasQuandAucuneOccurrenceDansLaPeriode() {
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(0), 7));
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(1), TIME.plusDays(3));
        assertTrue(result.isEmpty());
    }

    // Tests pour la détection de conflits entre évènements

    @Test
    void deuxEvenementsQuiSeChevauchentSontEnConflit() {
        Event e1 = new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60));
        Event e2 = new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusMinutes(30)), new DureeEvenement(60));
        assertTrue(calendar.conflit(e1, e2));
    }

    @Test
    void deuxEvenementsDisjointsSontSansConflit() {
        Event e1 = new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60));
        Event e2 = new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusHours(2)), new DureeEvenement(60));
        assertFalse(calendar.conflit(e1, e2));
    }

    @Test
    void deuxEvenementsAdjacentsSontSansConflit() {
        Event e1 = new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60));
        Event e2 = new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusHours(1)), new DureeEvenement(60));
        assertFalse(calendar.conflit(e1, e2));
    }

    @Test
    void conflitImpliquantUnPeriodiqueToujoursRetourneFalse() {
        Event e1 = new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60));
        Event e2 = new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(0), 7);
        assertFalse(calendar.conflit(e1, e2));
        assertFalse(calendar.conflit(e2, e1));
    }
}