import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HypotheekToolIntegrationTest {

    private HypotheekTool hypotheekTool;

    @BeforeEach
    public void setUp() {
        hypotheekTool = new HypotheekTool();
    }

    // Test the integration of start(), berekenHypotheekDetails(), and toonResultaten() with specific values
    @Test
    public void testFullIntegration_NoPartner_NoStudieschuld() {
        hypotheekTool.inkomenKlant = 60000;
        hypotheekTool.inkomenPartner = 0;
        hypotheekTool.heeftPartner = false;
        hypotheekTool.heeftStudieschuld = false;
        hypotheekTool.rentevastePeriode = 10;
        hypotheekTool.rentePercentage = hypotheekTool.getRentePercentage(hypotheekTool.rentevastePeriode);
        hypotheekTool.postcode = "1234";
        hypotheekTool.looptijd = 30;

        // Perform calculations
        hypotheekTool.berekenHypotheekDetails();
        String resultaat = hypotheekTool.toonResultaten();

        // Expected values based on the given inputs
        double expectedMaximaalTeLenenBedrag = 60000 * 4.25;
        double expectedMaandAflossing = expectedMaximaalTeLenenBedrag / (30 * 12);
        double expectedMaandRente = (expectedMaximaalTeLenenBedrag * (hypotheekTool.rentePercentage / 100)) / 12;
        double expectedMaandlasten = expectedMaandAflossing + expectedMaandRente;
        double expectedTotaleBetaling = expectedMaandlasten * 30 * 12;
        double expectedTotaleRente = expectedMaandRente * 30 * 12;

        // Assert calculations
        assertEquals(expectedMaximaalTeLenenBedrag, hypotheekTool.maximaalTeLenenBedrag, 0.01);
        assertEquals(expectedMaandAflossing, hypotheekTool.maandAflossing, 0.01);
        assertEquals(expectedMaandRente, hypotheekTool.maandRente, 0.01);
        assertEquals(expectedMaandlasten, hypotheekTool.maandlasten, 0.01);
        assertEquals(expectedTotaleBetaling, hypotheekTool.totaleBetaling, 0.01);
        assertEquals(expectedTotaleRente, hypotheekTool.totaleRente, 0.01);

        // Check if the output format is as expected
        assertTrue(resultaat.contains(String.format("Maximaal te lenen bedrag: €%.2f", expectedMaximaalTeLenenBedrag)));
    }

    @Test
    public void testFullIntegration_WithPartner_WithStudieschuld() {
        hypotheekTool.inkomenKlant = 50000;
        hypotheekTool.inkomenPartner = 30000;
        hypotheekTool.heeftPartner = true;
        hypotheekTool.heeftStudieschuld = true;
        hypotheekTool.rentevastePeriode = 20;
        hypotheekTool.rentePercentage = hypotheekTool.getRentePercentage(hypotheekTool.rentevastePeriode);
        hypotheekTool.postcode = "1234";
        hypotheekTool.looptijd = 30;

        // Perform calculations
        hypotheekTool.berekenHypotheekDetails();
        String resultaat = hypotheekTool.toonResultaten();

        // Expected values based on the given inputs
        double totaalInkomen = 50000 + 30000;
        double expectedMaximaalTeLenenBedrag = totaalInkomen * 4.25 * hypotheekTool.STUDIESCHULD_KORTING;
        double expectedMaandAflossing = expectedMaximaalTeLenenBedrag / (30 * 12);
        double expectedMaandRente = (expectedMaximaalTeLenenBedrag * (hypotheekTool.rentePercentage / 100)) / 12;
        double expectedMaandlasten = expectedMaandAflossing + expectedMaandRente;
        double expectedTotaleBetaling = expectedMaandlasten * 30 * 12;
        double expectedTotaleRente = expectedMaandRente * 30 * 12;

        // Assert calculations
        assertEquals(expectedMaximaalTeLenenBedrag, hypotheekTool.maximaalTeLenenBedrag, 0.01);
        assertEquals(expectedMaandAflossing, hypotheekTool.maandAflossing, 0.01);
        assertEquals(expectedMaandRente, hypotheekTool.maandRente, 0.01);
        assertEquals(expectedMaandlasten, hypotheekTool.maandlasten, 0.01);
        assertEquals(expectedTotaleBetaling, hypotheekTool.totaleBetaling, 0.01);
        assertEquals(expectedTotaleRente, hypotheekTool.totaleRente, 0.01);

        // Check if the output format is as expected
        assertTrue(resultaat.contains(String.format("Maximaal te lenen bedrag: €%.2f", expectedMaximaalTeLenenBedrag)));
    }

    @Test
    public void testFullIntegration_InvalidPostcode() {
        hypotheekTool.inkomenKlant = 60000;
        hypotheekTool.heeftStudieschuld = false;
        hypotheekTool.heeftPartner = false;
        hypotheekTool.rentevastePeriode = 5;
        hypotheekTool.rentePercentage = hypotheekTool.getRentePercentage(hypotheekTool.rentevastePeriode);

        // Set an invalid postcode
        hypotheekTool.postcode = "9679";  // In aardbevingsgebied

        // Perform calculations
        assertTrue(hypotheekTool.isInAardbevingsgebied(hypotheekTool.postcode));
    }
}
