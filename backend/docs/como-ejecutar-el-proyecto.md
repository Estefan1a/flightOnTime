# ▶️ Cómo ejecutar el proyecto Flight On Time (uso local)

A continuación se describen los pasos para ejecutar el proyecto de manera local.

---

## ETAPA 01 - Descarga del Código

En tu equipo local, con el terminal de Git abierto, ejecutar lo siguiente:

### 🔹 **Clonar el repositorio**
```
git clone https://github.com/Estefan1a/flightOnTime.git
cd flightOnTime
```
### 🔹 **Cambiar a la rama del backend**

```
git checkout backend
```

### 🔹 **Entrar a la carpeta del backend**

```
cd Backend
```

### 🔹**Ejecutar la aplicación Backend**
Esta aplicación trabaja con Spring Boot e incluye Maven Wrapper, por lo que no requiere instalar Maven globalmente.

Opción 1 - Windows + Git Bash

```
./mvnw spring-boot:run
```

Opción 2 - CMD o PowerShell

```
mvnw spring-boot:run
```

## ETAPA 02 - Acceso a la aplicación

### 🔹 **Desde la barra de direcciones de tu navegador digitar:**

```
URL: http://localhost:8080/dashboard
```

## ETAPA 03 - Ejecutar el microservicio Flask (ML)

Entrar a la carpeta del microservicio:

```
cd microservice
```

Instalar dependencias de Python (recomendado en un virtualenv):

```
pip install -r requirements.txt
```

Ejecutar el microservicio:

```
python api.py
```

El microservicio correrá por defecto en:

```
http://localhost:5000/predict
```

## ETAPA 04 - Configuración de MySQL

Si deseas ejecutar el frontend junto con la base de datos MySQL, crea un usuario y asigna permisos:
```
CREATE USER IF NOT EXISTS 'adminflightdb'@'localhost' IDENTIFIED BY '0089';
GRANT ALL PRIVILEGES ON *.* TO 'adminflightdb'@'localhost' WITH GRANT OPTION;
FLUSH PRIVILEGES;
```

Nota: Asegúrate de actualizar los datos de conexión en application.properties.

Si solo deseas ejecutar frontend sin la base de datos:
```
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
CONSIDERACIONES IMPORTANTES

El microservicio Flask debe estar activo antes de realizar predicciones desde el backend.

Verifica que los aeropuertos estén soportados en AirportCoordinates para evitar errores de predicción.

Para ver predicciones de ejemplo:
``` json
{
"fechaPartida": "2026-01-23T22:30:00",
"distanciaKm": 2500,
"aerolinea": "DL",
"origen": "JFK",
"destino": "LAX"
}
```

La respuesta mostrará:

- revision: `"PUNTUAL"` o `"RETRASADO"`
- probabilidad: `nivel de confianza`
- weather: `info climática`


## 🔗 Archivos de referencia


[Instalación y configuración de MySQL](backend/docs/mysql-install.md)

[Creación de la base de datos](backend/docs/mysql-database-setup.md)

[Configuración del microservicio Flask](backend/docs/flask-setup.md)

---



