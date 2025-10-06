package fabricas;

import persistence.adapter.DatabaseAdapter;
import persistence.adapter.DatabaseAdapterFactory;
import services.Servicios;

import java.util.HashMap;
import java.util.Map;

public class FabricaServiciosExterna {

    private static final Map<String, Servicios> instances = new HashMap<>();

    private FabricaServiciosExterna() {
        // Private constructor to prevent instantiation
    }

    /**
     * Returns a singleton instance of the Servicios class for a specific database type.
     * @param dbType The type of database to connect to (e.g., "h2", "mysql", "oracle").
     * @return A singleton Servicios instance configured for the specified database.
     */
    public static synchronized Servicios getServicios(String dbType) {
        if (!instances.containsKey(dbType)) {
            // 1. Create a new DatabaseAdapter using the factory
            DatabaseAdapter adapter = DatabaseAdapterFactory.createAdapter(dbType);

            // 2. Create a new instance of Servicios using the adapter and store it
            instances.put(dbType, new Servicios(adapter));
        }
        return instances.get(dbType);
    }
}