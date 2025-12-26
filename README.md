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
- Reinaldo Blanco  

### Data Science
- Felipe Rojas (Data Engineer)  
- Luis Cavero (Data Engineer)  
- Cristian Saenz (Data Scientist)  
- Juan Martínez (Data Scientist)  
- Felipe Guzmán de la Fuente (Data Scientist)  

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

---

## 📁 Estructura del repositorio

```
flightOnTime/
│
├── backend/
├── datascience/
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

funcionalidad: agregar endpoint POST /predict




## ▶️ Cómo ejecutar el proyecto (uso local)

A continuación se describen los pasos para ejecutar el proyecto de manera local.

## 🔹 Clonar el repositorio

```bash
git clone https://github.com/TU-USUARIO/flightOnTime.git
cd flightOnTime
```

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

## 🧩 SECCIÓN 2: Cómo probar la API

Pégala **justo después del endpoint que ya tienes** (POST /predict):


## 🧪 Cómo probar la API

Una vez que el backend esté en ejecución, puedes probar el endpoint usando **Postman** o **Insomnia**.

### 🔹 Endpoint disponible
```
POST http://localhost:8080/predict
```

### 🔹 Headers
```
Content-Type: application/json
```

### 🔹 Ejemplo de petición

json
{
  "aerolinea": "AZ",
  "origen": "GIG",
  "destino": "GRU",
  "fecha_partida": "2025-11-10T14:30:00",
  "distancia_km": 350
}

🔹 Ejemplo de respuesta
{
  "prevision": "Retrasado",
  "probabilidad": 0.78
}

## 🤝 Integración con Data Science

El backend está diseñado para integrarse con un modelo predictivo desarrollado por el equipo de Data Science.

- El modelo será entrenado en Python
- Exportado en formato `.joblib`
- La lógica actual del backend utiliza una predicción simulada
- Esta lógica será reemplazada por la integración con el modelo real

Esto permite desarrollar el backend y el modelo de forma paralela.

## 📌 Estado del proyecto

Proyecto en desarrollo como MVP para la Hackatón ONE – No Country 2025.










