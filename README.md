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

## 🧑‍🤝‍🧑 Equipo

### Backend
- Estefanía González  
- Alejandro Montoya Torres  
- Javier Alberto Chávez Córdova  
- Juan Gómez Martínez
- Gabriel Romero

### Data Science
- Felipe Rojas  *
- Luis Cavero  
- Cristian Saenz  
- Juan Martínez   

---

## 🛠️ Tecnologías

### Backend
- Java  
- Spring Boot  
- Maven  
- API REST  

### Data Science
- Python  
- Pandas  
- scikit-learn  
- Jupyter Notebook

## 📦 Dependencias y versiones

### Backend
- Java 17
- Spring Boot 3.4.1
- Maven 4.0
- Lombok

### Data Science
- Python 
- pandas
- scikit-learn
- joblib
- FastAPI / Flask (para exponer el modelo)

### Frontend
- React
- TypeScript
- Tailwind CSS
- Vite

---

## 📁 Estructura del repositorio

```
flightOnTime/
│
├── backend/
├── datascience/
├── frontend/
└── README.md
```

---

## 🌿 Estrategia de ramas (Branching)

### Ramas principales

- **main**  
  Rama estable del proyecto.

- **backend**  
  Desarrollo de la API REST.

- **datascience**  
  Desarrollo del modelo predictivo.

### Ramas temporales (si se requieren)

Ejemplos:
- `backend/endpoint-predict`
- `datascience/entrenamiento-modelo`

---

## 🧾 Reglas de commits

Para mantener orden y trazabilidad:
- Commits pequeños y claros
- Un commit por cambio lógico
- Mensajes descriptivos

Formato:

```
tipo: descripción corta
```

Tipos:
- funcionalidad  
- correccion  
- documentacion  
- configuracion  

Ejemplo:
```
funcionalidad: agregar endpoint POST /predict
```



## ▶️ Cómo ejecutar el proyecto (uso local)

A continuación se describen los pasos para ejecutar el proyecto de manera local.

## 🔹 Clonar el repositorio

```bash
git clone https://github.com/Estefan1a/flightOnTime.git
cd flightOnTime
```
## Ejecutar el Frontend
  Run `npm i` to install the dependencies.
  Run `npm run dev` to start the development server.

## Ejecutar el Backend


Cambiar a la rama backend:
```
git checkout backend
```

Entrar a la carpeta del backend:
```
cd backend
```

## Ejecutar la aplicación:

```
mvn spring-boot:run
```

## La API se levantará en:
```
http://localhost:8080
```


##📤 Ejemplos de petición y respuesta

### 🧪 Endpoint principal


```
POST /predict
```

### 🔹 Headers
```
Content-Type: application/json
```

### 🔹 Ejemplo de petición

```
json
{
  "aerolinea": "AZ",
  "origen": "GIG",
  "destino": "GRU",
  "fecha_partida": "2025-11-10T14:30:00",
  "distancia_km": 350
}
```

🔹 Ejemplo de respuesta
```
{
  "prevision": "Retrasado",
  "probabilidad": 0.78
}
```

## 🤝 Integración con Data Science

El backend está diseñado para integrarse con un modelo predictivo desarrollado por el equipo de Data Science.

### 🔹 Estrategia de integración

- El modelo será entrenado en Python
- Exportado en formato `.joblib`
- El modelo se expone mediante un microservicio REST (FastAPI o Flask).
- El Backend consume dicho servicio a través de una llamada HTTP.

Mientras el modelo real está en desarrollo, el backend utiliza una predicción simulada (fake) para pruebas y validaciones.

Cuando el modelo real esté disponible, solo será necesario actualizar el cliente de integración.

## ⚙️ Configuración del servicio de Data Science

La URL del microservicio del modelo se define mediante variable de entorno:

```
http://localhost:8080/swagger-ui/index.html



```

## 📌 Estado del proyecto

Proyecto en desarrollo como MVP para la Hackatón ONE – No Country 2025.










