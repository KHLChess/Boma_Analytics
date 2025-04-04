Boma Analytics - API de Campañas

Introducción

Esta API permite gestionar campañas de marketing, obteniendo métricas y generando reportes.

Instalación

Requisitos previos

Java 17+

Spring Boot

MySQL

Maven

Configuración

Clonar el repositorio:

git clone https://github.com/KHLChess/Boma_Analytics.git
cd boma-analytics

Configurar el archivo application.properties o application.yml con las credenciales de la base de datos.

Ejecutar el siguiente comando para compilar e iniciar el servicio:

mvn spring-boot:run

Endpoints Principales

Campañas

Obtener todas las campañas

GET /campanias/all

Buscar campaña por nombre

POST /campanias/find-by-name
Body: { "nombreCampania": "Nombre" }

Crear una nueva campaña

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

Actualizar campaña

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

Eliminar campaña (Solo Super Admin)

DELETE /campanias/delete
Body: { "nombreCampania": "Campaña a Eliminar" }

Filtrar campañas por estado

POST /campanias/filter
Body: { "statusCampania": "ACTIVA" }

Dashboard

Obtener métricas generales

GET /dashboard

Landing Page

Obtener detalles de campaña con métricas

POST /landing
Body: {
    "nombreCampania" : "Torneo Ajedrez 2025"
}
