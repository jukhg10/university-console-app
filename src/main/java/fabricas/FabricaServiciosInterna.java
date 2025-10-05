// src/main/java/fabricas/FabricaServiciosInterna.java
package fabricas;

import persistence.DatabaseManager;
import persistence.adapter.DatabaseAdapter;
import persistence.adapter.DatabaseAdapterFactory;
import services.Servicios;

import java.io.InputStream;
import java.util.Properties;

public class FabricaServiciosInterna {

    private static Servicios instance;

    static {
        try {
            Properties props = new Properties();
            InputStream input = FabricaServiciosInterna.class.getClassLoader().getResourceAsStream("db.properties");
            if (input == null) {
                throw new IllegalStateException("El archivo db.properties no se encontró en el classpath");
            }
            props.load(input);

            String databaseType = props.getProperty("db.active");
            if (databaseType == null || databaseType.trim().isEmpty()) {
                throw new IllegalStateException("La propiedad 'db.active' no está configurada en db.properties");
            }

            // Usamos la fábrica de adaptadores
            DatabaseAdapter adapter = DatabaseAdapterFactory.createAdapter(databaseType);

            System.out.println("Inicializando el esquema para la base de datos: " + databaseType);
            DatabaseManager.initializeDatabase(adapter);
            
            // Pasamos el adaptador a los servicios
            instance = new Servicios(adapter);
            System.out.println("Servicios inicializados correctamente con la base de datos: " + databaseType);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Falló la inicialización de los servicios", e);
        }
    }

    public static Servicios getServicios() {
        return instance;
    }
}