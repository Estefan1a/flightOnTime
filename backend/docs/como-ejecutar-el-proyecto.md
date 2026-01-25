# ▶️ Cómo ejecutar el proyecto FlightOnTime (uso local)

Esta guía describe el **orden correcto** para ejecutar todos los componentes del proyecto **FlightOnTime** en un entorno local.

> ⚠️ Importante  
> Este documento describe **el flujo de ejecución**, pero **no sustituye** la configuración detallada de cada componente.  
> Para ello, consulte los documentos específicos enlazados en cada sección.
---

## 🧩 Componentes del sistema

El proyecto está compuesto por cuatro partes principales:

1. Base de datos MySQL
2. Microservicio de Machine Learning (Flask)
3. Backend principal (Spring Boot)
4. Frontend (HTML / CSS / JS)

> 🔴 El orden de ejecución es **crítico** para que el sistema funcione correctamente.

---

## ✅ Prerrequisitos

Antes de iniciar, asegúrese de tener:

- Tener MySQL instalado y en ejecución

  👉 [Instalación y configuración de MySQL](mysql-install.md)
- Tener creada la base de datos

  👉 [Creación de la base de datos](mysql-database-setup.md)
- Tener activo el microservicio Flask

  👉 [Configuración del microservicio Flask](flask-setup.md)
- Variables de entorno configuradas según los archivos de configuración del backend y del microservicio

  👉 [configuración de las variables de entorno](variables-de-entorno.md)

---

## Descarga del Código

En tu equipo local, con el terminal de Git abierto, ejecutar lo siguiente:

### 🔹 **Clonar el repositorio**
```
git clone https://github.com/Estefan1a/flightOnTime.git
cd flightOnTime
```
>📌 El proyecto se ejecuta desde la rama main, que contiene la estructura final del sistema.

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

---
## 🚀 Paso a paso para ejecutar el proyecto

### 1️⃣ Iniciar MySQL

Verifique que el servicio de MySQL esté activo.

Puede comprobarlo desde:
- MySQL Workbench
- Terminal
- Servicios del sistema operativo

>⚠️ Importante
> 
> La base de datos debe existir y estar accesible.

---

### 2️⃣ Ejecutar el microservicio de Machine Learning (Flask)

El backend Spring Boot depende directamente de este servicio, por lo que **debe iniciarse primero**.

```bash
cd microservice
source venv/bin/activate   # o venv\Scripts\activate en Windows
python api.py
```

El microservicio quedará disponible en:

```bash
http://localhost:5000/predict
```

Puede validarse con Postman o Insomnia enviando un POST /predict.

---
### 3️⃣ Ejecutar el backend Spring Boot

Desde IntelliJ IDEA:
1. Abrir el proyecto ubicado en /backend
2. Verificar el archivo application.properties
3. Ejecutar la clase principal del proyecto (@SpringBootApplication)

El backend quedará disponible por defecto en:

```
http://localhost:8080
```
---
### 4️⃣ Probar el flujo completo (Backend → Flask)
Puede probar el flujo completo usando:

- Postman
- Insomnia

Ejemplo de endpoint:

```bash
POST http://localhost:8080/predict
```

El backend:
- Guarda la solicitud
- Consulta el microservicio Flask
- Persiste la predicción
- Devuelve el resultado al cliente

---
### 5️⃣ Ejecutar el frontend
El frontend es estático y no requiere servidor adicional.

Opciones:

Abrir directamente:
```bash
frontend/index.html
```

O usar una extensión como **Live Server** en VS Code

El frontend consume el backend en:
```
http://localhost:8080
```

---
### 🔁 Orden recomendado de ejecución
1. MySQL
2. Microservicio Flask
3. Backend Spring Boot
4. Frontend
---
### 🧪 Verificación rápida
Si todo está funcionando correctamente:

- Flask responde en /predict
- Spring Boot arranca sin errores
- Se guardan registros en MySQL
- El frontend muestra resultados de predicción
---
### 🆘 Errores comunes
- ❌ Backend iniciado antes que Flask
- ❌ Variables de entorno no configuradas
- ❌ MySQL detenido
- ❌ Puerto ocupado (5000 / 8080)

---

## 🔗 Archivos de referencia


- [Instalación y configuración de MySQL](mysql-install.md)
- [Creación de la base de datos](mysql-database-setup.md)
- [Configuración del microservicio Flask](flask-setup.md)
- [configuración de las variables de entorno](variables-de-entorno.md)

---




