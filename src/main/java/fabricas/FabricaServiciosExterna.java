package fabricas;

import persistence.adapter.DatabaseAdapter;
import persistence.adapter.DatabaseAdapterFactory;
import services.Servicios;

public class FabricaServiciosExterna {

    /**
     * Creates a new instance of the Servicios class for a specific database type.
     * @param dbType The type of database to connect to (e.g., "h2", "mysql", "oracle").
     * @return A new Servicios instance configured for the specified database.
     */
    public static Servicios getServicios(String dbType) {
        // 1. Create a new DatabaseAdapter using the factory
        DatabaseAdapter adapter = DatabaseAdapterFactory.createAdapter(dbType);
        
        // 2. Return a new instance of Servicios using the adapter
        return new Servicios(adapter);
    }
}