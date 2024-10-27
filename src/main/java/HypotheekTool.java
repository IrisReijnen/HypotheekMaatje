import java.text.DecimalFormat;
import java.util.Scanner;

public class HypotheekTool {

    double inkomenKlant;
    double inkomenPartner;
    boolean heeftPartner;
    boolean heeftStudieschuld;
    int rentevastePeriode;
    double rentePercentage;
    String postcode;
    int looptijd; // in jaren
    final double STUDIESCHULD_KORTING = 0.75;
    final double MAXIMUM_LENING_FACTOR = 4.25;

    double maximaalTeLenenBedrag;
    double maandlasten;
    double totaleBetaling;
    double totaleRente;
    double maandAflossing;
    double maandRente;

    public void start() {
        Scanner scanner = new Scanner(System.in);

        // Vraag 1: Wat is uw jaarinkomen?
        System.out.print("Wat is uw jaarinkomen? ");
        inkomenKlant = scanner.nextDouble();

        // Vraag 2: Heeft u een partner?
        System.out.print("Heeft u een partner? (Ja/Nee) ");
        String antwoordPartner = scanner.next();
        heeftPartner = antwoordPartner.equalsIgnoreCase("ja");

        // Vraag 3: Inkomsten partner
        if (heeftPartner) {
            System.out.print("Wat is het jaarinkomen van uw partner? ");
            inkomenPartner = scanner.nextDouble();
        } else {
            inkomenPartner = 0;
        }

        // Vraag 4: Heeft u een studieschuld?
        System.out.print("Heeft u een studieschuld? (Ja/Nee) ");
        String antwoordStudieschuld = scanner.next();
        heeftStudieschuld = antwoordStudieschuld.equalsIgnoreCase("ja");

        // Vraag 5: Rentevaste periode
        do {
            System.out.print("Wat is uw gewenste rentevaste periode? (1, 5, 10, 20 of 30 jaar) ");
            rentevastePeriode = scanner.nextInt();
            rentePercentage = getRentePercentage(rentevastePeriode);
        } while (rentePercentage == -1); // Als de invoer ongeldig is, vraag opnieuw.

        // Vraag 6: Postcode
        System.out.print("Wat is uw postcode? ");
        postcode = scanner.next();
        if (isInAardbevingsgebied(postcode)) {
            System.out.println("Hypotheken voor postcodegebieden 9679, 9681 of 9682 worden niet geaccepteerd.");
            System.exit(0); // Programma stopt bij ongeldige postcode
        }

        // Looptijd wordt altijd op 30 jaar gezet, ongeacht de gekozen rentevaste periode
        looptijd = 30;

        // Berekeningen en resultaten tonen
        berekenHypotheekDetails();
        toonResultaten();
    }

    // Berekeningen
    public void berekenHypotheekDetails() {
        maximaalTeLenenBedrag = berekenMaximaalTeLenenBedrag();
        maandAflossing = maximaalTeLenenBedrag / (looptijd * 12); // Aflossing per maand over 30 jaar
        maandRente = (maximaalTeLenenBedrag * (rentePercentage / 100)) / 12; // Maandelijkse rente
        maandlasten = maandAflossing + maandRente; // Totale maandlasten
        totaleBetaling = maandlasten * looptijd * 12; // Totale kosten na 30 jaar
        totaleRente = maandRente * looptijd * 12; // Totale rente over 30 jaar
    }

    // Resultaten tonen
    public String toonResultaten() {
        String resultaat = String.format(
                "Maximaal te lenen bedrag: €%.2f%n" +
                        "Maandelijkse rente: €%.2f%n" +
                        "Maandelijkse aflossing: €%.2f%n" +
                        "Totale maandlasten: €%.2f%n" +
                        "Totale te betalen rente over de gehele looptijd: €%.2f%n" +
                        "Totale kosten over de looptijd van %d jaar: €%.2f%n",
                maximaalTeLenenBedrag, maandRente, maandAflossing, maandlasten, totaleRente, looptijd, totaleBetaling
        );

        System.out.printf("Maximaal te lenen bedrag: €%.2f%n", maximaalTeLenenBedrag);
        System.out.printf("Maandelijkse rente: €%.2f%n", maandRente);
        System.out.printf("Maandelijkse aflossing: €%.2f%n", maandAflossing);
        System.out.printf("Totale maandlasten: €%.2f%n", maandlasten);
        System.out.printf("Totale te betalen rente over de gehele looptijd: €%.2f%n", totaleRente);
        System.out.printf("Totale kosten over de looptijd van %d jaar: €%.2f%n", looptijd, totaleBetaling);

        return resultaat;
    }

    // Bereken het maximaal te lenen bedrag
    public double berekenMaximaalTeLenenBedrag() {
        double totaalInkomen = inkomenKlant + inkomenPartner;
        double maximaalBedrag = totaalInkomen * MAXIMUM_LENING_FACTOR;

        if (heeftStudieschuld) {
            maximaalBedrag *= STUDIESCHULD_KORTING; // 25% minder lenen
        }

        return maximaalBedrag;
//        DecimalFormat df = new DecimalFormat("#.00");
//        return Double.parseDouble(df.format(maximaalBedrag));
//        return Math.round(maximaalBedrag * 100.0) / 100.0;
    }

    // Haal het rentepercentage op basis van de rentevaste periode
    public double getRentePercentage(int periode) {
        switch (periode) {
            case 1: return 2.0;
            case 5: return 3.0;
            case 10: return 3.5;
            case 20: return 4.5;
            case 30: return 5.0;
            default:
                System.out.println("Ongeldige rentevaste periode. Kies uit 1, 5, 10, 20 of 30 jaar.");
                return -1;
        }
    }

    // Check of de postcode in het aardbevingsgebied ligt
    public boolean isInAardbevingsgebied(String postcode) {
        return postcode.equals("9679") || postcode.equals("9681") || postcode.equals("9682");
    }

    public static void main(String[] args) {
        HypotheekTool tool = new HypotheekTool();
        tool.start();
    }
}
