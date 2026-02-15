package test.java.frontend.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;

import main.java.backend.model.DatoGTF;
import main.java.backend.service.TransitServiceImpl.WrapperGenerico;
import main.java.frontend.rightpanel.panels.EntitiesPanelFactory;
import main.java.frontend.utilities.ListToScrollConverter;

@ExtendWith(MockitoExtension.class)
/**
 * (GENERATA DA AI)
 */
class ListToScrollConverterTest {

    @Mock
    private DatoGTF mockDatoGTF;

    @Mock
    private WrapperGenerico mockWrapper;

    // --- TEST PER setContent ---

    @Test
    @DisplayName("setContent: Restituisce uno JScrollPane configurato correttamente con una lista di pannelli valida")
    void testSetContent_ValidList() {
        // Arrange
        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        List<JPanel> panels = List.of(p1, p2);

        // Act
        JScrollPane result = ListToScrollConverter.setContent(panels);

        // Assert
        assertNotNull(result, "Lo JScrollPane non dovrebbe essere null");
        
        // Verifica delle impostazioni di scorrimento
        assertEquals(16, result.getVerticalScrollBar().getUnitIncrement());
        assertEquals(16, result.getHorizontalScrollBar().getUnitIncrement());
        
        // Verifica che i pannelli siano stati aggiunti al container interno
        JViewport viewport = result.getViewport();
        assertNotNull(viewport.getView());
        assertTrue(viewport.getView() instanceof JPanel);
        
        JPanel container = (JPanel) viewport.getView();
        assertEquals(2, container.getComponentCount(), "Il container dovrebbe contenere 2 pannelli");
    }

    @Test
    @DisplayName("Edge Case - setContent: Restituisce null se la lista è vuota")
    void testSetContent_EmptyList() {
        // Arrange
        List<JPanel> panels = new ArrayList<>();

        // Act
        JScrollPane result = ListToScrollConverter.setContent(panels);

        // Assert
        assertNull(result, "Se la lista è vuota, il metodo deve ritornare null");
    }

    @Test
    @DisplayName("Edge Case - setContent: Lancia NullPointerException se la lista è null")
    void testSetContent_NullList() {
        // Act & Assert
        // Il metodo originale fa panels.isEmpty() senza check null, quindi ci aspettiamo NPE
        assertThrows(NullPointerException.class, () -> {
            ListToScrollConverter.setContent(null);
        });
    }

    // --- TEST PER convertList ---

    @Test
    @DisplayName("convertList: Converte una lista di DatoGTF in JPanel")
    void testConvertList_ValidInput() {
        /* * NOTA: Poiché il metodo originale fa `new EntitiesPanelFactory()` internamente,
         * dobbiamo mockare la costruzione dell'oggetto o assicurarci che EntitiesPanelFactory
         * funzioni in ambiente di test. Qui usiamo `mockConstruction` di Mockito (disponibile nelle versioni recenti)
         * per intercettare il "new".
         */
        
        try (MockedConstruction<EntitiesPanelFactory> mockedFactory = mockConstruction(EntitiesPanelFactory.class,
                (mock, context) -> {
                    // Quando viene chiamato createPanel, restituisci un JPanel vuoto
                    when(mock.createPanel(any(DatoGTF.class))).thenReturn(new JPanel());
                })) {

            // Arrange
            List<DatoGTF> inputList = List.of(mockDatoGTF, mockDatoGTF);

            // Act
            List<JPanel> result = ListToScrollConverter.convertList(inputList);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals(1, mockedFactory.constructed().size(), "Deve essere stata istanziata una factory");
        }
    }

    @Test
    @DisplayName("Edge Case - convertList: Restituisce lista vuota se input vuoto")
    void testConvertList_EmptyInput() {
        // Arrange
        List<DatoGTF> inputList = Collections.emptyList();

        // Act
        // Anche se non mockiamo la Factory, il loop non parte, quindi non dovrebbe esplodere
        List<JPanel> result = ListToScrollConverter.convertList(inputList);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // --- TEST PER convertSearchedList ---

    @Test
    @DisplayName("convertSearchedList: Converte una lista di WrapperGenerico in JPanel")
    void testConvertSearchedList_ValidInput() {
        
        // Simile a sopra, intercettiamo il costruttore di EntitiesPanelFactory
        try (MockedConstruction<EntitiesPanelFactory> mockedFactory = mockConstruction(EntitiesPanelFactory.class,
                (mock, context) -> {
                    when(mock.createPanel(any(WrapperGenerico.class))).thenReturn(new JPanel());
                })) {

            // Arrange
            List<WrapperGenerico> inputList = List.of(mockWrapper);

            // Act
            List<JPanel> result = ListToScrollConverter.convertSearchedList(inputList);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }
}
