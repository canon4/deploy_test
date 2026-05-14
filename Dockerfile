# =============================================================
# Etapa 1: Compilación con Maven + Java 17
# =============================================================
FROM maven:3.9-eclipse-temurin-17 AS builder

WORKDIR /app

# Copiar archivos de configuración Maven primero (cache de dependencias)
COPY pom.xml .
RUN mvn dependency:resolve -q

# Copiar código fuente y compilar
COPY src ./src
RUN mvn clean package -DskipTests -q

# =============================================================
# Etapa 2: Imagen de producción con Tomcat 10 + Java 17
# =============================================================
FROM tomcat:10.1-jdk17-temurin-jammy

# Eliminar aplicaciones por defecto de Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiar el WAR compilado como ROOT.war (se despliega en /)
COPY --from=builder /app/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

# Puerto interno de Tomcat
EXPOSE 8080

# Comando de inicio
CMD ["catalina.sh", "run"]
