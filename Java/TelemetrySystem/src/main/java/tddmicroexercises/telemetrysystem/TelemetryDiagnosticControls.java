package tddmicroexercises.telemetrysystem;

public class TelemetryDiagnosticControls implements TransmissionChecker
{
    private final String DiagnosticChannelConnectionString = "*111#";
    private Communication telemetryClient;
    private Connection connection;
    public void setTelemetryClient(Communication telemetryClient){
        this.telemetryClient=telemetryClient;
    }
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    private String diagnosticInfo = "";
    public String getDiagnosticInfo(){
        return diagnosticInfo;
    }
    public void setDiagnosticInfo(String diagnosticInfo){

        this.diagnosticInfo = diagnosticInfo;
    }
    @Override
    public void checkTransmission() throws Exception
    {
        diagnosticInfo = "";

        connection.disconnect();

        int retryLeft = 3;
        while (connection.getOnlineStatus() == false && retryLeft > 0)
        {
            connection.connect(DiagnosticChannelConnectionString);
            retryLeft -= 1;
        }

        if(connection.getOnlineStatus() == false)
        {
            throw new Exception("Unable to connect.");
        }

        telemetryClient.send(TelemetryClient.DIAGNOSTIC_MESSAGE);
        diagnosticInfo = telemetryClient.receive();
    }
}
