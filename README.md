# Festival API 🎟️

**API REST para la gestión de eventos y entradas de un festival**

Proyecto académico individual de backend, desarrollado en **Java 21, Spring Boot 4.1.1, Spring Data JPA y SQLite**. La aplicación permite administrar organizadores, asistentes, eventos y entradas de tipo **General, VIP o QR**.

El proyecto nació como un parcial de Bases de Datos II y fue ampliado para incorporar operaciones REST completas, separación por capas, validaciones, respuestas HTTP consistentes y pruebas unitarias.

## Funcionalidades

- Alta, consulta, modificación y baja de asistentes, organizadores y eventos.
- Búsqueda de organizadores por CUIT.
- Emisión de entradas vinculadas a un asistente, un organizador y un evento.
- Entradas con especialización **General**, **VIP** y **QR**, conservando el modelo de herencia JPA.
- Consulta, actualización de precio y eliminación de entradas.
- Registro de uso de entradas QR y prevención del uso duplicado.
- Validación de campos obligatorios y precios positivos.
- Manejo de recursos inexistentes (HTTP 404), entradas inválidas (HTTP 400) y conflictos de negocio (HTTP 409).
- Persistencia local en SQLite y pruebas unitarias de la lógica de negocio.

## Tecnologías

| Tecnología | Uso |
| --- | --- |
| Java 21 | Entidades, servicios y reglas de negocio |
| Spring Boot 4.1.1 | Configuración y ejecución de la aplicación |
| Spring Web MVC | Controladores y endpoints REST |
| Spring Data JPA / Hibernate | Persistencia y relaciones entre entidades |
| SQLite | Base de datos local |
| Jakarta Validation | Validación de solicitudes |
| JUnit 5 / Mockito | Pruebas unitarias |
| Maven | Gestión de dependencias y compilación |

## Arquitectura


```text
src/
├── main/
│   ├── java/com/festival/
│   │   ├── controlador/   # Endpoints HTTP
│   │   ├── dto/           # Solicitudes y respuestas JSON
│   │   ├── error/         # Excepciones y respuestas de error
│   │   ├── modelo/        # Entidades JPA
│   │   ├── repositorio/   # Persistencia
│   │   └── servicio/      # Reglas de negocio
│   └── resources/application.properties
└── test/java/com/festival/servicio/  # Pruebas unitarias
```

### Modelo de dominio

- `Persona` es la superclase abstracta de `Asistente` y `Organizador`.
- `Evento` representa los recitales o encuentros programados.
- `Entrada` es la superclase abstracta de `General`, `Vip` y `QR`.
- Cada entrada se relaciona con **un asistente, un organizador y un evento**; cada uno puede tener múltiples entradas.
- Los datos de entrada/salida se representan con **DTOs** para evitar exponer las entidades JPA directamente.

## Requisitos

- **JDK 21** instalado.
- Conexión a Internet en la primera compilación para descargar las dependencias Maven.
- Maven 3.9+ o el wrapper incluido (`mvnw` / `mvnw.cmd`).

## Cómo ejecutar

1. Clonar el repositorio y acceder a su carpeta:

   ```bash
   git clone https://github.com/MCammisiMicaela/FestivalAPI.git
   cd FestivalAPI
   ```

2. Iniciar el servidor:

   **Windows:**

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **Linux/macOS:**

   ```bash
   chmod +x ./mvnw
   ./mvnw spring-boot:run
   ```

3. Consultar la API en **http://localhost:8080/api/eventos**.

SQLite creará un archivo `festival.db` en el directorio de trabajo. Este archivo se encuentra excluido mediante `.gitignore` porque no es parte del código fuente.

## Endpoints REST

| Método | Ruta | Operación |
| --- | --- | --- |
| `GET` | `/api/asistentes` | Listar asistentes |
| `POST` | `/api/asistentes` | Crear asistente |
| `GET` | `/api/asistentes/{id}` | Consultar asistente |
| `PUT` | `/api/asistentes/{id}` | Actualizar asistente |
| `DELETE` | `/api/asistentes/{id}` | Eliminar asistente sin entradas |
| `GET` | `/api/organizadores` | Listar organizadores |
| `POST` | `/api/organizadores` | Crear organizador |
| `GET` | `/api/organizadores/{id}` | Consultar organizador |
| `GET` | `/api/organizadores/cuit/{cuit}` | Buscar por CUIT |
| `PUT` | `/api/organizadores/{id}` | Actualizar organizador |
| `DELETE` | `/api/organizadores/{id}` | Eliminar organizador sin entradas |
| `GET` | `/api/eventos` | Listar eventos |
| `POST` | `/api/eventos` | Crear evento |
| `GET` | `/api/eventos/{id}` | Consultar evento |
| `PUT` | `/api/eventos/{id}` | Actualizar evento |
| `DELETE` | `/api/eventos/{id}` | Eliminar evento sin entradas |
| `GET` | `/api/entradas` | Listar entradas |
| `POST` | `/api/entradas` | Crear entrada General, VIP o QR |
| `GET` | `/api/entradas/{id}` | Consultar entrada |
| `PATCH` | `/api/entradas/{id}/precio` | Modificar precio |
| `PATCH` | `/api/entradas/{id}/usar` | Registrar uso de QR |
| `DELETE` | `/api/entradas/{id}` | Eliminar entrada |

### Ejemplo: crear un evento

```bash
curl -X POST http://localhost:8080/api/eventos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Festival Demo","descripcion":"Concierto de prueba","fechaRealizacion":"2027-06-15T20:00:00","lugar":"Anfiteatro Demo"}'
```

Una creación correcta devuelve **HTTP 201**, un JSON con su identificador y la cabecera `Location`.

### Ejemplo: emitir una entrada general

Primero se crean un asistente, un organizador y un evento. Con sus identificadores:

```json
{
  "tipo": "GENERAL",
  "asistenteId": 1,
  "organizadorId": 2,
  "eventoId": 1,
  "precio": 15000.00,
  "codigo": "GEN-DEMO-001"
}
```

Este JSON se envía mediante `POST /api/entradas`. Los identificadores son ilustrativos y pueden variar según los registros creados.

El archivo [`examples/api.http`](examples/api.http) contiene solicitudes HTTP de ejemplo.

### Datos ficticios para la demostración

Para que la API pueda mostrar información sin crear cada registro a mano, incluimos un [archivo JSON de ejemplo](examples/datos-demo.json) y un [cargador para Windows PowerShell](examples/cargar-datos-demo.ps1).

El conjunto contiene **6 asistentes, 3 organizadores, 4 eventos y 12 entradas** de tipo General, VIP y QR. Las identidades, empresas y documentos son ficticios; no representan personas ni entidades reales.

Con el servidor iniciado en el puerto 8080, desde la carpeta principal del proyecto, ejecutá:

```powershell
powershell -ExecutionPolicy Bypass -File .\examples\cargar-datos-demo.ps1
```

El script utiliza los endpoints REST y registra las relaciones con los **IDs devueltos por la API**. Los datos se guardan en el archivo SQLite local `festival.db`, que no se sube a GitHub. Es posible volver a ejecutar el script: identifica los ejemplos existentes por documento, CUIT, nombre de evento o código de entrada y evita crearlos de nuevo.

Verificá los resultados en `GET /api/asistentes`, `GET /api/organizadores`, `GET /api/eventos` y `GET /api/entradas`.

### Verificación manual con Postman

Incluí la colección [`postman/FestivalAPI.postman_collection.json`](postman/FestivalAPI.postman_collection.json) con **19 solicitudes** organizadas en el orden recomendado de ejecución.

1. Iniciá la aplicación localmente con `./mvnw spring-boot:run` (en Windows, `.\mvnw.cmd spring-boot:run`).
2. Abrí Postman y elegí **Import**. Seleccioná el archivo de la colección.
3. Comprobá que la variable `baseUrl` tenga el valor `http://localhost:8080`.
4. Abrí el **Collection Runner** y ejecutá las solicitudes en su orden original.

La colección crea registros con **datos ficticios**, guarda automáticamente sus identificadores y después comprueba consultas, cambios de precio, uso único de QR, errores esperados (400/409) y eliminación de registros. Algunas solicitudes deben devolver 400 o 409: **son comprobaciones previstas, no fallos de la API**.

> Se trata de un backend académico de demostración: no hay autenticación, autorización ni pagos reales. Usá exclusivamente datos de prueba, nunca información personal real.

## Pruebas

Para ejecutar las pruebas automatizadas:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

Las pruebas unitarias cubren creación de eventos, prevención de documentos duplicados, emisión de entradas, códigos repetidos, recursos inexistentes y uso único de entradas QR. Además, las **pruebas de integración con MockMvc y SQLite** recorren los endpoints HTTP: CRUD de eventos; entradas General, VIP y QR; actualización de precios; relaciones y restricciones de eliminación; documentos y CUIT duplicados; validaciones y respuestas 400, 404 y 409. Una prueba adicional verifica que Spring Boot inicia correctamente. GitHub Actions ejecuta `mvnw verify` en cada cambio.

### Verificación manual

El **27 de noviembre de 2026** se ejecutó en Postman el recorrido completo de la colección contra la API iniciada localmente con Spring Boot y SQLite. Resultado: **19 solicitudes HTTP verificadas, 19 pruebas aprobadas, 0 fallos y 0 errores**, incluidas las respuestas 400 y 409 esperadas para comprobar las reglas de validación y los conflictos. La duración de la ejecución fue de 2,413 segundos, con una respuesta promedio de 30 ms. Los valores corresponden a esa ejecución local y no representan una prueba de rendimiento o de carga.

## Evolución del parcial

Partiendo del código original, realicé las siguientes mejoras:

- Eliminación de la dependencia duplicada de SQLite en `pom.xml`.
- Corrección de `Evento`: se modeló como entidad concreta para permitir su creación.
- Cambio de las asociaciones de entradas a `@ManyToOne` para admitir múltiples entradas por persona, organizador y evento.
- Separación entre controladores, servicios, repositorios, DTOs y entidades.
- Unificación de los tipos de entradas bajo `/api/entradas`.
- Tratamiento del precio con `BigDecimal` y del teléfono como `String`.
- Validación de solicitudes, precisión de importes y manejo centralizado de errores (incluidos los identificadores no numéricos).
- Incorporación de métodos de actualización, baja y registro de uso de QR.
- Exclusión de la base de datos original y los archivos generados del repositorio público.

El proyecto se enfoca en el desarrollo backend y no incluye interfaz web, autenticación ni procesamiento de pagos reales.

## Autora

Desarrollado por **[Micaela Cammisi](https://github.com/MCammisiMicaela)**.
