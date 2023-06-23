package tddmicroexercises.telemetrysystem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class TelemetryDiagnosticControlsTest {
    @Mock
    private Communication mockTelemetryClient;
    @Mock
    private Connection mockConnection;

    private TelemetryDiagnosticControls controls;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        controls = new TelemetryDiagnosticControls();
        controls.setTelemetryClient(mockTelemetryClient);
        controls.setConnection(mockConnection);
    }

    @Test
    public void testCheckTransmission_SuccessfulConnection() throws Exception {
        when(mockConnection.getOnlineStatus()).thenReturn(true);
        when(mockTelemetryClient.receive()).thenReturn("Mock diagnostic info");
        controls.checkTransmission();

        String actualDiagnosticInfo = controls.getDiagnosticInfo();
        String expectedDiagnosticInfo = "Mock diagnostic info";
        assertEquals(expectedDiagnosticInfo, actualDiagnosticInfo);

        verify(mockConnection).disconnect();
        verify(mockConnection, times(1)).getOnlineStatus();
        verify(mockConnection, times(1)).connect("*111#");
        verify(mockTelemetryClient).send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        verify(mockTelemetryClient).receive();
    }

    @Test
    public void testCheckTransmission_FailedConnection() throws Exception {
        when(mockConnection.getOnlineStatus()).thenReturn(false);

        try {
            controls.checkTransmission();
        } catch (Exception e) {
            verify(mockConnection).disconnect();
            verify(mockConnection, times(3)).getOnlineStatus();
            verify(mockConnection, times(3)).connect("*111#");
            verify(mockTelemetryClient, never()).send(anyString());
            verify(mockTelemetryClient, never()).receive();

            String expectedErrorMessage = "Unable to connect.";
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}
