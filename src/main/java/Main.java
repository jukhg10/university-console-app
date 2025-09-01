import model.Curso;
import model.Estudiante;
import model.Inscripcion;
import model.Persona;
import persistence.DatabaseManager;
import services.CursosInscritos;
import services.InscripcionesPersonas;

import java.util.Scanner;
public class Main {
    public static void main(String[] args) {

        System.out.println("Iniciando aplicación...");
        DatabaseManager.initializeDatabase();

        CursosInscritos servicio = new CursosInscritos();
        servicio.cargarDatos();
        System.out.println("Datos cargados. " + servicio.cantidadActual() + " inscripciones existentes.");

        System.out.println("Registrando nueva inscripcion con datos: Estudiante 102, Curso 901, Año 2025, Semestre 2 ");
        Estudiante est = new Estudiante(1, "Estudiante", "Temp", "correo@gmail", 102, true, 3.2);
        Curso cur = new Curso(901, "Curso Temp", true);
        Inscripcion nuevaInscripcion = new Inscripcion(est, cur, 2025, 2);
        servicio.inscribirCurso(nuevaInscripcion);
        servicio.cargarDatos();
        System.out.println("¡Inscripción registrada con éxito!");
        System.out.println("Datos cargados. " + servicio.cantidadActual() + " inscripciones existentes.");

        System.out.println("\n --Listando las inscripciones:");
        if (servicio.cantidadActual() == 0) {
            System.out.println("No hay inscripciones para mostrar.");
        } else {
            for (int i = 0; i < servicio.cantidadActual(); i++) {
                System.out.println(servicio.imprimirPosicion(i));
            }
        }

        System.out.println("\n-- Eliminar Inscripción --");
        int idNueva = nuevaInscripcion.getId();
        System.out.println("Eliminando inscripción con ID: " + idNueva);
        servicio.eliminar(idNueva);
        servicio.cargarDatos();

        System.out.println("\n --Listando las inscripciones luego de eliminar:");
        if (servicio.cantidadActual() == 0) {
            System.out.println("No hay inscripciones para mostrar.");
        } else {
            for (int i = 0; i < servicio.cantidadActual(); i++) {
                System.out.println(servicio.imprimirPosicion(i));
            }
        }

        // === GESTIÓN DE PERSONAS ===
        InscripcionesPersonas servicioPersona = new InscripcionesPersonas();
        servicioPersona.cargarDatos(); // ← corregido: era servicio, ahora servicioPersona

        Persona p1 = new Persona(1, "Ana", "Gómez", "ana@email.com");
        Persona p2 = new Persona(2, "Luis", "Mendoza", "luis@email.com");

        servicioPersona.inscribir(p1);
        servicioPersona.inscribir(p2);

        Persona p1new = new Persona(1, "Ana María", "Gómez", "ana@email.com");
        servicioPersona.actualizar(p1new);

        Persona p1update = new Persona(1, "Ana Carla", "Gómez", "ana@email.com");
        servicioPersona.guardarInformacion(p1update);

        Persona p3 = new Persona(3, "Carlos", "Ruiz", "carlos@email.com");
        servicioPersona.guardarInformacion(p3);

        servicioPersona.eliminar(2);


        System.out.println("Estado final de personas:");
        for (Persona p : servicioPersona.listado) {
            System.out.println(p);
        }
    }
}