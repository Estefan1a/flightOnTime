# ✏️ `flask-setup.md`

---
# Configuración del microservicio Flask

## Entorno
- Python 3.10+
- Editor: Visual Studio Code
---
## Estructura recomendada del microservicio
```
microservice/
│
├── app.py                  ← (aquí va el código)
├── champion_clima.joblib   ← modelo
├── requirements.txt
└── venv/                   ← entorno virtual

```
---
## Crear entorno virtual

```bash
python -m venv venv
```
### Si no funciona, intenta:

```
py -m venv venv
```
---
## Activar el entorno virtual

En Windows (PowerShell o Git Bash):

PowerShell
```
venv\Scripts\Activate.ps1
```

Si te da error de permisos:
```
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
venv\Scripts\Activate.ps1
```
Git Bash
```
source venv/Scripts/activate
```

Cuando esté activo verás algo así:

```
(venv) usser@NITROV ...
```

## ✅ Eso significa que ya estás dentro del ambiente.

### Crear requirements.txt

Crea un archivo llamado **requirements.txt** y pon esto:

```
Flask==3.0.0
flask-cors==4.0.0
joblib==1.3.2
pandas==2.3.3
numpy==2.3.2
scikit-learn
waitress==2.1.2
```
---
## Instalar dependencias
Con el `venv` activo:
```
pip install -r requirements.txt
```
para verificar 

```
pip list
```
---
## Seleccionar el Python correcto en VS Code ⚠️ (MUY IMPORTANTE)

En VS Code:
- Ctrl + Shift + P
- Python: Select Interpreter
- Selecciona algo como:

```
Python 3.x (venv)
```
Si no haces esto, VS Code puede usar el Python global y marcar errores falsos.

## Ejecutar microservicio 🚀

En la terminal (con venv activo):

```
python api.py
```
Deberías ver algo como:

```
✅ Modelo cargado exitosamente: <class 'sklearn.pipeline.Pipeline'>
* Running on http://127.0.0.1:5000
```
---
## El microservicio quedará disponible en:
```
http://localhost:5000/predict
```
---

## Probar el endpoint /health

Abre el navegador o Postman/insonmia:

```
http://localhost:5000/health
```
## Respuesta esperada:

```
{
  "status": "OK",
  "modelo_cargado": true
}
```
## Probar /predict (ejemplo rápido)

```
POST http://localhost:5000/predict
```

Body (JSON):
```json
{
"fechaPartida": "2025-01-20T14:30:00Z",
"distanciaKm": 900,
"aerolinea": "AA",
"origen": "JFK",
"destino": "LAX"
}
```

Respuesta esperada:
```json
{
"prevision": "A TIEMPO",
"probabilidad": 0.83,
"detalle": {
"prob_a_tiempo": 0.83,
"prob_retraso": 0.17
}
}
```

---

