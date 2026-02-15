package test.java.frontend.waypoints;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.WaypointPainter;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import main.java.backend.model.Linea;
import main.java.backend.model.PredizioneArrivo;
import main.java.backend.model.RisultatoLinea;
import main.java.backend.model.RouteType;
import main.java.backend.realtime.VehiclePositionInfo;
import main.java.backend.service.TransitServiceImpl;
import main.java.frontend.BackendController;
import main.java.frontend.MapPanel;
import main.java.frontend.waypoints.StopWaypoint;
import main.java.frontend.waypoints.VehicleWaypoint;
import main.java.frontend.waypoints.WaypointManager;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
/**
 * (GENERATA DA AI)
 */
class WaypointManagerTest {

    @Mock
    private MapPanel mapPanel;

    @Mock
    private JXMapViewer jxMapViewer;

    
    @Mock
    private TransitServiceImpl ttsMock;

    @BeforeEach
    void setUp() {
        // Configurazione base del mock MapPanel
        lenient().when(mapPanel.getMapViewer()).thenReturn(jxMapViewer);
        
        // Impostiamo il MapPanel nella classe statica
        WaypointManager.addMap(mapPanel);
    }

    @AfterEach
    void tearDown() throws Exception {
        // IMPORTANTE: Pulizia dello stato statico tra i test per evitare "inquinamento"
        resetStaticField("busPainter");
        resetStaticField("tramPainter");
        resetStaticField("metroPainter");
        resetStaticField("trainPainter");
        resetStaticField("stopPainter");
        
        // Resettiamo i waypoints nel painter
        WaypointPainter<StopWaypoint> stopPainter = getStaticField("stopPainter");
        stopPainter.setWaypoints(Collections.emptySet());
    }

    @Test
    @DisplayName("Setup: Dovrebbe configurare correttamente i painter sul JXMapViewer")
    void testSetUp() {
        // Act
        WaypointManager.setUp();

        // Assert
        verify(mapPanel, atLeastOnce()).getMapViewer();
        verify(jxMapViewer).setOverlayPainter(any(CompoundPainter.class));
    }

    @Test
    @DisplayName("PaintStop: Dovrebbe aggiungere un waypoint al stopPainter")
    void testPaintStop() throws Exception {
        // Arrange
        GeoPosition pos = new GeoPosition(45.0, 9.0);

        // Act
        WaypointManager.setUp(); // Assicura che i renderer siano inizializzati
        WaypointManager.paintStop(pos);

        // Assert - Usiamo reflection per verificare che il set interno non sia vuoto
        WaypointPainter<StopWaypoint> stopPainter = getStaticField("stopPainter");
        Set<StopWaypoint> waypoints = stopPainter.getWaypoints();
        
        assertNotNull(waypoints);
        assertEquals(1, waypoints.size());
        assertEquals(pos, waypoints.iterator().next().getPosition());
    }

    @Test
    @DisplayName("PaintVehicles: Flusso corretto per un BUS")
    void testPaintVehicles_Bus() throws Exception {
        // Aggiungiamo TransitServiceImpl al try-with-resources per gestire entrambi i mock statici
        try (MockedStatic<BackendController> mockedBackend = mockStatic(BackendController.class);
             MockedStatic<TransitServiceImpl> mockedTransit = mockStatic(TransitServiceImpl.class)) {
            
            // --- ARRANGE ---
            
            // 1. Setup BackendController
            mockedBackend.when(BackendController::getTTS).thenReturn(ttsMock);
            
            String routeId = "route1";
            String tripId = "trip1";
            
            // 2. Setup Predizione
            PredizioneArrivo predizione = mock(PredizioneArrivo.class);
            when(predizione.getRouteId()).thenReturn(routeId);
            when(predizione.getTripId()).thenReturn(tripId);

            // 3. Setup Linea e Tipo
            RisultatoLinea lineMock = mock(RisultatoLinea.class);
            Linea mockLinea = mock(Linea.class);
            
            // Configurazione base della linea
            when(lineMock.getLinea()).thenReturn(mockLinea);
            when(mockLinea.getType()).thenReturn(3);
            when(mockLinea.getRouteType()).thenReturn(RouteType.BUS); 
            // IMPORTANTE: Se WaypointManager usa altri metodi di Linea (es. getColor, isUrban), 
            // aggiungi qui i relativi when(...) per evitare NullPointerException.
            
            when(ttsMock.cercaLinee(routeId)).thenReturn(List.of(lineMock));

            // 4. Setup TransitServiceImpl (NUOVA PARTE)
            // Istruiamo il metodo statico a restituire la nostra linea mockata quando richiesto con routeId
            mockedTransit.when(() -> ttsMock.getLinea(routeId)).thenReturn(mockLinea);

            // 5. Setup della Posizione
            VehiclePositionInfo posMock = mock(VehiclePositionInfo.class);
            when(posMock.getLat()).thenReturn(45.0);
            when(posMock.getLon()).thenReturn(9.0);
            // Se necessario, mockare anche getBearing() o altri getter usati dal painter
            // when(posMock.getBearing()).thenReturn(0.0);
            
            when(ttsMock.getVehiclePositionForTripId(tripId)).thenReturn(Optional.of(posMock));

            Set<PredizioneArrivo> vehicles = Set.of(predizione);

            // --- ACT ---
            WaypointManager.setUp();
            WaypointManager.paintVehicles(vehicles);

            // --- ASSERT ---
            
            // Verifica che busPainter abbia 1 waypoint
            WaypointPainter<VehicleWaypoint> busPainter = getStaticField("busPainter");
            assertEquals(1, busPainter.getWaypoints().size(), "Dovrebbe esserci 1 bus da disegnare");
            
            // Verifica che tramPainter sia vuoto
            WaypointPainter<VehicleWaypoint> tramPainter = getStaticField("tramPainter");
            assertEquals(0, tramPainter.getWaypoints().size(), "Il tramPainter dovrebbe essere vuoto");

            // Verifica che sia stato chiamato repaint
            verify(jxMapViewer, atLeastOnce()).repaint();
        }
    }

    // --- EDGE CASES ---

    @Test
    @DisplayName("Edge Case: Lista veicoli vuota")
    void testPaintVehicles_EmptySet() {
        // Act
        WaypointManager.paintVehicles(Collections.emptySet());

        // Assert
        verify(jxMapViewer, atLeastOnce()).repaint();
        // Non dovrebbe lanciare eccezioni
    }

    @Test
    @DisplayName("Edge Case: Veicolo senza posizione (Optional.empty)")
    void testPaintVehicles_MissingPosition() throws Exception {
        // Inizializziamo ENTRAMBI i mock statici nel try-with-resources
        try (MockedStatic<BackendController> mockedBackend = mockStatic(BackendController.class);
             MockedStatic<TransitServiceImpl> mockedTransit = mockStatic(TransitServiceImpl.class)) {
            
            // --- ARRANGE ---
            
            // 1. Setup BackendController
            mockedBackend.when(BackendController::getTTS).thenReturn(ttsMock);
            
            // 2. Setup Oggetti Mock base
            PredizioneArrivo predizione = mock(PredizioneArrivo.class);
            when(predizione.getRouteId()).thenReturn("r1");
            when(predizione.getTripId()).thenReturn("t1");

            // Crea la linea mockata (questa è quella importante che non deve essere null)
            Linea mockLinea = mock(Linea.class);
            // IMPORTANTE: Assicurati che mockLinea risponda alle chiamate successive per evitare NPE dopo il getLinea
            when(mockLinea.getRouteType()).thenReturn(RouteType.BUS); 
            // Aggiungi qui eventuali altri metodi di Linea usati da WaypointManager (es. getColor, isUrban, ecc.)

            RisultatoLinea lineMock = mock(RisultatoLinea.class);
            when(lineMock.getLinea()).thenReturn(mockLinea);
            
            // 3. Setup TransitServiceImpl (La parte che mancava)
            // Quando viene chiesto getLinea("r1"), restituisci l'oggetto mockato sopra
            mockedTransit.when(() -> ttsMock.getLinea("r1")).thenReturn(mockLinea);

            // 4. Setup ttsMock
            when(ttsMock.cercaLinee("r1")).thenReturn(List.of(lineMock));
            // Simuliamo che la posizione non sia disponibile
            when(ttsMock.getVehiclePositionForTripId("t1")).thenReturn(Optional.empty());

            // --- ACT ---
            WaypointManager.paintVehicles(Set.of(predizione));

            // --- ASSERT ---
            WaypointPainter<VehicleWaypoint> busPainter = getStaticField("busPainter");
            assertEquals(0, busPainter.getWaypoints().size(), "Non dovrebbero essere creati waypoint se manca la posizione");
        }
    }
    
    

    // --- HELPER METHODS PER REFLECTION ---
    // Necessari perché i campi sono privati statici
    
    @SuppressWarnings("unchecked")
    private <T> T getStaticField(String fieldName) throws Exception {
        Field field = WaypointManager.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(null);
    }

    private void resetStaticField(String fieldName) throws Exception {
        Field field = WaypointManager.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        // Resetta creando una nuova istanza pulita
        field.set(null, new WaypointPainter<VehicleWaypoint>());
    }
}
