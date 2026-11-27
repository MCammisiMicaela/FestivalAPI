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

El archivo [`examples/api.http`](examples/api.http) contiene una secuencia de solicitudes para practicar con Postman, Insomnia o un cliente HTTP compatible.

## Pruebas

Para ejecutar las pruebas automatizadas:

```bash
./mvnw test
```

En Windows:

```powershell
.\mvnw.cmd test
```

Los tests unitarios cubren creación de eventos, prevención de documentos duplicados, emisión de entradas, códigos duplicados, recursos inexistentes y validación de uso único de entradas QR. Además, existe una prueba de carga del contexto de Spring Boot. GitHub Actions ejecuta `mvnw verify` al publicar cambios.

## Evolución del parcial

Partiendo del código original, realicé las siguientes mejoras:

- Eliminación de la dependencia duplicada de SQLite en `pom.xml`.
- Corrección de `Evento`: se modeló como entidad concreta para permitir su creación.
- Cambio de las asociaciones de entradas a `@ManyToOne` para admitir múltiples entradas por persona, organizador y evento.
- Separación entre controladores, servicios, repositorios, DTOs y entidades.
- Unificación de los tipos de entradas bajo `/api/entradas`.
- Tratamiento del precio con `BigDecimal` y del teléfono como `String`.
- Validación de solicitudes y manejo centralizado de errores.
- Incorporación de métodos de actualización, baja y registro de uso de QR.
- Exclusión de la base de datos original y los archivos generados del repositorio público.

El proyecto se enfoca en el desarrollo backend y no incluye interfaz web, autenticación ni procesamiento de pagos reales.

## Autora

Desarrollado por **[Micaela Cammisi](https://github.com/MCammisiMicaela)**.
