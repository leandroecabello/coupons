# Coupon Challenge - Mercado Libre

## Descripción
Este proyecto implementa una API REST para resolver el challenge solicitado por Mercado Libre. La API permite maximizar el uso de un cupón para la compra de ítems favoritos y gestionar un endpoint para obtener el top 5 de ítems más seleccionados.

## Tecnologías utilizadas
- **Java 17**
- **Spring Boot 3.4.1**
- **Maven**
- **Docker**
- **AWS Lambda** (planificado para hosting en producción)
- **Redis** y **DynamoDB** (integración futura)

## Endpoints
### `/api/status`
- **Método**: GET
- **Descripción**: Verifica el estado de la aplicación.
- **Respuesta**: 
  ```json
  {
      "message": "Application is running!"
  }
  ```

### `/coupon`
- **Método**: POST (Planificado)
- **Descripción**: Calcula los ítems que pueden ser comprados con el cupón sin exceder su monto.

### `/coupon/stats`
- **Método**: GET (Planificado)
- **Descripción**: Devuelve el top 5 de ítems más seleccionados.

## Requisitos previos
1. **Java 17**: Asegúrate de tener instalado Java 17.
2. **Docker**: Configurado en tu sistema.
3. **Maven**: Instalado para construir el proyecto localmente si no usas Docker.

## Instalación y ejecución
### Opcion 1: Usando Docker
1. Construye la imagen Docker:
   ```bash
   docker build -t springboot-coupon .
   ```
2. Inicia el contenedor:
   ```bash
   docker run -p 8080:8080 springboot-coupon
   ```
3. Accede a la aplicación en [http://localhost:8080](http://localhost:8080).

### Opcion 2: Usando Docker Compose
1. Inicia los servicios:
   ```bash
   docker-compose up
   ```
2. Accede a la aplicación en [http://localhost:8080](http://localhost:8080).

### Opcion 3: Ejecución local
1. Instala las dependencias:
   ```bash
   mvn clean install
   ```
2. Inicia la aplicación:
   ```bash
   mvn spring-boot:run
   ```

## Estructura del proyecto
```
src
├── main
│   ├── java
│   │   └── com.coupon.challenge
│   │       ├── CouponApplication.java   # Clase principal
│   │       ├── controller
│   │       │   └── CouponController.java # Controlador REST
│   │       └── service                  # Lógica de negocio
│   └── resources
│       └── application.properties       # Configuración de la aplicación
└── test
    └── java                             # Pruebas unitarias
```

## Archivos adjuntos
Este repositorio incluye los siguientes documentos relevantes:
1. [Challenge](./challenge.txt): Detalles completos del desafío planteado por Mercado Libre.
2. [Propuesta](./propuesta_desarrollo.txt): Documentación de la planificación inicial del proyecto.

## Diagramas
### Arquitectura del sistema
![Arquitectura](./images/diagrama_arq.png)

## Próximos pasos
1. Integrar Redis para el almacenamiento en caché.
2. Implementar DynamoDB como base de datos persistente.
3. Configurar el despliegue en AWS Lambda para producción.

## Contribuciones
Si deseas contribuir al proyecto, sigue estos pasos:
1. Haz un fork del repositorio.
2. Crea una rama para tu funcionalidad (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza un pull request con tus cambios.

## Autor
Leandro Cabello - [Linkedin](https://www.linkedin.com/in/leandroezequielcabello/)

---
Gracias por revisar este proyecto. ¡Espero tus comentarios! 🚀

