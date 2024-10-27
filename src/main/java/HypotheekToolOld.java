//import java.util.Scanner;
//
//public class HypotheekToolOld {
//
//    // Rentepercentages op basis van de rentevaste periode
//    private final double[] rentePercentages = {2.0, 3.0, 3.5, 4.5, 5.0}; // for 1, 5, 10, 20, 30 years
//
//    // Postcodes die niet geaccepteerd worden
//    private final int[] verbodenPostcodes = {9679, 9681, 9682};
//
//    private Scanner scanner = new Scanner(System.in);
//
//    public static void main(String[] args) {
//        HypotheekTool tool = new HypotheekTool();
//        tool.startHypotheekTool();
//    }
//
//    public void startHypotheekTool() {
//        double inkomen = vraagMaandinkomen();
//        double partnerInkomen = vraagPartnerInkomen();
//        boolean heeftStudieschuld = vraagHeeftStudieschuld();
//        int rentevastePeriode = vraagRentevastePeriode();
//        int postcode = vraagPostcode();
//
//        if (isVerbodenPostcode(postcode)) {
//            System.out.println("Hypotheek aanvragen in dit gebied is niet toegestaan.");
//            return;
//        }
//
//        double maxLening = berekenMaxLening(inkomen, partnerInkomen, heeftStudieschuld, rentevastePeriode);
//
//        if (maxLening == 0) {
//            System.out.println("Er is een fout opgetreden bij het berekenen van de lening. Probeer het opnieuw.");
//            return;
//        }
//
//        int looptijd = vraagLooptijd();
//        double maandlasten = berekenMaandlasten(maxLening, rentevastePeriode, looptijd);
//        double totaleKosten = maandlasten * looptijd * 12;
//
//        // Resultaten tonen
//        System.out.printf("Maximaal te lenen bedrag: €%.2f\n", maxLening);
//        System.out.printf("Maandlasten: €%.2f\n", maandlasten);
//        System.out.printf("Totale kosten over %d jaar: €%.2f\n", looptijd, totaleKosten);
//    }
//
//    // Methoden voor invoer vragen
//    protected double vraagMaandinkomen() {
//        double inkomen = 0;
//        while (inkomen <= 0) {
//            try {
//                System.out.print("Voer het maandinkomen in: ");
//                inkomen = Double.parseDouble(scanner.nextLine());
//                if (inkomen <= 0) {
//                    System.out.println("Het inkomen moet groter zijn dan 0.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Ongeldige invoer. Voer een geldig getal in.");
//            }
//        }
//        return inkomen;
//    }
//
//    protected double vraagPartnerInkomen() {
//        double partnerInkomen = 0;
//        System.out.print("Heb je een partner? (Ja/Nee): ");
//        String partner = scanner.nextLine().trim().toLowerCase();
//        if (partner.equals("ja")) {
//            while (partnerInkomen <= 0) {
//                try {
//                    System.out.print("Voer het maandinkomen van de partner in: ");
//                    partnerInkomen = Double.parseDouble(scanner.nextLine());
//                    if (partnerInkomen < 0) {
//                        System.out.println("Het partnerinkomen kan niet negatief zijn.");
//                    }
//                } catch (NumberFormatException e) {
//                    System.out.println("Ongeldige invoer. Voer een geldig getal in.");
//                }
//            }
//        }
//        return partnerInkomen;
//    }
//
//    protected boolean vraagHeeftStudieschuld() {
//        System.out.print("Heeft de klant een studieschuld? (Ja/Nee): ");
//        String heeftStudieschuldInput = scanner.nextLine().trim().toLowerCase();
//        return heeftStudieschuldInput.equals("ja");
//    }
//
//    protected int vraagRentevastePeriode() {
//        int rentevastePeriode = 0;
//        while (rentevastePeriode != 1 && rentevastePeriode != 5 && rentevastePeriode != 10 && rentevastePeriode != 20 && rentevastePeriode != 30) {
//            try {
//                System.out.print("Kies de rentevaste periode (1, 5, 10, 20, 30 jaar): ");
//                rentevastePeriode = Integer.parseInt(scanner.nextLine());
//                if (rentevastePeriode != 1 && rentevastePeriode != 5 && rentevastePeriode != 10 && rentevastePeriode != 20 && rentevastePeriode != 30) {
//                    System.out.println("Ongeldige invoer. Kies 1, 5, 10, 20 of 30 jaar.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Ongeldige invoer. Voer een geldig getal in.");
//            }
//        }
//        return rentevastePeriode;
//    }
//
//    protected int vraagPostcode() {
//        int postcode = 0;
//        while (postcode <= 0) {
//            try {
//                System.out.print("Voer de postcode in: ");
//                postcode = Integer.parseInt(scanner.nextLine());
//                if (postcode <= 0) {
//                    System.out.println("De postcode moet een positief getal zijn.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Ongeldige invoer. Voer een geldig getal in.");
//            }
//        }
//        return postcode;
//    }
//
//    protected int vraagLooptijd() {
//        int looptijd = 0;
//        while (looptijd <= 0) {
//            try {
//                System.out.print("Voer de looptijd in jaren in (bijvoorbeeld 30): ");
//                looptijd = Integer.parseInt(scanner.nextLine());
//                if (looptijd <= 0) {
//                    System.out.println("De looptijd moet groter zijn dan 0.");
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Ongeldige invoer. Voer een geldig getal in.");
//            }
//        }
//        return looptijd;
//    }
//
//    // Bereken het maximaal te lenen bedrag
//    public double berekenMaxLening(double inkomen, double partnerInkomen, boolean heeftStudieschuld, int rentevastePeriode) {
//        double totaalInkomen = inkomen + partnerInkomen;
//
//        // Stel een fictieve factor in voor hoeveel keer het inkomen kan worden geleend (bijv. 5 keer het jaarinkomen)
//        double leningFactor = 5;
//
//        // Controleer de rentevaste periode en bijbehorende rentepercentage
//        double rente = krijgRente(rentevastePeriode);
//        if (rente == -1) {
//            System.out.println("Ongeldige rentevaste periode.");
//            return 0;
//        }
//
//        // Bereken het maximale bedrag
//        double maxLening = totaalInkomen * 12 * leningFactor;
//
//        // Verminder het bedrag met 25% als de klant een studieschuld heeft
//        if (heeftStudieschuld) {
//            maxLening = berekenStudieSchuld(maxLening);
//        }
//
//        return maxLening;
//    }
//
//    public double berekenStudieSchuld(double bedrag) {
//        return bedrag *= 0.75;
//    }
//
//    // Bereken de maandelijkse lasten op basis van de lening en rente
//    public double berekenMaandlasten(double lening, int rentevastePeriode, int looptijd) {
//        double rente = krijgRente(rentevastePeriode) / 100;
//        int maanden = looptijd * 12;
//
//        // Gebruik de annuïtaire formule: maandlasten = lening * (rente / (1 - (1 + rente)^-n))
//        return lening * (rente / (1 - Math.pow(1 + rente, -maanden)));
//    }
//
//    // Krijg het rentepercentage op basis van de rentevaste periode
//    public double krijgRente(int jaren) {
//        switch (jaren) {
//            case 1:
//                return rentePercentages[0];
//            case 5:
//                return rentePercentages[1];
//            case 10:
//                return rentePercentages[2];
//            case 20:
//                return rentePercentages[3];
//            case 30:
//                return rentePercentages[4];
//            default:
//                return -1; // Ongeldige periode
//        }
//    }
//
//    // Controleer of de postcode in een verboden gebied ligt
//    public boolean isVerbodenPostcode(int postcode) {
//        for (int verboden : verbodenPostcodes) {
//            if (postcode == verboden) {
//                return true;
//            }
//        }
//        return false;
//    }
//}
