===========================================================================
    REPORTE TÉCNICO: PREDICCIÓN DE RETRASOS EN VUELOS COMERCIALES
===========================================================================

1. RESUMEN DEL PROYECTO
-----------------------
Este documento detalla la fase de ingeniería de datos y modelado base para 
la predicción de la probabilidad de retrasos en operaciones aéreas. El 
objetivo es transformar datos logísticos y ambientales en un dataset 
optimizado para el aprendizaje supervisado.

2. PIPELINE DE PROCESAMIENTO DE DATOS (INGENIERÍA)
--------------------------------------------------
A. Limpieza y Consolidación:
   - Tratamiento de Nulos: Imputación estadística para variables 
     climáticas y depuración de registros con datos faltantes críticos.
   - Tipado de Datos: Conversión de variables de visibilidad y tiempo a 
     formatos numéricos (float64) para asegurar compatibilidad con 
     algoritmos de gradiente.

B. Ingeniería de Características (Feature Engineering):
   - Creación de "Bloques Horarios": Segmentación de horarios de salida en 
     categorías (Madrugada, Mañana, Tarde, Noche) para capturar ciclos de 
     congestión aeroportuaria.
   - Normalización de Destinos: Limpieza y consolidación de nodos de 
     origen y destino para reducir la cardinalidad categórica.

C. Transformación Automatizada (ColumnTransformer):
   - Variables Numéricas: Aplicación de StandardScaler para normalizar 
     escalas en distancias y temperaturas, evitando sesgos por magnitudes.
   - Variables Categóricas: Implementación de OneHotEncoder,  y target encoder con política 
     de "handle_unknown='ignore'" y eliminación de redundancia binaria.

3. ESTRATEGIA DE MODELADO Y SELECCIÓN
-------------------------------------
- Modelos Evaluados: Se priorizó el uso de HistGradientBoostingClassifier 
  debido a su alta eficiencia en el manejo de grandes volúmenes de datos y 
  soporte nativo para nulos.

- Decisión Técnica (Recall vs. Accuracy):
  Se tomó la decisión estratégica de priorizar el RECALL sobre el ACCURACY 
  puro. Se seleccionó un umbral de decisión de 0.50.

  Justificación: En logística aérea, un "Falso Negativo" (un retraso no 
  detectado) tiene un costo operativo y de satisfacción al cliente mucho 
  mayor que un "Falso Positivo". Maximizar la sensibilidad del modelo 
  (Recall) permite una respuesta proactiva.

4. RESULTADOS OBTENIDOS (LÍNEA BASE)
------------------------------------
- Accuracy Global: 67.4%
- Recall (Clase 1 - Retrasos): 0.60
- Precision (Clase 0 - A tiempo): 0.75

[Nota: Se observó una convergencia en el rendimiento, indicando que se ha 
alcanzado el límite de extracción de información para el set de 
características disponible, se confirma al utilizar un modelo Xgboost con contraste.]

5. CONCLUSIONES TÉCNICAS
------------------------
El modelo base demuestra una robustez significativa en la clasificación de 
vuelos puntuales y una capacidad aceptable para predecir contingencias. La 
arquitectura de Pipeline implementada garantiza que el modelo sea 
escalable y esté listo para recibir nuevos datos sin riesgo de fugas de 
información (Data Leakage).

6. CONSIDERACIONES PARA EL USUARIO
----------------------------------
- Cuaderno Principal: El archivo "codigo_final.ipynb" contiene el flujo 
  completo, la documentación técnica y la implementación final del modelo.
  
- Modelos Champion: Los archivos con el prefijo "champion" representan las 
  versiones finales optimizadas. Específicamente, "champion_clima.joblib" 
  es el modelo entrenado con la integración de variables numéricas y 
  ambientales.

- Antecedentes: El cuaderno "candidato_dataframe.ipynb" constituye el 
  trabajo preliminar de exploración de datos. Este sentó las bases del 
  proyecto, aunque su estructura técnica fue depurada y migrada 
  íntegramente al archivo principal "codigo_final.ipynb" para mayor 
  legibilidad y eficiencia.

===========================================================================
Generado para Hackathon Alura One - 2026
===========================================================================