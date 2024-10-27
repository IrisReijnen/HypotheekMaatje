import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class HypotheekToolUnitTest {

    @InjectMocks
    private HypotheekTool hypotheekTool;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        hypotheekTool = spy(new HypotheekTool()); // Use spy to mock individual methods as needed
    }

    // Test berekenMaximaalTeLenenBedrag without dependencies
    @Test
    public void testBerekenMaximaalTeLenenBedrag_ZonderPartner_ZonderStudieschuld() {
        hypotheekTool.inkomenKlant = 60000;
        hypotheekTool.inkomenPartner = 0;
        hypotheekTool.heeftStudieschuld = false;

        double verwachtMaximaalTeLenenBedrag = 60000 * 4.25;
        double resultaat = hypotheekTool.berekenMaximaalTeLenenBedrag();
        assertEquals(verwachtMaximaalTeLenenBedrag, resultaat, 0.01);
    }

    @Test
    public void testBerekenMaximaalTeLenenBedrag_MetPartner_MetStudieschuld() {
        hypotheekTool.inkomenKlant = 40000;
        hypotheekTool.inkomenPartner = 20000;
        hypotheekTool.heeftStudieschuld = true;

        double verwachtMaximaalTeLenenBedrag = (40000 + 20000) * 4.25 * 0.75;
        double resultaat = hypotheekTool.berekenMaximaalTeLenenBedrag();
        assertEquals(verwachtMaximaalTeLenenBedrag, resultaat, 0.01);
    }

    // Test berekenMaandlasten while mocking berekenMaximaalTeLenenBedrag
    @Test
    public void testBerekenMaandlasten() {
        double lening = 100000;
        hypotheekTool.rentePercentage = 2.0;
        hypotheekTool.looptijd = 30;

        when(hypotheekTool.berekenMaximaalTeLenenBedrag()).thenReturn(lening);

        double verwachtMaandlasten = (lening / (hypotheekTool.looptijd * 12)) + ((lening * (2.0 / 100)) / 12);
        hypotheekTool.berekenHypotheekDetails();
        double resultaat = hypotheekTool.maandlasten;
        assertEquals(verwachtMaandlasten, resultaat, 0.01);
    }

    // Test getRentePercentage without dependencies
    @Test
    public void testGetRentePercentage_ValidPeriode() {
        assertEquals(2.0, hypotheekTool.getRentePercentage(1));
        assertEquals(3.0, hypotheekTool.getRentePercentage(5));
        assertEquals(3.5, hypotheekTool.getRentePercentage(10));
        assertEquals(4.5, hypotheekTool.getRentePercentage(20));
        assertEquals(5.0, hypotheekTool.getRentePercentage(30));
    }

    @Test
    public void testGetRentePercentage_InvalidPeriode() {
        assertEquals(-1, hypotheekTool.getRentePercentage(15));
    }

    // Test isInAardbevingsgebied without dependencies
    @Test
    public void testIsInAardbevingsgebied() {
        assertTrue(hypotheekTool.isInAardbevingsgebied("9679"));
        assertTrue(hypotheekTool.isInAardbevingsgebied("9681"));
        assertTrue(hypotheekTool.isInAardbevingsgebied("9682"));

        assertFalse(hypotheekTool.isInAardbevingsgebied("1234"));
        assertFalse(hypotheekTool.isInAardbevingsgebied("5678"));
    }

    // Test berekenHypotheekDetails while mocking berekenMaximaalTeLenenBedrag
    @Test
    public void testBerekenHypotheekDetails() {
        double verwachtMaximaalTeLenenBedrag = 60000 * 4.25;
        when(hypotheekTool.berekenMaximaalTeLenenBedrag()).thenReturn(verwachtMaximaalTeLenenBedrag);

        hypotheekTool.rentePercentage = 5.0;
        hypotheekTool.looptijd = 30;

        double verwachtMaandAflossing = verwachtMaximaalTeLenenBedrag / (hypotheekTool.looptijd * 12);
        double verwachtMaandRente = (verwachtMaximaalTeLenenBedrag * 5.0 / 100) / 12;
        double verwachtMaandlasten = verwachtMaandAflossing + verwachtMaandRente;

        hypotheekTool.berekenHypotheekDetails();
        assertEquals(verwachtMaximaalTeLenenBedrag, hypotheekTool.maximaalTeLenenBedrag, 0.01);
        assertEquals(verwachtMaandAflossing, hypotheekTool.maandAflossing, 0.01);
        assertEquals(verwachtMaandRente, hypotheekTool.maandRente, 0.01);
        assertEquals(verwachtMaandlasten, hypotheekTool.maandlasten, 0.01);
    }

    // Test toonResultaten with mock values only (no dependency)
    @Test
    public void testToonResultaten() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        hypotheekTool.maximaalTeLenenBedrag = 255000;
        hypotheekTool.maandRente = 1062.50;
        hypotheekTool.maandAflossing = 708.33;
        hypotheekTool.maandlasten = 1770.83;
        hypotheekTool.totaleRente = 382498.80;
        hypotheekTool.totaleBetaling = 637498.80;
        hypotheekTool.looptijd = 30;

        hypotheekTool.toonResultaten();

        String expectedOutput = String.format(
                "Maximaal te lenen bedrag: €%.2f%n" +
                        "Maandelijkse rente: €%.2f%n" +
                        "Maandelijkse aflossing: €%.2f%n" +
                        "Totale maandlasten: €%.2f%n" +
                        "Totale te betalen rente over de gehele looptijd: €%.2f%n" +
                        "Totale kosten over de looptijd van %d jaar: €%.2f%n",
                255000.00, 1062.50, 708.33, 1770.83, 382498.80, 30, 637498.80
        );

        assertEquals(expectedOutput, outContent.toString());
    }
}
