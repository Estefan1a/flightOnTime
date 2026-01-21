### ✏️ `mysql-database-setup.md`


# Creación y configuración de la base de datos

## Base de datos usada

### Nombre:
```
dbprediccionvuelo
```

## Script SQL

```sql
CREATE DATABASE dbprediccionvuelo;
USE dbprediccionvuelo;
```
## ⚙️ Configurar `application.properties`

### Ubicación:

```
backend/src/main/resources/application.properties
```

### Ejemplo:

```
spring.datasource.url=jdbc:mysql://localhost:3306/flight_on_time
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
```

## ▶️ Verificar conexión
- Arranca el backend
- Revisa que no haya errores de conexión en consola
- Las tablas se crean automáticamente al iniciar


🧠 Notas
Si ya tienes la BD configurada, puedes omitir este paso.

