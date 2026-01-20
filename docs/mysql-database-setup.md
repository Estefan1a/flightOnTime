### ✏️ `mysql-database-setup.md`


# Creación y configuración de la base de datos

## Base de datos usada
Nombre:
dbprediccionvuelo

## Script SQL

```sql
CREATE DATABASE dbprediccionvuelo;
USE dbprediccionvuelo;
```
## Configuración en Spring Boot

### Opción A – Variables de entorno (recomendado)

```
spring.datasource.url=jdbc:mysql://localhost:3306/dbprediccionvuelo
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}
```

### Opción B – Hardcodeado (solo desarrollo)

```
spring.datasource.password=tu_password_mysql
```
