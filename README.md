# Boma Analytics - API de Campañas

## Introducción
Esta API permite gestionar campañas de marketing, obteniendo métricas y generando reportes.

## Instalación

### Requisitos previos
- Java 17+
- Spring Boot
- MySQL
- Maven

### Configuración
1. Clonar el repositorio:
   ```sh
   git clone https://github.com/KHLChess/Boma_Analytics.git
   cd boma-analytics
   ```
2. Configurar el archivo `application.properties` con las credenciales de la base de datos.
3. Ejecutar el siguiente comando para compilar e iniciar el servicio:
   ```sh
   mvn spring-boot:run
   ```

## Endpoints Principales

### Campañas
- **Obtener todas las campañas**
  ```http
  GET /campanias/all
  ```
- **Buscar campaña por nombre**
  ```http
  POST /campanias/find-by-name
  Body: { "nombreCampania": "Nombre" }
  ```
- **Crear una nueva campaña**
  ```http
  POST /campanias/create
  Body: {
    "nombreCampania": "Mecanica 2025",
	"descripcion": "Mecanica y mas",
	"fechaInicio": "2025-04-18",
	"fechaFinal": "2025-04-28",	
	"tipoCampania": "promocional",
	"presupuesto": 35000,
	"tipoCanal": "redes sociales", 
	"resultadoEsperado": "diversion familiar",
	"observaciones": "todo ok"
	}
  ```
- **Actualizar campaña**
  ```http
  PUT /campanias/update
  Body: {
    "nombreCampania": "Mecanica 2025",
	"descripcion": "Mecanica y mas",
	"fechaInicio": "2025-04-18",
	"fechaFinal": "2025-04-28",	
	"tipoCampania": "promocional",
	"presupuesto": 35000,
	"tipoCanal": "redes sociales", 
	"resultadoEsperado": "diversion familiar",
	"observaciones": "todo ok",
  "statusCampania": "FINALIZADA"
	}
  ```
- **Eliminar campaña (Solo Super Admin)**
  ```http
  DELETE /campanias/delete
  Body: { "nombreCampania": "Campaña a Eliminar" }
  ```
- **Filtrar campañas por estado**
  ```http
  POST /campanias/filter
  Body: { "statusCampania": "ACTIVA" }
  ```

### Dashboard
- **Obtener métricas generales**
  ```http
  GET /dashboard
  ```

### Landing Page
- **Obtener detalles de campaña con métricas**
  ```http
  POST /landing
  Body: {
    "nombreCampania" : "Torneo Ajedrez 2025"
	}
  ```

## Colección de Postman / Insomnia
Para facilitar las pruebas, se ha exportado una colección de Postman con todas las solicitudes necesarias.

Descargar la colección:
[Postman Collection](https://github.com/KHLChess/Boma_Analytics/blob/main/docs/Boma%20Analytics.postman_collection.json)

---


