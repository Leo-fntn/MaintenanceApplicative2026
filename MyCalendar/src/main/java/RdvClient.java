public class RdvClient extends Event{
    public NomClient nomClient;

    public RdvClient(EventId id, TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes, NomClient nomClient) {
        super(id, title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.nomClient = nomClient;
    }

    public RdvClient(TitreEvenement title, ProprietaireEvenement proprietaire, DateEvenement dateDebut, HeureDebut heureDebut, DureeEvenement dureeMinutes, NomClient nomClient) {
        super(title, proprietaire, dateDebut, heureDebut, dureeMinutes);
        this.nomClient = nomClient;
    }

    public String description(){
        return "RDV : " + title.valeur() + " le " + dateDebut.valeur().toString() + " à " + heureDebut.valeur().toString() + " avec " + nomClient.valeur();
    }
}
