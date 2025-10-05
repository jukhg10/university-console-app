package fabricas;

import persistence.ConnectionFactory;
import persistence.DatabaseManager;
import services.Servicios;

import java.io.InputStream;
import java.util.Properties;

public class FabricaServiciosInterna {

    private static Servicios instance;

    static {
        try {
            // 1. Cargar el archivo de propiedades
            Properties props = new Properties();
            InputStream input = FabricaServiciosInterna.class.getClassLoader().getResourceAsStream("db.properties");
            if (input == null) {
                throw new IllegalStateException("El archivo db.properties no se encontró en el classpath");
            }
            props.load(input);

            // 2. Leer la propiedad 'db.active' para decidir qué base de datos usar
            String databaseType = props.getProperty("db.active");
            if (databaseType == null || databaseType.trim().isEmpty()) {
                throw new IllegalStateException("La propiedad 'db.active' no está configurada en db.properties");
            }

            // 3. Crear una ConnectionFactory para la base de datos seleccionada
            ConnectionFactory connectionFactory = new ConnectionFactory(databaseType);

            // 4. --- CORRECCIÓN IMPORTANTE ---
            // Inicializar (crear/recrear) el esquema de la base de datos SIEMPRE al iniciar.
            // Esto asegura que el código y la BD siempre estén sincronizados.
            System.out.println("Inicializando el esquema para la base de datos: " + databaseType);
            DatabaseManager.initializeDatabase(connectionFactory);
            
            // 5. Crear la instancia única de Servicios
            instance = new Servicios(connectionFactory);
            System.out.println("Servicios inicializados correctamente con la base de datos: " + databaseType);

        } catch (Exception e) {
            e.printStackTrace();
            // Esto previene que la aplicación inicie si la configuración es incorrecta
            throw new RuntimeException("Falló la inicialización de los servicios", e);
        }
    }

    /**
     * Obtiene la instancia única y compartida de la clase Servicios.
     * @return La instancia singleton de Servicios.
     */
    public static Servicios getServicios() {
        return instance;
    }
}