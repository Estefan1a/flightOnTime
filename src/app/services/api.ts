// src/app/services/api.ts
const API_BASE_URL = 'http://localhost:8080';

export interface FlightRequest {
  aerolinea: string;
  origen: string;
  destino: string;
  fechaPartida: string;
  distanciaKm: number;
}

export interface PredictionResponse {
  prevision: string;
  probabilidad: number;
  weather?: {
    temperatura: number;
    viento: number;
  };
}

export interface PredictionHistory {
  id: number;
  aerolinea: string;
  origen: string;
  destino: string;
  fechaPartida: string;
  prevision: string;
  probabilidad: number;
  fechaPrediccion: string;
}

export class FlightApiService {
  static async predictPrediction(flightData: FlightRequest): Promise<PredictionResponse> {
    const response = await fetch(`${API_BASE_URL}/predict`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(flightData),
    });

    if (!response.ok) {
      throw new Error('Error en la predicción');
    }

    return response.json();
  }

  static async getPredictionHistory(page: number = 0, size: number = 10): Promise<PredictionHistory[]> {
    const response = await fetch(`${API_BASE_URL}/predict/history?page=${page}&size=${size}`);

    if (!response.ok) {
      throw new Error('Error obteniendo historial');
    }

    return response.json();
  }
}
