# simbaña\_josselyn\_shipflow 

Sistema de gestión y seguimiento de envíos tipo "ShipFlow" desarrollado en Kotlin + Spring Boot + PostgreSQL.

Microservicio REST para registrar, consultar y actualizar el estado de paquetes enviados.

---

## Tabla de contenido

* Descripción General
* Requisitos Previos
* Cómo Ejecutar el Proyecto
* Estructura del Sistema
* **Endpoints Disponibles**
* Notas y Reglas de Negocio

---

## Descripción General

ShipFlow es un sistema para la gestión de envíos de paquetes, permitiendo:

* Registrar nuevos envíos (paquetes) con información relevante y estados.
* Consultar detalles completos de los envíos por número de seguimiento (`tracking_id`).
* Actualizar el estado del envío y mantener el historial de eventos.
* Validar reglas de transición entre estados.

---

## Requisitos Previos

* Docker (recomendado para la base de datos)
* Java 17 o superior
* (Opcional) Postman para pruebas

---

## Cómo ejecutar el proyecto

1. **Clona el repositorio:**

   ```bash
   git clone https://github.com/tuusuario/simbaña_josselyn_shipflow.git
   cd simbaña_josselyn_shipflow
   ```
2. **Levanta la base de datos PostgreSQL:**

   ```bash
   docker-compose up -d
   ```
3. **Ejecuta el microservicio:**

    * Con Gradle:

      ```bash
      ./gradlew bootRun
      ```
    * O desde IntelliJ ejecuta `SimbanaJosselynShipflowApplication.kt`
4. **Accede a la API en:** `http://localhost:8080`

---

## Estructura del Sistema

```
src/main/kotlin/com/pucetec/simbana_josselyn_shipflow/
│
├── controllers/      # Controladores REST (PackageController)
├── models/           # Entidades, enums y requests/responses
├── repositories/     # Repositorios JPA (PackageRepository, EventRepository)
├── services/         # Lógica de negocio (PackageService)
├── mappers/          # Conversión entre modelos y DTOs
├── exceptions/       # Excepciones personalizadas
└── routes/           # Rutas centralizadas (Routes.kt)
```

---

# Endpoints disponibles

## 1. Registrar un nuevo envío

* **Método:** POST
* **URL:** `/api/packages`
* **Body ejemplo:**

```json
{
  "tracking_id": "A1",
  "type": "DOCUMENT",
  "weight": 1.5,
  "description": "Documentos legales",
  "city_from": "Quito",
  "city_to": "Guayaquil",
  "status": "PENDING",
  "estimated_delivery": "2025-08-01T10:00:00"
}
```

* **Respuesta:**

```json
{
  "id": 1,
  "tracking_id": "A1",
  "created_at": "2025-07-22T21:00:00",
  "udated_at": "2025-07-22T21:00:00",
  "type": "DOCUMENT",
  "weight": 1.5,
  "description": "Documentos legales",
  "city_from": "Quito",
  "city_to": "Guayaquil",
  "status": "PENDING",
  "estimated_delivery": "2025-08-01T10:00:00",
  "events": [
    {
      "id": 1,
      "created_at": "2025-07-22T21:00:00",
      "udated_at": "2025-07-22T21:00:00",
      "status": "PENDING",
      "comment": "Registro inicial"
    }
  ]
}
```

---

## 2. Consultar un envío por número de seguimiento

* **Método:** GET
* **URL:** `/api/packages/{trackingId}`
* **Respuesta ejemplo:**

```json
{
  "id": 1,
  "tracking_id": "A1",
  "created_at": "2025-07-22T21:00:00",
  "udated_at": "2025-07-22T21:00:00",
  "type": "DOCUMENT",
  "weight": 1.5,
  "description": "Documentos legales",
  "city_from": "Quito",
  "city_to": "Guayaquil",
  "status": "PENDING",
  "estimated_delivery": "2025-08-01T10:00:00",
  "events": [
    {
      "id": 1,
      "created_at": "2025-07-22T21:00:00",
      "udated_at": "2025-07-22T21:00:00",
      "status": "PENDING",
      "comment": "Registro inicial"
    },
    {
      "id": 2,
      "created_at": "2025-07-23T08:00:00",
      "udated_at": "2025-07-23T08:00:00",
      "status": "IN_TRANSIT",
      "comment": "El paquete salió de bodega"
    }
  ]
}
```

---

## 3. Actualizar estado de un envío

* **Método:** PUT
* **URL:** `/api/packages/{trackingId}/status`
* **Body ejemplo:**

```json
{
  "status": "IN_TRANSIT",
  "comment": "El paquete salió de bodega"
}
```

* **Respuesta:** Igual al GET, con historial actualizado.

---

\$1

---

## Pruebas de restricciones y reglas de negocio (casos especiales)

### 1. Detección de duplicados de tracking\_id

* Si intentas registrar un envío con un `tracking_id` ya existente, obtendrás un error:

    * **POST /api/packages**
    * **Respuesta:**

      ```json
      {
        "error": "El tracking_id ya está registrado."
      }
      ```

### 2. Ciudades de origen y destino iguales

* Si envías un paquete con la misma ciudad en `city_from` y `city_to`:

    * **POST /api/packages**
    * **Respuesta:**

      ```json
      {
        "error": "La ciudad de origen y destino no pueden ser iguales."
      }
      ```

### 3. Cambios inválidos de estado (reglas de transición)

* **Ejemplo:** PENDING → DELIVERED (no permitido)
* **PUT /api/packages/{trackingId}/status** con body:

  ```json
  {
    "status": "DELIVERED",
    "comment": "Intento no permitido"
  }
  ```

    * **Respuesta:**

      ```json
      {
        "error": "El primer cambio debe ser a IN_TRANSIT."
      }
      ```
* Otros ejemplos de errores por transición:

    * Intentar cambiar estado después de DELIVERED o CANCELLED:

      ```json
      {
        "error": "No se puede cambiar el estado de un paquete finalizado."
      }
      ```
    * Cambiar a DELIVERED sin pasar por IN\_TRANSIT:

      ```json
      {
        "error": "Solo puedes poner DELIVERED si el paquete ya estuvo en IN_TRANSIT."
      }
      ```

### 4. Historial de eventos

* Cada vez que el estado cambia exitosamente, el nuevo evento se agrega al array `events` en la respuesta del GET del paquete.
* Puedes consultar todos los cambios y comentarios realizados en la vida del envío.

---

## Autor

* **Josselyn Simbaña Calapiña** [PUCE TEC](https://www.pucetec.edu.ec/)
