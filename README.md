# ✈️ FlightOnTime

Proyecto desarrollado en el marco del **Hackatón ONE – No Country 2025**.

FlightOnTime es una solución predictiva que estima la probabilidad de que un vuelo despegue **puntual o con retraso**, a partir de datos históricos y características del vuelo.

---

## 🏭 Sector de negocio

**Aviación Civil / Logística / Transporte Aéreo**

Empresas aéreas, aeropuertos y pasajeros que dependen de la puntualidad de los vuelos.

---

## 🎯 Objetivo del proyecto

Crear un **MVP (Producto Mínimo Viable)** que:

- Reciba información de un vuelo
- Procese los datos usando un modelo predictivo
- Devuelva:
  - el estado del vuelo (Puntual / Retrasado)
  - la probabilidad asociada

---
## 🏗️ Arquitectura general
```
[ Cliente / Frontend ]
          |
          v
[ Backend Spring Boot ]
   |      |          |
   |      |          └── Base de Datos MySQL
   |      |
   |      └── Endpoints /stats
   |
   └── HTTP
          v
[ Microservicio Flask ]
   |
   └── Modelo ML (.joblib)
```

---

## 🧑‍🤝‍🧑 Equipo

### Backend
Integración, persistencia, estadísticas y APIs REST
- Estefanía González
- Alejandro Montoya Torres
- Javier Alberto Chávez Córdova
- Juan Gómez Martínez
- Gabriel Romero

### Data Science
Limpieza de datos, entrenamiento y exportación del modelo
- Luis Cavero
- Cristian Saenz

### Front End
Visualización y consumo de APIs
- Juan Martínez
- Gabriel Romero

---

## 🔧 Tecnologías utilizadas

### 🛠️ Tecnologías

### Backend
- Java 17
- Spring Boot 3.4.1
- Maven 4.0
- API REST
- MySQL
- Spring Data JPA
- Lombok

### Microservicio ML
- Python 3.10+
- Flask 3.0.0
- flask-cors 4.0.0
- scikit-learn
- joblib 1.3.2
- pandas 2.3.3
- numpy 2.3.2
- waitress 2.1.2

### Data Science
- Jupyter Notebook

### Herramientas
- IntelliJ IDEA
- Visual Studio Code
- Git & GitHub
- Insomnia / Postman

---
## 📦 Dataset utilizado

El modelo fue entrenado utilizando datasets públicos de Kaggle:

- https://www.kaggle.com/datasets/hrishitpatil/flight-data-2024/data
- https://www.kaggle.com/datasets/aadharshviswanath/flight-data/data

Incluyen información histórica de vuelos y variables operativas, complementadas con datos climáticos.

---

## 🧠 Modelo de Machine Learning

### Modelo activo

- Archivo: champion_clima.joblib

- Tipo: sklearn.pipeline.Pipeline

- Clasificador: HistGradientBoostingClassifier

### Variables utilizadas
Numéricas:
- crs_dep_time
- crs_arr_time
- crs_elapsed_time
- distancia_km
- is_weekend
- Temperature_Celsius
- Wind_Speed_knots
- Visibility_km

### Categóricas:
- op_unique_carrier
- origin
- dest
- day_of_week
- month
- day_of_month
- bloque_horario
- Turbulence_Level

El modelo devuelve:
- Clase: `0 = A TIEMPO`, `1 = RETRASADO`
- Probabilidades asociadas

---

## 🐍 Microservicio Flask (Predicción)

### Responsabilidad
- Cargar el modelo entrenado
- Transformar requests provenientes del backend Java
- Ejecutar predicciones
- Retornar resultados vía HTTP

### Endpoint principal

`POST /predict`

Entrada:
```
{
"aerolinea": "American Airlines",
"origen": "JFK",
"destino": "LAX",
"fechaPartida": "2026-06-15T14:30:00",
"distanciaKm": 3983
}
```

Salida:

```
{
"prevision": "RETRASADO",
"probabilidad": 0.9529,
"detalle": {
"prob_a_tiempo": 0.0471,
"prob_retraso": 0.9529}
}
```

---
## ☕ Backend Spring Boot
☕ Backend Spring Boot
Flujo de predicción

- El cliente envía la solicitud
- Se guarda el vuelo (`FlightRequest`)
- Se consulta el microservicio Flask
- Se guarda la predicción (`Prediction`)
- Se retorna la respuesta al cliente

---

## 🔧 Prerrequisitos
Antes de ejecutar el proyecto, asegúrese de:

- Tener MySQL instalado y en ejecución

  👉 [Instalación y configuración de MySQL](docs/mysql-install.md)
- Tener creada la base de datos

  👉 [Creación de la base de datos](docs/mysql-database-setup.md)
- Tener activo el microservicio Flask

  👉 [Configuración del microservicio Flask](docs/flask-setup.md)

  El microservicio de Machine Learning se encuentra en:👉 [microservice/](microservice/api.py)
  Incluye:
  - API Flask (`api.py`)
  - Carga del modelo `.joblib`
  - Endpoint `/predict`
---

## 📁 Estructura del repositorio

El repositorio está organizado por dominios de responsabilidad, siguiendo una arquitectura de microservicios y separación de capas:

```
flightOnTime/Backend Java (Spring Boot)
│
├── backend/                       # Java/Spring
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/flightOnTime/flightOnTime/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── entity/
│   │   │   │   ├── dto/
│   │   │   │   └── FlightOnTimeApplication.java
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/java/com/flightOnTime/flightOnTime/
│   │       └── FlightOnTimeApplicationTests.java
│   ├── pom.xml
│   └── docs/                       # Documentación complementaria
│       ├── flask-setup.md
│       ├── mysql-database-setup.md
│       ├── mysql-install.md
│       └── como-ejecutar-el-proyecto-uso-local.md
│
├── frontend/                     
│   ├── index.html
│   ├── script.js
│   └── styles.css
│
├── datascience/                 
│   ├── data/
│   │   ├── flight_data.csv
│   │   ├── candidato_dataframe.ipynb
│   │   ├── champion_clima.joblib
│   │   ├── codigo_final.ipynb
│   │   └── README.txt
│   │
│   ├── pom.xml (si es Maven)
│   └── README.txt (opcional)
│
├── microservice/ # Microservicio de predicción (Python + Flask)
│ ├── api.py
│ ├── requirements.txt
│ ├── champion_clima.joblib
│ └── venv/ # (no versionado)
│
├── data/ # Data Science
│ ├── notebooks/
│ ├── datasets/
│ └── models/
└── README.md # Documentación principal

```

📌 **Nota:** algunas carpetas pueden vivir en ramas específicas durante el desarrollo, pero esta es la estructura lógica final del proyecto.

---

## 🌿 Estrategia de ramas (Branching)

### Ramas principales

- **main**  
  Rama estable del proyecto.

- **backend**  
  Desarrollo de la API REST.

- **datascience**  
  Desarrollo del modelo predictivo.

- **frontend**  
  Desarrollo y visualización del consumo de la API.

---

## ▶️ Cómo ejecutar el proyecto (uso local)
- Iniciar MySQL
- Ejecutar el backend Spring Boot desde IntelliJ
- Activar entorno virtual y ejecutar api.py
- Probar endpoints con Insomnia o Postman



## ⚙️ Configuración del microservicio

La URL del microservicio del modelo se define mediante variable de entorno:

```
http://localhost:5000

```

## 📌 Estado del proyecto

Proyecto en desarrollo como MVP para la Hackatón ONE – No Country 2025.




