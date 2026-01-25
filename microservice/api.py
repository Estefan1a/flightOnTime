from dotenv import load_dotenv
import os

from flask import Flask, request, jsonify
from flask_cors import CORS
import joblib
import pandas as pd
from datetime import datetime
from functools import lru_cache
import json
import hashlib

load_dotenv(dotenv_path=".env")
# =========================================================
#                       CONFIG APP
# =========================================================
app = Flask(__name__)
CORS(app)

# =========================================================
#                        LOAD MODEL
# =========================================================
MODEL_PATH = os.getenv("MODEL_PATH")
FLASK_PORT = os.getenv("FLASK_PORT", 5000)

if not MODEL_PATH:
    raise ValueError("❌ La variable de entorno MODEL_PATH no está definida")

try:
    modelo = joblib.load(MODEL_PATH)
    print("✅ Modelo cargado exitosamente:", type(modelo))
except Exception as e:
    print("❌ Error cargando modelo:", e)
    modelo = None

# =========================================================
#        FEATURE DEFINITION (REAL MODEL CONTRACT)
# =========================================================
FEATURES_NUM = [
    'crs_dep_time',
    'crs_arr_time',
    'crs_elapsed_time',
    'distancia_km',
    'is_weekend',
    'Temperature_Celsius',
    'Wind_Speed_knots',
    'Visibility_km'
]

FEATURES_CAT = [
    'op_unique_carrier',
    'dest',
    'origin',
    'day_of_week',
    'month',
    'day_of_month',
    'bloque_horario',
    'Turbulence_Level'
]

ALL_FEATURES = FEATURES_NUM + FEATURES_CAT

# =========================================================
#            TRANSFORMATION FROM JAVA REQUEST
# =========================================================
def transformar_request_java(data: dict) -> dict:
    fecha_dt = datetime.fromisoformat(
        data['fechaPartida'].replace('Z', '+00:00')
    )

    crs_dep_time = fecha_dt.hour * 60 + fecha_dt.minute
    distancia = float(data['distanciaKm'])
    crs_elapsed_time = int((distancia / 800) * 60)
    crs_arr_time = (crs_dep_time + crs_elapsed_time) % 1440

    day_of_week = fecha_dt.isoweekday()
    is_weekend = 1 if day_of_week in [6, 7] else 0

    return {
        # ===== NUMERIC =====
        'crs_dep_time': crs_dep_time,
        'crs_arr_time': crs_arr_time,
        'crs_elapsed_time': crs_elapsed_time,
        'distancia_km': distancia,
        'is_weekend': is_weekend,
        'Temperature_Celsius': float(data.get('Temperature_Celsius', 22.0)),
        'Wind_Speed_knots': float(data.get('Wind_Speed_knots', 5.0)),
        'Visibility_km': float(data.get('Visibility_km', 10.0)),

        # ===== CATEGORICAL =====
        'op_unique_carrier': str(data['aerolinea'][:2].upper()),
        'dest': str(data['destino']),
        'origin': str(data['origen']),
        'day_of_week': day_of_week,
        'month': fecha_dt.month,
        'day_of_month': fecha_dt.day,
        'bloque_horario': int(crs_dep_time // 360),
        'Turbulence_Level': str(data.get('Turbulence_Level', 'Low'))
    }

# =========================================================
#                    DATAFRAME BUILDER
# =========================================================
def preparar_dataframe(data: dict) -> pd.DataFrame:
    df = pd.DataFrame([{col: data[col] for col in ALL_FEATURES}])

    for col in FEATURES_NUM:
        df[col] = pd.to_numeric(df[col], errors='raise')

    for col in FEATURES_CAT:
        df[col] = df[col].astype(str)

    return df

# =========================================================
#                   CACHED PREDICTION
# =========================================================
@lru_cache(maxsize=500)
def ejecutar_prediccion(data_json: str):
    data_dict = json.loads(data_json)
    df = preparar_dataframe(data_dict)

    pred = int(modelo.predict(df)[0])
    probs = modelo.predict_proba(df)[0]

    return pred, float(probs[0]), float(probs[1])

# =========================================================
#                        ENDPOINTS
# =========================================================
@app.route("/health", methods=["GET"])
def health():
    return jsonify({
        "status": "OK",
        "modelo_cargado": modelo is not None
    }), 200


@app.route("/predict", methods=["POST"])
def predict():
    if modelo is None:
        return jsonify({"error": "Modelo no cargado"}), 500

    try:
        raw = request.get_json()

        if not raw:
            return jsonify({"error": "Body vacío"}), 400

        model_data = (
            transformar_request_java(raw)
            if "fechaPartida" in raw
            else raw
        )

        # Validate contract
        missing = set(ALL_FEATURES) - set(model_data.keys())
        if missing:
            return jsonify({
                "error": f"Faltan columnas: {list(missing)}"
            }), 400

        data_json = json.dumps(model_data, sort_keys=True)
        pred, p0, p1 = ejecutar_prediccion(data_json)

        estado = "PUNTUAL" if pred == 0 else "RETRASADO"
        prob = p0 if pred == 0 else p1

        return jsonify({
            "prevision": estado,
            "probabilidad": round(prob, 4),
            "detalle": {
                "prob_a_tiempo": round(p0, 4),
                "prob_retraso": round(p1, 4)
            }
        }), 200

    except Exception as e:
        return jsonify({
            "error": f"Error interno: {str(e)}"
        }), 500


# =========================================================
#                            RUN
# =========================================================
if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=FLASK_PORT,
        debug=True
    )
