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
        Event e = new Reunion(new TitreEvenement("Sprint"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60), new LieuReunion("Salle B"), new ParticipantsReunion("Léo, Ilan"));
        assertTrue(e.description().contains("Sprint"));
        assertTrue(e.description().contains("Salle B"));
        assertTrue(e.description().contains("Léo, Ilan"));
    }

    @Test
    void periodiqueMentionneFrequenceEtTitre() {
        Event e = new Periodique(new TitreEvenement("Stand-up"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(0), new FrequencePeriodique(7));
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
        calendar.ajouterEvent(new Reunion(new TitreEvenement("Revue"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusHours(2)), new DureeEvenement(60), new LieuReunion("Salle A"), new ParticipantsReunion("Léo")));
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Stand-up"), new ProprietaireEvenement("Léo"), new DateEvenement(DATE.valeur().plusDays(1)), HEURE, new DureeEvenement(0), new FrequencePeriodique(1)));
        assertEquals(3, calendar.events.size());
    }

    @Test
    void evenementAjouteConserveSesDonnees() {
        calendar.ajouterEvent(new Reunion(new TitreEvenement("Démo"), new ProprietaireEvenement("Ilan"), DATE, HEURE, new DureeEvenement(90), new LieuReunion("Salle C"), new ParticipantsReunion("Ilan, Noah")));
        Reunion e = (Reunion) calendar.events.getFirst();
        assertEquals(new TitreEvenement("Démo"),          e.title);
        assertEquals(new ProprietaireEvenement("Ilan"),    e.proprietaire);
        assertEquals(DATE,                                 e.dateDebut);
        assertEquals(HEURE,                                e.heureDebut);
        assertEquals(new DureeEvenement(90),               e.dureeMinutes);
        assertEquals(new LieuReunion("Salle C"),                            e.lieu);
        assertEquals(new ParticipantsReunion("Ilan, Noah"),                         e.participants);
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
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(0), new FrequencePeriodique(7)));
        List<Event> result = calendar.eventsDansPeriode(TIME.plusDays(13), TIME.plusDays(15));
        assertEquals(1, result.size());
    }

    @Test
    void evenementPeriodiqueNApparaitPasQuandAucuneOccurrenceDansLaPeriode() {
        calendar.ajouterEvent(new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(0), new FrequencePeriodique(7)));
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
        Event e2 = new Periodique(new TitreEvenement("Hebdo"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(0), new FrequencePeriodique(7));
        assertFalse(calendar.conflit(e1, e2));
        assertFalse(calendar.conflit(e2, e1));
    }

    // Tests supprimer un evenement avec son ID

    @Test
    void supprimerUnEvenementParSonId() {
        EventId id = new EventId("abc-123");
        calendar.ajouterEvent(new RdvPersonnel(id, new TitreEvenement("Dentiste"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(30)));
        calendar.supprimerEvent(id);
        assertTrue(calendar.events.isEmpty());
    }

    @Test
    void supprimerUnEvenementNaffectePasLesAutres() {
        EventId id1 = new EventId("abc-123");
        EventId id2 = new EventId("def-456");
        calendar.ajouterEvent(new RdvPersonnel(id1, new TitreEvenement("Dentiste"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(30)));
        calendar.ajouterEvent(new RdvPersonnel(id2, new TitreEvenement("Coiffeur"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(45)));
        calendar.supprimerEvent(id1);
        assertEquals(1, calendar.events.size());
        assertEquals(id2, ((RdvPersonnel) calendar.events.getFirst()).id);
    }

    @Test
    void supprimerUnIdInexistantNeFaitRien() {
        calendar.ajouterEvent(new RdvPersonnel(new EventId("abc-123"), new TitreEvenement("Dentiste"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(30)));
        calendar.supprimerEvent(new EventId("inexistant"));
        assertEquals(1, calendar.events.size());
    }

    // Tests pour l'ajout d'un nouveau type d'Event :
    // Tests pour RdvClient
    @Test
    void rdvClientContientTitreClientEtDate() {
        Event e = new RdvClient(new TitreEvenement("Démo produit"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60), new NomClient("Acme Corp"));
        assertTrue(e.description().contains("Démo produit"));
        assertTrue(e.description().contains("Acme Corp"));
        assertTrue(e.description().contains(DATE.valeur().toString()));
    }

    @Test
    void rdvClientCommenceParRdvClient() {
        Event e = new RdvClient(new TitreEvenement("Démo produit"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60), new NomClient("Acme Corp"));
        assertTrue(e.description().startsWith("RDV Client : "));
    }

    @Test
    void rdvClientEstRetourneDansPeriode() {
        calendar.ajouterEvent(new RdvClient(new TitreEvenement("Démo produit"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60), new NomClient("Acme Corp")));
        List<Event> result = calendar.eventsDansPeriode(TIME.minusDays(1), TIME.plusDays(1));
        assertEquals(1, result.size());
    }

    @Test
    void rdvClientEstEnConflitAvecUnRdvPersonnel() {
        Event e1 = new RdvClient(new TitreEvenement("Démo produit"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60), new NomClient("Acme Corp"));
        Event e2 = new RdvPersonnel(new TitreEvenement("Appel"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusMinutes(30)), new DureeEvenement(60));
        assertTrue(calendar.conflit(e1, e2));
    }

    // Tests : vérification automatique des conflits à l'insertion

    @Test
    void ajouterUnEvenementEnConflitLeveUneException() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60)));
        assertThrows(ConflitEvenementException.class, () ->
                calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusMinutes(30)), new DureeEvenement(60)))
        );
    }

    @Test
    void ajouterUnEvenementSansConflitNeLevePasException() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60)));
        assertDoesNotThrow(() ->
                calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusHours(2)), new DureeEvenement(60)))
        );
    }

    @Test
    void ajouterUnEvenementEnConflitNAjoutePasLevenement() {
        calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("A"), new ProprietaireEvenement("Léo"), DATE, HEURE, new DureeEvenement(60)));
        try {
            calendar.ajouterEvent(new RdvPersonnel(new TitreEvenement("B"), new ProprietaireEvenement("Léo"), DATE, new HeureDebut(HEURE.valeur().plusMinutes(30)), new DureeEvenement(60)));
        } catch (ConflitEvenementException e) { }
        assertEquals(1, calendar.events.size());
    }
}