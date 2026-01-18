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


---

## 📡 Endpoint principal

### POST /predict

**Entrada**
```json
{
  "aerolinea": "AZ",
  "origen": "GIG",
  "destino": "GRU",
  "fecha_partida": "2025-11-10T14:30:00",
  "distancia_km": 350
}
```
**Salida**
```json
{
  "prevision": "Retrasado",
  "probabilidad": 0.78
}
```

## 📌 Estado del proyecto

Proyecto en desarrollo como MVP para la Hackatón ONE – No Country 2025.










