import model.Curso;
import model.Estudiante;
import model.Inscripcion;
import persistence.DatabaseManager;
import services.CursosInscritos;

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
            servicio.imprimirLista().forEach(System.out::println);
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
            servicio.imprimirLista().forEach(System.out::println);
        }






    }
}