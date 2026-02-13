package test.frontend;

import main.java.backend.service.TransitServiceImpl;
import main.java.frontend.main.BackendController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BackendControllerTest {

    // 1. Resetta il campo statico 'tts' prima e dopo ogni test
    // Questo è fondamentale perché essendo statico, altrimenti sopravvive tra un test e l'altro
    @BeforeEach
    @AfterEach
    void resetSingleton() throws Exception {
        Field instance = BackendController.class.getDeclaredField("tts");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    @DisplayName("Successo: openTransit inizializza tts e controlla la connessione")
    void testOpenTransitSuccess() {
        // Mockiamo le classi statiche TransitServiceImpl e JOptionPane
        try (MockedStatic<TransitServiceImpl> transitStatic = mockStatic(TransitServiceImpl.class);
             MockedStatic<JOptionPane> optionPaneStatic = mockStatic(JOptionPane.class)) {

            // A. PREPARAZIONE (Arrange)
            TransitServiceImpl serviceMock = mock(TransitServiceImpl.class);
            when(serviceMock.isOnline()).thenReturn(true); // Simuliamo che sia online

            // Simuliamo che createDefault() restituisca il nostro mock
            transitStatic.when(TransitServiceImpl::createDefault).thenReturn(serviceMock);

            // B. ESECUZIONE (Act)
            BackendController.openTransit();

            // C. VERIFICA (Assert)
            // Verifichiamo che tts sia stato popolato
            assertThat(BackendController.getTTS()).isNotNull();
            assertThat(BackendController.getTTS()).isEqualTo(serviceMock);

            // Verifichiamo che non siano apparsi messaggi di errore (perché è online)
            optionPaneStatic.verify(() -> JOptionPane.showMessageDialog(any(), any()), never());
        }
    }

    @Test
    @DisplayName("Edge Case: IOException e Utente riprova (Sì)")
    void testOpenTransitRetry() {
        try (MockedStatic<TransitServiceImpl> transitStatic = mockStatic(TransitServiceImpl.class);
             MockedStatic<JOptionPane> optionPaneStatic = mockStatic(JOptionPane.class)) {

            TransitServiceImpl serviceMock = mock(TransitServiceImpl.class);
            when(serviceMock.isOnline()).thenReturn(true);

            // A. PREPARAZIONE:
            // La prima volta lancia eccezione, la seconda restituisce l'oggetto corretto
            transitStatic.when(TransitServiceImpl::createDefault)
                    .thenThrow(new IOException("Errore simulato")) // 1° chiamata
                    .thenReturn(serviceMock);                      // 2° chiamata

            // Simuliamo che l'utente clicchi "Sì" (0) nel dialog di conferma
            optionPaneStatic.when(() -> JOptionPane.showConfirmDialog(any(), anyString()))
                    .thenReturn(0); // 0 = YES_OPTION

            // B. ESECUZIONE
            BackendController.openTransit();

            // C. VERIFICA
            // Deve aver chiamato createDefault 2 volte (una fallita, una riuscita)
            transitStatic.verify(TransitServiceImpl::createDefault, times(2));
            assertThat(BackendController.getTTS()).isNotNull();
        }
    }

    @Test
    @DisplayName("Edge Case: Offline Warning")
    void testOfflineWarning() {
        try (MockedStatic<TransitServiceImpl> transitStatic = mockStatic(TransitServiceImpl.class);
             MockedStatic<JOptionPane> optionPaneStatic = mockStatic(JOptionPane.class)) {

            TransitServiceImpl serviceMock = mock(TransitServiceImpl.class);
            // Simuliamo che sia OFFLINE
            when(serviceMock.isOnline()).thenReturn(false);

            transitStatic.when(TransitServiceImpl::createDefault).thenReturn(serviceMock);

            // Act
            BackendController.openTransit();

            // Assert
            // Deve mostrare il messaggio "Sei offline!"
            optionPaneStatic.verify(() -> JOptionPane.showMessageDialog(isNull(), eq("Sei offline!")));
        }
    }

    @Test
    @DisplayName("Bug: IOException e Utente NON riprova -> Crash (NPE)")
    void testOpenTransitFailNoRetry() {
        try (MockedStatic<TransitServiceImpl> transitStatic = mockStatic(TransitServiceImpl.class);
             MockedStatic<JOptionPane> optionPaneStatic = mockStatic(JOptionPane.class)) {

            // Simuliamo eccezione
            transitStatic.when(TransitServiceImpl::createDefault)
                    .thenThrow(new IOException("Errore fatale"));

            // Simuliamo che l'utente clicchi "No" (1)
            optionPaneStatic.when(() -> JOptionPane.showConfirmDialog(any(), anyString()))
                    .thenReturn(1); // 1 = NO_OPTION

            // QUI IL TUO CODICE FALLIRÀ con NullPointerException
            // Perché dopo il catch prosegue su checkConnection() ma tts è null
            assertThrows(NullPointerException.class, () -> {
                BackendController.openTransit();
            });
        }
    }
    
    @Test
    @DisplayName("Singleton: Se già inizializzato, non ricrea l'istanza")
    void testSingletonBehavior() throws Exception {
         try (MockedStatic<TransitServiceImpl> transitStatic = mockStatic(TransitServiceImpl.class)) {
             // Setup manuale del campo tts per simulare che sia già pieno
             TransitServiceImpl existingService = mock(TransitServiceImpl.class);
             Field field = BackendController.class.getDeclaredField("tts");
             field.setAccessible(true);
             field.set(null, existingService);
             
             // Act
             BackendController.openTransit();
             
             // Assert
             // Non deve chiamare createDefault perché tts non era null
             transitStatic.verify(TransitServiceImpl::createDefault, never());
         }
    }
}