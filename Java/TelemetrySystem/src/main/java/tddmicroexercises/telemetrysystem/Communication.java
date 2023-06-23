package tddmicroexercises.telemetrysystem;

public interface Communication {
    public void send(String message);
    String receive();
}
