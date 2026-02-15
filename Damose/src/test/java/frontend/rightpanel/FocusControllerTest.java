package test.java.frontend.rightpanel;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import javax.swing.JPanel;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.frontend.rightpanel.FocusController;
import main.java.frontend.rightpanel.FocusPanel;
import main.java.frontend.rightpanel.RightPanel;
//import main.java.frontend.rightpanel.StopFocus;
// Assumiamo che ci sia una classe FocusPanel o simile restituita da getFocus()
// Se non esiste, sostituisci con la classe reale o JPanel se generico.
import main.java.frontend.rightpanel.panels.StopFocus; 

@ExtendWith(MockitoExtension.class)
/**
 * (GENERATA DA AI)
 */
class FocusControllerTest {

    @Mock
    private RightPanel mockRightPanel;

    @Mock
    private StopFocus mockStopFocus;
    
    @Mock
    private FocusPanel mockCurrentFocusPanel; // Il pannello che ha il focus attuale

    @BeforeEach
    void setUp() {
        // Opzionale: Setup iniziale se comune a tutti i test
    }

    @AfterEach
    void tearDown() throws Exception {
        // PULIZIA FONDAMENTALE DELLO STATO STATICO
        // Dobbiamo assicurarci che rxPane sia null dopo ogni test
        // per evitare che un test influenzi l'altro.
        resetStaticRightPanel();
    }

    // Helper per resettare il campo statico privato
    private void resetStaticRightPanel() throws Exception {
        Field field = FocusController.class.getDeclaredField("rxPane");
        field.setAccessible(true);
        field.set(null, null);
    }

    // ========================================================================
    // TEST: setRightPanel & openFocus
    // ========================================================================

    @Test
    @DisplayName("setRightPanel: Imposta correttamente l'istanza e openFocus la utilizza")
    void testSetRightPanel_And_OpenFocus() {
        // Arrange
        FocusController.setRightPanel(mockRightPanel);

        // Act
        FocusController.openFocus(mockStopFocus);

        // Assert
        // Verifica che la chiamata sia stata delegata al RightPanel mockato
        verify(mockRightPanel).openFocusPanel(mockStopFocus);
    }

    @Test
    @DisplayName("setRightPanel: Sovrascrittura istanza esistente")
    void testSetRightPanel_Overwrite() {
        // Arrange
        RightPanel firstMock = mock(RightPanel.class);
        RightPanel secondMock = mock(RightPanel.class);

        // Act
        FocusController.setRightPanel(firstMock);
        FocusController.setRightPanel(secondMock); // Sovrascrive
        
        FocusController.openFocus(mockStopFocus);

        // Assert
        // Deve aver chiamato solo il secondo mock
        verify(secondMock).openFocusPanel(mockStopFocus);
        // Assicuriamoci che non abbia interagito col primo dopo il reset
        verify(firstMock, org.mockito.Mockito.never()).openFocusPanel(any());
    }

    // ========================================================================
    // TEST: openPrevious (Catena di chiamate)
    // ========================================================================

    @Test
    @DisplayName("openPrevious: Recupera il pannello precedente e lo imposta")
    void testOpenPrevious_Success() {
        // Arrange
        JPanel mockPreviousPanel = mock(JPanel.class); // Il pannello da ripristinare

        // Simuliamo la catena: rxPane.getFocus().getPrevious()
        // 1. rxPane.getFocus() ritorna mockCurrentFocusPanel
        when(mockRightPanel.getFocus()).thenReturn(mockCurrentFocusPanel);
        // 2. mockCurrentFocusPanel.getPrevious() ritorna mockPreviousPanel
        when(mockCurrentFocusPanel.getPrevious()).thenReturn(mockPreviousPanel);

        FocusController.setRightPanel(mockRightPanel);

        // Act
        FocusController.openPrevious();

        // Assert
        // Verifica che venga chiamato rxPane.setAndShowCurrent con il pannello recuperato
        verify(mockRightPanel).setAndShowCurrent(mockPreviousPanel);
    }

    // ========================================================================
    // EDGE CASES: Null Pointer Exceptions
    // ========================================================================

    @Test
    @DisplayName("Edge Case: openFocus lancia NPE se RightPanel non è settato")
    void testOpenFocus_WithoutSettingPanel_ThrowsNPE() throws Exception {
        // Arrange
        resetStaticRightPanel(); // Assicuriamoci che sia null

        // Act & Assert
        // Poiché rxPane è null, rxPane.openFocusPanel(...) lancerà NullPointerException
        assertThrows(NullPointerException.class, () -> {
            FocusController.openFocus(mockStopFocus);
        });
    }

    @Test
    @DisplayName("Edge Case: openPrevious lancia NPE se RightPanel non è settato")
    void testOpenPrevious_WithoutSettingPanel_ThrowsNPE() throws Exception {
        // Arrange
        resetStaticRightPanel();

        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            FocusController.openPrevious();
        });
    }
    
    @Test
    @DisplayName("Edge Case: setRightPanel(null) è permesso e resetta il campo")
    void testSetRightPanel_Null() {
        // Arrange
        FocusController.setRightPanel(mockRightPanel);
        
        // Act
        FocusController.setRightPanel(null);

        // Assert
        // Se ora chiamo openFocus, deve lanciare NPE perché ho settato null
        assertThrows(NullPointerException.class, () -> {
            FocusController.openFocus(mockStopFocus);
        });
    }
}