# Proyecto Universitario con Patrón Adapter para Bases de Datos

Este proyecto es una aplicación de escritorio y consola para gestionar una universidad, desarrollada en Java con JavaFX. La característica principal es la implementación del **patrón de diseño Adapter**, que permite a la aplicación conectarse a diferentes motores de bases de datos (H2, MySQL y Oracle) de una manera flexible y desacoplada.

La aplicación se inicializa con un conjunto de datos de ejemplo para facilitar la prueba y 
---

## Requisitos Previos

Para ejecutar y evaluar el proyecto, es necesario tener instalado:

1.  **Java JDK 11** o superior.
2.  **Apache Maven** 3.6 o superior.
3.  **Docker** y **Docker Compose** (necesario solo para probar con MySQL y Oracle).

---

## Guía de Ejecución

A continuación se presentan las instrucciones para ejecutar el proyecto en cada uno de los entornos de base de datos soportados.

### Opción 1: Ejecución con H2 (Por defecto, no requiere Docker)

Esta es la forma más sencilla de ejecutar la aplicación.

1.  **Abrir una terminal** en la raíz del proyecto (`university-console-app-lab4`).

2.  **Construir el proyecto con Maven:**
    ```bash
    mvn clean package
    ```

3.  **Ejecutar el archivo JAR:**
    ```bash
    java -jar target/university-console-app-1.0-SNAPSHOT.jar
    ```

    La aplicación de escritorio y la de consola se iniciarán simultáneamente, conectadas a una base de datos H2 local y precargada con datos de ejemplo.

### Opción 2: Probar con MySQL (Requiere Docker)

Estos pasos demuestran la flexibilidad del patrón Adapter.

1.  **Iniciar el contenedor de MySQL con Docker:**
    En la terminal, en la raíz del proyecto, ejecutar:
    ```bash
    docker-compose up -d db-mysql
    ```
    *(Esperar unos 15 segundos para que la base de datos se inicie por completo).*

2.  **Modificar el archivo de configuración:**
    * Abrir el archivo `src/main/resources/db.properties`.
    * Cambiar la línea `db.active=h2` por `db.active=mysql`.
    * Guardar el archivo.

3.  **Reconstruir y ejecutar el proyecto:** Es crucial reconstruir para que el JAR incluya la nueva configuración.
    ```bash
    mvn clean package && java -jar target/university-console-app-1.0-SNAPSHOT.jar
    ```

    La aplicación se conectará a la base de datos MySQL. Se mostrará el mensaje `Servicios inicializados correctamente con la base de datos: mysql` en la consola.

### Opción 3: Probar con Oracle (Requiere Docker)

Este paso demuestra la compatibilidad con un sistema de base de datos más complejo.

1.  **Iniciar el contenedor de Oracle con Docker:**
    En la terminal, ejecutar:
    ```bash
    docker-compose up -d db-oracle
    ```
    *Importante: El contenedor de Oracle es lento. **Esperar entre 1 y 2 minutos** después de que el comando termine para asegurar que la base de datos esté lista.*

2.  **Modificar el archivo de configuración:**
    * Abrir `src/main/resources/db.properties`.
    * Cambiar la línea `db.active` para que sea `db.active=oracle`.
    * Guardar el archivo.

3.  **Reconstruir y ejecutar el proyecto:**
    ```bash
    mvn clean package && java -jar target/university-console-app-1.0-SNAPSHOT.jar
    ```

    La aplicación mostrará los intentos de conexión en la consola y, una vez establecida, se iniciará conectada a Oracle. Se mostrará el mensaje `Servicios inicializados correctamente con la base de datos: oracle`.

---
### Para Detener los Contenedores de Docker

Al finalizar la evaluación, los servicios de bases de datos iniciados se pueden detener con el siguiente comando en la terminal:

```bash
docker-compose down