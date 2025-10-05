package fabricas;

import persistence.ConnectionFactory;
import services.Servicios;

public class FabricaServiciosExterna {

    /**
     * Creates a new instance of the Servicios class for a specific database type.
     * @param dbType The type of database to connect to (e.g., "h2", "mysql", "oracle").
     * @return A new Servicios instance configured for the specified database.
     */
    public static Servicios getServicios(String dbType) {
        // 1. Create a new ConnectionFactory based on the provided type
        ConnectionFactory connectionFactory = new ConnectionFactory(dbType);
        
        // 2. Return a new instance of Servicios using the factory
        return new Servicios(connectionFactory);
    }
}