package test.java.frontend.rightpanel.panels;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import javax.swing.JPanel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.backend.model.DatoGTF;
import main.java.backend.model.Fermata;
import main.java.backend.model.Linea;
import main.java.backend.model.PredizioneArrivo;
import main.java.backend.model.RisultatoLinea;
import main.java.backend.service.TransitServiceImpl.WrapperGenerico;
import main.java.frontend.rightpanel.panels.ArrivingVehiclePanel;
import main.java.frontend.rightpanel.panels.EntitiesPanelFactory;
import main.java.frontend.rightpanel.panels.LinePanel;
import main.java.frontend.rightpanel.panels.StopPanel;

@ExtendWith(MockitoExtension.class)
/**
 * (GENERATA DA AI)
 */
class EntitiesPanelFactoryTest {

    private EntitiesPanelFactory factory;

    @Mock
    private Fermata mockFermata;
    @Mock
    private RisultatoLinea mockLinea;
    @Mock
    private PredizioneArrivo mockPredizione;
    @Mock
    private WrapperGenerico mockWrapper;

    @BeforeEach
    void setUp() {
        factory = new EntitiesPanelFactory();
    }

    // ========================================================================
    // TEST: createPanel(DatoGTF)
    // ========================================================================

    @Test
    @DisplayName("DatoGTF: Se input è Fermata -> ritorna StopPanel")
    void testCreatePanel_DatoGTF_Fermata() {
        // Usiamo MockedConstruction per evitare che venga eseguito il vero costruttore di StopPanel
        try (MockedConstruction<StopPanel> mocked = mockConstruction(StopPanel.class)) {
            
            // Act
            JPanel result = factory.createPanel(mockFermata);

            // Assert
            assertNotNull(result);
            // Verifica che l'istanza creata sia quella che ci aspettiamo
            assertEquals(1, mocked.constructed().size());
            assertTrue(result instanceof StopPanel);
        }
    }

    @Test
    @DisplayName("DatoGTF: Se input è Linea -> ritorna LinePanel")
    void testCreatePanel_DatoGTF_Linea() {
        try (MockedConstruction<LinePanel> mocked = mockConstruction(LinePanel.class)) {
            
            // Act
            JPanel result = factory.createPanel(mockLinea);

            // Assert
            assertNotNull(result);
            assertEquals(1, mocked.constructed().size());
            assertTrue(result instanceof LinePanel);
        }
    }

    @Test
    @DisplayName("DatoGTF: Se input è PredizioneArrivo -> ritorna ArrivingVehiclePanel")
    void testCreatePanel_DatoGTF_PredizioneArrivo() {
        // Questo è critico perché ArrivingVehiclePanel lanciava timer nel costruttore
        try (MockedConstruction<ArrivingVehiclePanel> mocked = mockConstruction(ArrivingVehiclePanel.class)) {
            
            // Act
            JPanel result = factory.createPanel(mockPredizione);

            // Assert
            assertNotNull(result);
            assertEquals(1, mocked.constructed().size());
            assertTrue(result instanceof ArrivingVehiclePanel);
        }
    }

    // ========================================================================
    // TEST: createPanel(WrapperGenerico)
    // ========================================================================

    @Test
    @DisplayName("Wrapper: Se Type è 'Fermata' -> ritorna StopPanel")
    void testCreatePanel_Wrapper_Fermata() {
        // Arrange
        // ATTENZIONE: Il codice sorgente usa '==' per le stringhe, quindi dobbiamo usare la stringa literal
        when(mockWrapper.getType()).thenReturn("Fermata");
        when(mockWrapper.getItem()).thenReturn(mockFermata);

        try (MockedConstruction<StopPanel> mocked = mockConstruction(StopPanel.class)) {
            // Act
            JPanel result = factory.createPanel(mockWrapper);

            // Assert
            assertNotNull(result);
            assertTrue(result instanceof StopPanel);
        }
    }

    @Test
    @DisplayName("Wrapper: Se Type è 'Linea' -> ritorna LinePanel")
    void testCreatePanel_Wrapper_Linea() {
        // Arrange
        when(mockWrapper.getType()).thenReturn("Linea");
        when(mockWrapper.getItem()).thenReturn(mockLinea);

        try (MockedConstruction<LinePanel> mocked = mockConstruction(LinePanel.class)) {
            // Act
            JPanel result = factory.createPanel(mockWrapper);

            // Assert
            assertNotNull(result);
            assertTrue(result instanceof LinePanel);
        }
    }

    // ========================================================================
    // EDGE CASES
    // ========================================================================

    @Test
    @DisplayName("Edge Case: DatoGTF sconosciuto -> ritorna null")
    void testCreatePanel_DatoGTF_UnknownType() {
        // Arrange
        DatoGTF unknownType = mock(DatoGTF.class); // Un mock generico che non è ne Fermata ne Linea...

        // Act
        JPanel result = factory.createPanel(unknownType);

        // Assert
        assertNull(result, "Se il tipo non è gestito, deve tornare null");
    }

    @Test
    @DisplayName("Edge Case: Wrapper con Type sconosciuto -> ritorna null")
    void testCreatePanel_Wrapper_UnknownType() {
        // Arrange
        when(mockWrapper.getType()).thenReturn("Sconosciuto");

        // Act
        JPanel result = factory.createPanel(mockWrapper);

        // Assert
        assertNull(result);
    }
    
    @Test
    @DisplayName("Edge Case: Wrapper Type mismatch (ClassCastException)")
    void testCreatePanel_Wrapper_TypeMismatch() {
        // Arrange
        // Scenario: Il wrapper dice "Fermata", ma l'oggetto dentro è una "Linea"
        when(mockWrapper.getType()).thenReturn("Fermata");
        when(mockWrapper.getItem()).thenReturn(mockLinea); // Oggetto sbagliato

        // Act & Assert
        // Poiché il codice fa il cast esplicito (Fermata) wg.getItem(), deve lanciare eccezione
        assertThrows(ClassCastException.class, () -> {
            factory.createPanel(mockWrapper);
        });
    }

    @Test
    @DisplayName("Edge Case: Input null per DatoGTF -> ritorna null")
    void testCreatePanel_DatoGTF_NullInput() {
        // Act
        JPanel result = factory.createPanel((DatoGTF) null);

        // Assert
        // instanceof null restituisce false, quindi p rimane null
        assertNull(result);
    }

    @Test
    @DisplayName("Edge Case: Input null per WrapperGenerico -> lancia NPE")
    void testCreatePanel_Wrapper_NullInput() {
        // Act & Assert
        // wg.getType() lancerà NullPointerException se wg è null
        assertThrows(NullPointerException.class, () -> {
            factory.createPanel((WrapperGenerico) null);
        });
    }
}