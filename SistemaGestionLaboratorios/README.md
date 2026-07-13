# Sistema de Gestión de Laboratorios - Java Swing

Interfaz de escritorio desarrollada con Java Swing sobre la arquitectura original del proyecto.

## Funcionalidades por rol

### Alumno
- Inicio de sesión.
- Asignación automática de la primera computadora disponible.
- Selección de laboratorio, fecha, horario y curso.
- Consulta de reservas activas.
- Registro de incidencias técnicas.

### Profesor
- Inicio de sesión.
- Reserva de laboratorio por fecha y horario.
- Validación de cruces mediante detección de intervalos.
- Consulta de reservas activas.
- Registro de incidencias técnicas.

### Administrador
- Consulta y cambio de estado de computadoras.
- Listado de usuarios.
- Consulta y cancelación de reservas.
- Consulta y actualización de incidencias.

### Técnico
- Atención de incidencias en orden FIFO.
- Cambio de estado a EN_PROCESO o RESUELTA.
- Consulta y actualización del estado de computadoras.

## Requisitos

- JDK 21.
- MySQL 8 o compatible.
- Maven 3.9 o ejecución desde NetBeans/IntelliJ con soporte Maven.
- Base de datos `gestion_laboratorios` con las tablas usadas por el proyecto original.

## Configuración de MySQL

Edite el archivo ubicado en la raíz del proyecto:

`db.properties`

La copia de `src/main/resources/db.properties` se conserva como configuración predeterminada del classpath.

```properties
db.url=jdbc:mysql://localhost:3306/gestion_laboratorios
db.user=root
db.password=SU_CONTRASENA
```

También se admiten las variables de entorno:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

## Ejecución

Desde NetBeans:

1. Cierre la copia anterior del proyecto si todavía está abierta.
2. Abra esta carpeta como proyecto Maven.
3. Espere a que se descargue `mysql-connector-j`.
4. Use **Run Project / Ejecutar proyecto (F6)**.

El archivo `nbactions.xml` fija explícitamente `pe.edu.unmsm.fisi.Main` como clase principal. Esto evita que NetBeans intente ejecutar literalmente `${packageClassName}`.

También puede abrir `src/main/java/pe/edu/unmsm/fisi/Main.java` y elegir **Run File / Ejecutar archivo**.

Desde terminal:

```bash
mvn clean package
java -jar target/SistemaGestionLaboratoriosSwing-1.0.0.jar
```

## Observaciones

- El inicio de sesión usa los registros de `tbl_usuarios`.
- Las contraseñas se validan como en el proyecto original. Para producción debe implementarse hash seguro.
- Una reserva docente usa `id_computadora = NULL`.
- Las reservas activas se reconocen mediante los estados `ACTIVA` y `APROBADA`.
- Las incidencias se atienden por fecha ascendente cuando su estado es `PENDIENTE`.

## Solución del error `${packageClassName}` en NetBeans

Si una copia anterior mostró:

```text
Error: no se ha encontrado o cargado la clase principal ${packageClassName}
```

la causa es que NetBeans intentó ejecutar el marcador `${packageClassName}` como si fuera el nombre real de una clase. Esta entrega incluye `nbactions.xml`, donde la clase principal queda fijada como:

```text
pe.edu.unmsm.fisi.Main
```

Para evitar que NetBeans reutilice la configuración anterior:

1. Cierre el proyecto anterior en NetBeans.
2. Elimine o cambie de nombre la carpeta anterior.
3. Extraiga nuevamente este ZIP.
4. Abra la carpeta que contiene directamente `pom.xml` y `nbactions.xml`.
5. Use **Ejecutar proyecto (F6)**.

Como alternativa desde una terminal situada en la carpeta del proyecto:

```bash
mvn clean package
java -jar target/SistemaGestionLaboratoriosSwing-1.0.0.jar
```

## Edición visual con la pestaña Design de NetBeans

Esta entrega contiene formularios reales de NetBeans. Cada interfaz tiene dos archivos asociados:

```text
NombreFormulario.java
NombreFormulario.form
```

Abra el archivo `.java` desde **Source Packages**. NetBeans mostrará las pestañas:

```text
Source | Design
```

Formularios incluidos:

- `ui/LoginFrame`
- `ui/DashboardFrame`
- `ui/panel/InicioPanel`
- `ui/panel/AsignacionAlumnoPanel`
- `ui/panel/ReservaProfesorPanel`
- `ui/panel/MisReservasPanel`
- `ui/panel/ReportarIncidenciaPanel`
- `ui/panel/EquiposPanel`
- `ui/panel/UsuariosPanel`
- `ui/panel/ReservasAdminPanel`
- `ui/panel/IncidenciasAdminPanel`
- `ui/panel/IncidenciasTecnicoPanel`

No edite manualmente el bloque marcado como **Generated Code** dentro de `initComponents()`. Cambie componentes y distribución desde **Design**. La lógica de botones, consultas y servicios está colocada fuera de ese bloque para que NetBeans no la sobrescriba.

Si NetBeans muestra únicamente `Source`:

1. Cierre la clase.
2. Confirme que el archivo `.form` está al lado del archivo `.java` dentro de la misma carpeta.
3. Cierre el proyecto anterior y abra la carpeta de esta entrega que contiene `pom.xml`.
4. Use **Clean and Build** y vuelva a abrir la clase desde **Source Packages**, no desde la vista **Files**.
