# FestivalAPI 🎟️

**API REST para la gestión de festivales, eventos y entradas**

Desarrollé FestivalAPI como parte de la materia Programación II. El proyecto comenzó en un parcial y después lo fui ampliando para completar las operaciones de la API, mejorar la organización del código y trabajar con las relaciones entre las entidades.

La aplicación permite registrar **asistentes, organizadores y eventos**, y gestionar entradas de tipo **General, VIP y QR**. Está desarrollada con Java y Spring Boot, y utiliza SQLite para guardar la información.

## Funcionalidades

- Registrar, consultar, modificar y eliminar asistentes, organizadores y eventos.
- Buscar organizadores por CUIT.
- Crear entradas y asociarlas con un asistente, un organizador y un evento.
- Gestionar entradas de tipo General, VIP y QR.
- Consultar entradas, modificar sus precios y eliminarlas.
- Registrar el uso de entradas QR y evitar que se utilicen dos veces.
- Validar los datos recibidos y devolver respuestas HTTP según el resultado de cada operación.

## Tecnologías utilizadas

| Tecnología | Uso |
| --- | --- |
| Java 21 | Lógica de la aplicación y programación orientada a objetos |
| Spring Boot 4.1.1 | Desarrollo de la API REST |
| Spring Web MVC | Controladores y solicitudes HTTP |
| Spring Data JPA / Hibernate | Persistencia y relaciones entre entidades |
| SQLite | Base de datos |
| Jakarta Validation | Validación de datos |
| JUnit 5 y Mockito | Pruebas automatizadas |
| Maven | Dependencias y ejecución del proyecto |

## Organización del proyecto

Organicé el código en capas para separar las responsabilidades de la aplicación:

```text
src/
├── main/
│   ├── java/com/festival/
│   │   ├── controlador/
│   │   ├── dto/
│   │   ├── error/
│   │   ├── modelo/
│   │   ├── repositorio/
│   │   └── servicio/
│   └── resources/
│       └── application.properties
└── test/java/com/festival/
    ├── integracion/
    └── servicio/
```

Los **controladores** reciben las solicitudes HTTP, los **servicios** contienen la lógica de negocio y los **repositorios** se encargan de las operaciones con la base de datos. Utilicé **DTOs** para definir los datos que recibe y devuelve la API.

En el modelo de datos, `Persona` es la clase base de `Asistente` y `Organizador`. Por otro lado, `Entrada` es la clase base de `General`, `Vip` y `QR`. Cada entrada se relaciona con un asistente, un organizador y un evento.

## Cómo ejecutar el proyecto

**Requisito:** tener instalado JDK 21.

1. Clonar el repositorio:

```bash
git clone https://github.com/MCammisiMicaela/FestivalAPI.git
cd FestivalAPI
```

2. Ejecutar la aplicación.

**En Windows:**

```powershell
.\mvnw.cmd spring-boot:run
```

**En Linux o macOS:**

```bash
./mvnw spring-boot:run
```

3. Cuando se inicie Spring Boot, consultar la API en:

**http://localhost:8080/api/eventos**

La aplicación crea y utiliza una base de datos SQLite local llamada `festival.db`.

## Endpoints

### Asistentes

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/asistentes` | Listar asistentes |
| POST | `/api/asistentes` | Registrar un asistente |
| GET | `/api/asistentes/{id}` | Buscar por ID |
| PUT | `/api/asistentes/{id}` | Actualizar |
| DELETE | `/api/asistentes/{id}` | Eliminar |

### Organizadores

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/organizadores` | Listar organizadores |
| POST | `/api/organizadores` | Registrar un organizador |
| GET | `/api/organizadores/{id}` | Buscar por ID |
| GET | `/api/organizadores/cuit/{cuit}` | Buscar por CUIT |
| PUT | `/api/organizadores/{id}` | Actualizar |
| DELETE | `/api/organizadores/{id}` | Eliminar |

### Eventos

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/eventos` | Listar eventos |
| POST | `/api/eventos` | Registrar un evento |
| GET | `/api/eventos/{id}` | Buscar por ID |
| PUT | `/api/eventos/{id}` | Actualizar |
| DELETE | `/api/eventos/{id}` | Eliminar |

### Entradas

| Método | Endpoint | Descripción |
| --- | --- | --- |
| GET | `/api/entradas` | Listar entradas |
| POST | `/api/entradas` | Crear entrada General, VIP o QR |
| GET | `/api/entradas/{id}` | Buscar por ID |
| PATCH | `/api/entradas/{id}/precio` | Actualizar precio |
| PATCH | `/api/entradas/{id}/usar` | Registrar uso de una entrada QR |
| DELETE | `/api/entradas/{id}` | Eliminar entrada |

## Ejemplos de uso

### Crear un evento

**POST** `http://localhost:8080/api/eventos`

```json
{
  "nombre": "Festival Demo",
  "descripcion": "Concierto de prueba",
  "fechaRealizacion": "2027-06-15T20:00:00",
  "lugar": "Anfiteatro Demo"
}
```

### Crear una entrada general

**POST** `http://localhost:8080/api/entradas`

```json
{
  "tipo": "GENERAL",
  "asistenteId": 1,
  "organizadorId": 7,
  "eventoId": 1,
  "precio": 15000.00,
  "codigo": "GEN-EJEMPLO-001"
}
```

Para crear una entrada, primero tienen que existir el asistente, el organizador y el evento. Los IDs del ejemplo se reemplazan por los que corresponda en cada base de datos.

También incluí solicitudes adicionales en [`examples/api.http`](examples/api.http).

## Datos de ejemplo

Preparé un conjunto de datos para probar la aplicación sin cargar todos los registros a mano. Incluye **6 asistentes, 3 organizadores, 4 eventos y 12 entradas** de los tres tipos.

Los datos están en [`examples/datos-demo.json`](examples/datos-demo.json) y se pueden cargar desde Windows PowerShell, con la API en ejecución:

```powershell
powershell -ExecutionPolicy Bypass -File .\examples\cargar-datos-demo.ps1
```

El script registra los datos mediante los endpoints de la API y utiliza los IDs generados para relacionarlos. Si volvés a ejecutarlo, reconoce los registros de ejemplo ya cargados.

## Pruebas

Agregué pruebas unitarias y de integración para comprobar las operaciones principales, las relaciones entre entidades y las validaciones.

Para ejecutarlas:

**Windows:**

```powershell
.\mvnw.cmd test
```

**Linux/macOS:**

```bash
./mvnw test
```

El proyecto cuenta con **17 pruebas automatizadas aprobadas** en GitHub Actions.

Además, probé la API con Postman usando una colección de **19 solicitudes HTTP**, que incluye creación de registros, consultas, actualización de precios, uso de entradas QR y eliminación de datos. La ejecución completa terminó con **19 pruebas aprobadas y ninguna fallida**.

Podés importar la colección desde [`postman/FestivalAPI.postman_collection.json`](postman/FestivalAPI.postman_collection.json) y ejecutarla con la aplicación iniciada en `http://localhost:8080`.

## Autora

**[Micaela Cammisi](https://github.com/MCammisiMicaela)**
