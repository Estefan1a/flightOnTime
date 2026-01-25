# 🔐 Variables de Entorno

Este documento describe las variables de entorno necesarias para ejecutar
el proyecto FlightOnTime en un entorno local.

---

## ☕ Backend Spring Boot (IntelliJ IDEA)

El backend principal está desarrollado en Spring Boot.
Las variables de entorno se configuran desde la **Run Configuration**
de IntelliJ IDEA o como variables del sistema.

### Variables requeridas

|     Variable   |         Descripción         |        Ejemplo        |
|----------------|-----------------------------|-----------------------|
| DB_HOST        | Host de MySQL               | localhost             |
| DB_PORT        | Puerto de MySQL             | 3306                  |
| DB_NAME        | Nombre de la base de datos  | dbprediccionvuelo     |
| DB_USER        | Usuario de base de datos    | adminflightdb         |
| DB_PASSWORD    | Contraseña de la BD         | Equipo67#0089         |
| ML_SERVICE_URL | URL del microservicio Flask | http://localhost:5000 |

### Ejemplo en IntelliJ
- DB_URL = jdbc:mysql://localhost/tu_base_de_datos
- DB_NAME = tu_base_de_datos
- DB_USER = tu_usuario
- DB_PASSWORD = tu_contraseña
- ORACLE_PREDICTION_URL = http://localhost/predict
- WEATHER_API_URL = https://api_de_clima
---
### Configuración en IntelliJ IDEA

1. Ir a **Run > Edit Configurations**
2. Seleccionar la aplicación Spring Boot
3. En el campo **Environment variables**, agregar la ruta del archivo .env o manualmente:

    ```
    DB_URL = jdbc:mysql://localhost/tu_base_de_datos
    DB_NAME = tu_base_de_datos
    DB_USER = tu_usuario
    DB_PASSWORD = tu_contraseña
    ORACLE_PREDICTION_URL = http://localhost/predict
    WEATHER_API_URL = https://api_de_clima
    ```
4. `Guardar` y `ejecutar el proyecto`.
---

## 🐍 Microservicio Flask (VS Code)

El microservicio de Machine Learning se ejecuta en Python utilizando Flask.
Las variables de entorno pueden configurarse mediante un archivo `.env`
o directamente desde VS Code.

Estas variables pueden configurarse:

- en un archivo `.env` (no versionado)
- o como variables del sistema

### Variables requeridas

| Variable | Descripción | Ejemplo |
|--------|-------------|---------|
| MODEL_PATH | Ruta del modelo entrenado | champion_clima.joblib |
| FLASK_ENV | Entorno de ejecución | development |
| FLASK_PORT | Puerto del microservicio | 5000 |

### Ejemplo `.env`

```env
MODEL_PATH=champion_clima.joblib
FLASK_ENV=development
FLASK_PORT=5000

```

### 📄 Carga de variables de entorno en Flask

El microservicio utiliza la librería `python-dotenv` para cargar las variables
definidas en el archivo `.env`.

En el archivo principal del microservicio (`api.py`) se incluye lo siguiente:

```python
from dotenv import load_dotenv
import os

load_dotenv(dotenv_path=".env")
```
Esto permite que Flask acceda a las variables de entorno mediante `os.getenv()`:
```
MODEL_PATH = os.getenv("MODEL_PATH")
FLASK_PORT = os.getenv("FLASK_PORT", 5000)
```

>⚠️ El archivo .env no debe subirse al repositorio.

---