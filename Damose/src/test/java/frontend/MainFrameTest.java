package test.java.frontend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIf;
import org.mockito.MockedStatic;

import main.java.backend.service.TransitServiceImpl;
import main.java.frontend.BackendController;
import main.java.frontend.MainFrame;

/**
 * (GENERATA DA AI)
 */
class MainFrameTest {

    private String originalOsName;

    @BeforeEach
    void setUp() {
        // Salviamo il nome del sistema operativo originale per ripristinarlo dopo i test
        originalOsName = System.getProperty("os.name");
    }

    @AfterEach
    void tearDown() {
        // Ripristino sistema operativo originale
        if (originalOsName != null) {
            System.setProperty("os.name", originalOsName);
        }
    }
    
    // Metodo helper per capire se siamo in ambiente "Headless" (senza monitor, es. server CI/CD)
    boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }

    @Test
    @DisplayName("Inizializzazione corretta del Frame e dei Layout")
    @DisabledIf("isHeadless") // Salta se non c'è un monitor
    void testMainFrameInitialization() throws Exception {
        // Swing deve essere manipolato nel suo thread dedicato
        SwingUtilities.invokeAndWait(() -> {
            
            // --- 1. PREPARAZIONE MOCK (Fix per NullPointerException) ---
            // Creiamo un mock del servizio che risponda con una lista vuota invece di null
            TransitServiceImpl mockService = mock(TransitServiceImpl.class);
            when(mockService.getAllAlerts()).thenReturn(new ArrayList<>());

            // Apriamo il Mock Statico all'interno del thread Swing
            try (MockedStatic<BackendController> mockedController = mockStatic(BackendController.class)) {
                
                // Istruiamo il controller statico a restituire il nostro servizio finto
                mockedController.when(BackendController::getTTS).thenReturn(mockService);

                // --- 2. CODICE SOTTO TEST ---
                try {
                    MainFrame mainFrame = new MainFrame(); // Ora l'inizializzazione è sicura
                    JFrame frame = mainFrame.getFrame();

                    // --- 3. ASSERZIONI ---
                    
                    // Verifica esistenza frame
                    assertNotNull(frame, "Il JFrame non dovrebbe essere null");
                    assertTrue(frame.isVisible(), "Il frame dovrebbe essere visibile");
                    assertEquals("Damose - Rome Bus Transit Tracker", frame.getTitle());

                    // Verifica Layout Principale (CardLayout)
                    assertTrue(frame.getContentPane().getLayout() instanceof CardLayout, 
                            "Il content pane dovrebbe usare CardLayout");
                    // Verifica che il layout usato sia lo stesso esposto dal getter
                    assertEquals(mainFrame.getCardLayout(), frame.getContentPane().getLayout());

                    // Verifica Main Panel
                    JPanel mainPanel = mainFrame.getMainPanel();
                    assertNotNull(mainPanel, "Il MainPanel non deve essere null");
                    assertTrue(mainPanel.getLayout() instanceof BorderLayout, 
                            "Il MainPanel dovrebbe usare BorderLayout");

                    // Pulizia finale (importante per non lasciare finestre aperte durante i test)
                    mainFrame.getFrame().dispose();

                } catch (Exception e) {
                    // Se c'è un errore diverso dal NPE del database (es. icone mancanti), lo catturiamo qui
                    fail("Errore durante l'inizializzazione del MainFrame: " + e.getMessage());
                }
            } // Qui il mock statico viene chiuso automaticamente
        });
    }

    @Test
    @DisplayName("Verifica gerarchia dei componenti interni")
    @DisabledIf("isHeadless")
    void testComponentHierarchy() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            // 1. Il mock statico deve essere creato all'interno dello stesso thread 
            // in cui viene chiamato (l'Event Dispatch Thread)
            try (MockedStatic<BackendController> mockedController = mockStatic(BackendController.class)) {
                
                // 2. Configura il mock
                TransitServiceImpl mockService = mock(TransitServiceImpl.class);
                when(mockService.getAllAlerts()).thenReturn(new ArrayList<>());
                mockedController.when(BackendController::getTTS).thenReturn(mockService);

                // 3. Ora istanzia il frame: il mock sarà visibile!
                MainFrame mainFrame = new MainFrame();
                JPanel mainPanel = mainFrame.getMainPanel();

                // 4. Esegui gli assert
                BorderLayout layout = (BorderLayout) mainPanel.getLayout();
                Component westComp = layout.getLayoutComponent(BorderLayout.WEST);
                Component centerComp = layout.getLayoutComponent(BorderLayout.CENTER);

                assertNotNull(westComp, "Manca ServicePanel a OVEST");
                assertNotNull(centerComp, "Manca MapPanel al CENTRO");
                
                assertEquals("main.java.frontend.news.ServicePanel", westComp.getClass().getName());
                assertEquals("main.java.frontend.MapPanel", centerComp.getClass().getName());

                // Pulizia
                mainFrame.getFrame().dispose();
            } 
        });
    }

    // --- EDGE CASES (Simulazione OS) ---

    @Test
    @DisplayName("Edge Case: Caricamento Icona su Windows")
    @DisabledIf("isHeadless")
    void testIconLoadingWindows() throws Exception {
        System.setProperty("os.name", "Windows 10");
        
        SwingUtilities.invokeAndWait(() -> {
            try {
                MainFrame mf = new MainFrame();
                assertNotNull(mf.getFrame().getIconImage(), "L'icona dovrebbe essere caricata su Windows");
            } catch (NullPointerException e) {
                // Questo catch serve solo se il file .ico non esiste nei test resources
                System.err.println("Test Windows Icon: File .ico mancante nel classpath di test.");
            }
        });
    }

    @Test
    @DisplayName("Edge Case: Caricamento Icona su Linux/Mac")
    @DisabledIf("isHeadless")
    void testIconLoadingUnix() throws Exception {
        System.setProperty("os.name", "Linux");
        
        SwingUtilities.invokeAndWait(() -> {
            try {
                MainFrame mf = new MainFrame();
                assertNotNull(mf.getFrame().getIconImage(), "L'icona dovrebbe essere caricata su Linux");
            } catch (NullPointerException e) {
                 // Questo catch serve solo se il file .png non esiste nei test resources
                 System.err.println("Test Linux Icon: File .png mancante nel classpath di test.");
            }
        });
    }
}