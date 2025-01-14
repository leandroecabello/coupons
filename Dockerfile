# Usa la imagen de OpenJDK para Java 19
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo de dependencias y descarga las dependencias necesarias
COPY pom.xml .
RUN apt-get update && \
	apt-get install -y maven && \
	mvn -B dependency:go-offline && \
	apt-get clean && \
	rm -rf /var/lib/apt/lists/*

# Copia el código fuente del proyecto al contenedor
COPY src ./src

# Compila y empaqueta la aplicación
RUN mvn -B clean package -DskipTests

# Expone el puerto por donde correrá la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "target/coupon-0.0.1-SNAPSHOT.jar"]