import { useState, useEffect } from "react";
import { useForm, Controller } from "react-hook-form";
import { Plane, AlertTriangle, CheckCircle2, CalendarIcon, Clock } from "lucide-react";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";
import { Progress } from "./ui/progress";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";
import { Calendar } from "./ui/calendar";
import { format } from "date-fns";
import { es } from "date-fns/locale";
import { WeatherCard } from "./WeatherCard";
import { FlightApiService, type FlightRequest, type PredictionResponse } from "../services/api";

type FlightFormData = {
  airline: string;
  originAirport: string;
  destinationAirport: string;
  departureDate: Date | undefined;
  departureTime: string;
  distance: number;
};

type PredictionResult = {
  onTime: {
    probability: number;
    description: string;
  };
  delayed: {
    probability: number;
    description: string;
  };
};

type WeatherCondition = "sunny" | "cloudy" | "rainy" | "snowy";

type WeatherData = {
  city: string;
  condition: WeatherCondition;
  temperature: number;
  humidity: number;
  windSpeed: number;
  visibility: number;
  description: string;
};

export function FlightPredictionForm() {
  const [predictionResult, setPredictionResult] = useState<PredictionResult | null>(null);
  const [calculatedDistance, setCalculatedDistance] = useState<number | null>(null);
  const [originWeather, setOriginWeather] = useState<WeatherData | null>(null);
  const [destinationWeather, setDestinationWeather] = useState<WeatherData | null>(null);
  
  const {
    register,
    handleSubmit,
    formState: { errors },
    watch,
    setValue,
    control,
  } = useForm<FlightFormData>();

  const originAirport = watch("originAirport");
  const destinationAirport = watch("destinationAirport");

  // Base de datos simulada de aeropuertos con coordenadas aproximadas
  const airportDatabase: { [key: string]: { lat: number; lng: number; name: string } } = {
    "BOG": { lat: 4.7016, lng: -74.1469, name: "Bogotá" },
    "BOGOTA": { lat: 4.7016, lng: -74.1469, name: "Bogotá" },
    "LIM": { lat: -12.0219, lng: -77.1143, name: "Lima" },
    "LIMA": { lat: -12.0219, lng: -77.1143, name: "Lima" },
    "MEX": { lat: 19.4363, lng: -99.0721, name: "Ciudad de México" },
    "MEXICO": { lat: 19.4363, lng: -99.0721, name: "Ciudad de México" },
    "MIA": { lat: 25.7959, lng: -80.2870, name: "Miami" },
    "MIAMI": { lat: 25.7959, lng: -80.2870, name: "Miami" },
    "MAD": { lat: 40.4983, lng: -3.5676, name: "Madrid" },
    "MADRID": { lat: 40.4983, lng: -3.5676, name: "Madrid" },
    "NYC": { lat: 40.6413, lng: -73.7781, name: "Nueva York" },
    "JFK": { lat: 40.6413, lng: -73.7781, name: "Nueva York" },
    "LAX": { lat: 33.9416, lng: -118.4085, name: "Los Ángeles" },
    "GUA": { lat: 14.5833, lng: -90.5275, name: "Guatemala" },
    "PTY": { lat: 9.0714, lng: -79.3835, name: "Panamá" },
    "PANAMA": { lat: 9.0714, lng: -79.3835, name: "Panamá" },
    "SCL": { lat: -33.3927, lng: -70.7854, name: "Santiago" },
    "SANTIAGO": { lat: -33.3927, lng: -70.7854, name: "Santiago" },
    "BUE": { lat: -34.8222, lng: -58.5358, name: "Buenos Aires" },
    "EZE": { lat: -34.8222, lng: -58.5358, name: "Buenos Aires" },
  };

  // Función para calcular la distancia entre dos coordenadas (fórmula de Haversine)
  const calculateDistance = (lat1: number, lon1: number, lat2: number, lon2: number): number => {
    const R = 6371; // Radio de la Tierra en km
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = 
      Math.sin(dLat / 2) * Math.sin(dLat / 2) +
      Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
      Math.sin(dLon / 2) * Math.sin(dLon / 2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return Math.round(R * c);
  };

  // Función para extraer código de aeropuerto
  const extractAirportCode = (input: string): string => {
    if (!input) return "";
    // Busca código entre paréntesis, ej: "Lima (LIM)" -> "LIM"
    const match = input.match(/\(([^)]+)\)/);
    if (match) return match[1].toUpperCase();
    // Si no hay paréntesis, usa todo el texto
    return input.trim().toUpperCase();
  };

  // Función para generar datos del clima simulados
  const generateWeatherData = (cityName: string): WeatherData => {
    const weatherConditions: WeatherCondition[] = ["sunny", "cloudy", "rainy", "snowy"];
    const weatherDescriptions = {
      sunny: ["Soleado", "Despejado", "Cielo claro"],
      cloudy: ["Nublado", "Parcialmente nublado", "Nubes dispersas"],
      rainy: ["Lluvia ligera", "Lluvias", "Chubascos"],
      snowy: ["Nevadas ligeras", "Nieve", "Nevando"],
    };

    // Generar condición aleatoria basada en el nombre de la ciudad (para consistencia)
    const hash = cityName.split("").reduce((acc, char) => acc + char.charCodeAt(0), 0);
    const conditionIndex = hash % weatherConditions.length;
    const condition = weatherConditions[conditionIndex];

    // Generar temperatura basada en la latitud aproximada de la ciudad
    let baseTemp = 20;
    const cityLower = cityName.toLowerCase();
    if (cityLower.includes("miami") || cityLower.includes("lima") || cityLower.includes("panamá")) {
      baseTemp = 28;
    } else if (cityLower.includes("nueva york") || cityLower.includes("madrid")) {
      baseTemp = 15;
    } else if (cityLower.includes("santiago") || cityLower.includes("buenos aires")) {
      baseTemp = 18;
    }

    const temperature = baseTemp + Math.floor(Math.random() * 10) - 5;

    return {
      city: cityName,
      condition,
      temperature,
      humidity: 50 + Math.floor(Math.random() * 40),
      windSpeed: 10 + Math.floor(Math.random() * 20),
      visibility: 8 + Math.floor(Math.random() * 5),
      description: weatherDescriptions[condition][Math.floor(Math.random() * weatherDescriptions[condition].length)],
    };
  };

  // Efecto para calcular distancia automáticamente
  useEffect(() => {
    if (originAirport && destinationAirport) {
      const originCode = extractAirportCode(originAirport);
      const destCode = extractAirportCode(destinationAirport);
      
      const origin = airportDatabase[originCode];
      const destination = airportDatabase[destCode];

      if (origin && destination) {
        const distance = calculateDistance(origin.lat, origin.lng, destination.lat, destination.lng);
        setCalculatedDistance(distance);
        setValue("distance", distance);
      } else {
        setCalculatedDistance(null);
      }
    } else {
      setCalculatedDistance(null);
    }
  }, [originAirport, destinationAirport, setValue]);

  const onSubmit = async (data: FlightFormData) => {
    try {
      // ✅ Conectar con backend real
      const flightRequest: FlightRequest = {
        aerolinea: data.airline,
        origen: extractAirportCode(data.originAirport),
        destino: extractAirportCode(data.destinationAirport),
        fechaPartida: new Date(`${format(data.departureDate!, 'yyyy-MM-dd')}T${data.departureTime}:00`).toISOString(),
        distanciaKm: data.distance
      };

      const prediction: PredictionResponse = await FlightApiService.predictPrediction(flightRequest);
      
      // Convertir respuesta del backend al formato del frontend
      const isDelayed = prediction.prevision === "RETRASADO";
      const probability = isDelayed ? prediction.probabilidad * 100 : (1 - prediction.probabilidad) * 100;

      setPredictionResult({
        onTime: {
          probability: isDelayed ? (100 - probability) : probability,
          description: isDelayed ? "Existe riesgo de retraso" : "El vuelo tiene alta probabilidad de salir puntual",
        },
        delayed: {
          probability: isDelayed ? probability : (100 - probability),
          description: isDelayed ? "Existe riesgo de retraso" : "El vuelo tiene alta probabilidad de salir puntual",
        },
      });

      // Generar datos del clima para origen y destino
      const originCode = extractAirportCode(data.originAirport);
      const destCode = extractAirportCode(data.destinationAirport);

      const originAirportData = airportDatabase[originCode];
      const destAirportData = airportDatabase[destCode];

      if (originAirportData) {
        setOriginWeather(generateWeatherData(originAirportData.name));
      }

      if (destAirportData) {
        setDestinationWeather(generateWeatherData(destAirportData.name));
      }

    } catch (error) {
      console.error('Error en la predicción:', error);
      // Fallback a simulación si falla la API
      const onTimeProbability = Math.floor(Math.random() * 30) + 60;
      const delayedProbability = 100 - onTimeProbability;

      setPredictionResult({
        onTime: {
          probability: onTimeProbability,
          description: "El vuelo tiene alta probabilidad de salir puntual",
        },
        delayed: {
          probability: delayedProbability,
          description: "Existe riesgo de retraso",
        },
      });
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100 p-4 md:p-8">
      <div className="max-w-5xl mx-auto">
        {/* Header */}
        <div className="text-center mb-8">
          <div className="flex items-center justify-center gap-3 mb-4">
            <Plane className="w-10 h-10 text-indigo-600" />
            <h1 className="text-4xl text-gray-800">Predicción de Vuelo</h1>
          </div>
          <p className="text-gray-600">Analiza la probabilidad de puntualidad de tu vuelo</p>
        </div>

        {/* Form Card */}
        <Card className="shadow-xl mb-8">
          <CardHeader className="bg-gradient-to-r from-indigo-600 to-blue-600 text-white rounded-t-lg">
            <CardTitle className="text-center text-2xl">Ingrese los datos del vuelo</CardTitle>
          </CardHeader>
          <CardContent className="p-6">
            <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
              {/* Primera fila: Aerolínea, Origen, Destino */}
              <div className="grid md:grid-cols-3 gap-6">
                {/* Aerolínea */}
                <div className="space-y-2">
                  <Label htmlFor="airline">Aerolínea</Label>
                  <Input
                    id="airline"
                    placeholder="Ej. Avianca"
                    {...register("airline", { 
                      required: "La aerolínea es requerida",
                      minLength: { value: 2, message: "Mínimo 2 caracteres" }
                    })}
                    className={errors.airline ? "border-red-500" : ""}
                  />
                  {errors.airline && (
                    <p className="text-sm text-red-600 flex items-center gap-1">
                      <AlertTriangle className="w-4 h-4" />
                      {errors.airline.message}
                    </p>
                  )}
                </div>

                {/* Aeropuerto Origen */}
                <div className="space-y-2">
                  <Label htmlFor="originAirport">Aeropuerto Origen</Label>
                  <Input
                    id="originAirport"
                    placeholder="Ej. Bogotá (BOG)"
                    {...register("originAirport", { 
                      required: "El aeropuerto de origen es requerido",
                      minLength: { value: 3, message: "Mínimo 3 caracteres" }
                    })}
                    className={errors.originAirport ? "border-red-500" : ""}
                  />
                  {errors.originAirport && (
                    <p className="text-sm text-red-600 flex items-center gap-1">
                      <AlertTriangle className="w-4 h-4" />
                      {errors.originAirport.message}
                    </p>
                  )}
                </div>

                {/* Aeropuerto Destino */}
                <div className="space-y-2">
                  <Label htmlFor="destinationAirport">Aeropuerto Destino</Label>
                  <Input
                    id="destinationAirport"
                    placeholder="Ej. Lima (LIM)"
                    {...register("destinationAirport", { 
                      required: "El aeropuerto de destino es requerido",
                      minLength: { value: 3, message: "Mínimo 3 caracteres" }
                    })}
                    className={errors.destinationAirport ? "border-red-500" : ""}
                  />
                  {errors.destinationAirport && (
                    <p className="text-sm text-red-600 flex items-center gap-1">
                      <AlertTriangle className="w-4 h-4" />
                      {errors.destinationAirport.message}
                    </p>
                  )}
                </div>
              </div>

              {/* Segunda fila: Fecha y Hora, Distancia */}
              <div className="grid md:grid-cols-2 gap-6">
                {/* Fecha y Hora */}
                <div className="space-y-2">
                  <Label>Fecha y Hora de Partida</Label>
                  <div className="grid grid-cols-2 gap-3">
                    {/* Calendario */}
                    <Controller
                      name="departureDate"
                      control={control}
                      rules={{ required: "La fecha es requerida" }}
                      render={({ field }) => (
                        <Popover>
                          <PopoverTrigger asChild>
                            <Button
                              variant="outline"
                              className={`justify-start text-left font-normal ${
                                !field.value && "text-muted-foreground"
                              } ${errors.departureDate ? "border-red-500" : ""}`}
                            >
                              <CalendarIcon className="mr-2 h-4 w-4" />
                              {field.value ? format(field.value, "dd/MM/yyyy", { locale: es }) : "Fecha"}
                            </Button>
                          </PopoverTrigger>
                          <PopoverContent className="w-auto p-0" align="start">
                            <Calendar
                              mode="single"
                              selected={field.value}
                              onSelect={field.onChange}
                              initialFocus
                              disabled={(date) => date < new Date(new Date().setHours(0, 0, 0, 0))}
                            />
                          </PopoverContent>
                        </Popover>
                      )}
                    />
                    
                    {/* Hora */}
                    <div className="relative">
                      <Input
                        type="time"
                        {...register("departureTime", { 
                          required: "La hora es requerida"
                        })}
                        className={errors.departureTime ? "border-red-500" : ""}
                      />
                      <Clock className="absolute right-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400 pointer-events-none" />
                    </div>
                  </div>
                  {(errors.departureDate || errors.departureTime) && (
                    <p className="text-sm text-red-600 flex items-center gap-1">
                      <AlertTriangle className="w-4 h-4" />
                      {errors.departureDate?.message || errors.departureTime?.message}
                    </p>
                  )}
                </div>

                {/* Distancia */}
                <div className="space-y-2">
                  <Label htmlFor="distance">Distancia (km)</Label>
                  <div className="relative">
                    <Input
                      id="distance"
                      type="number"
                      placeholder="Se calcula automáticamente"
                      {...register("distance", { 
                        required: "La distancia es requerida",
                        min: { value: 1, message: "La distancia debe ser mayor a 0" },
                        max: { value: 20000, message: "La distancia no puede exceder 20000 km" }
                      })}
                      className={`${errors.distance ? "border-red-500" : ""} ${
                        calculatedDistance ? "bg-green-50 border-green-300" : ""
                      }`}
                      readOnly={calculatedDistance !== null}
                    />
                    {calculatedDistance && (
                      <div className="absolute right-3 top-1/2 -translate-y-1/2 text-sm text-green-600 font-medium">
                        Auto
                      </div>
                    )}
                  </div>
                  {calculatedDistance && (
                    <p className="text-sm text-green-600 flex items-center gap-1">
                      <CheckCircle2 className="w-4 h-4" />
                      Distancia calculada automáticamente
                    </p>
                  )}
                  {errors.distance && !calculatedDistance && (
                    <p className="text-sm text-red-600 flex items-center gap-1">
                      <AlertTriangle className="w-4 h-4" />
                      {errors.distance.message}
                    </p>
                  )}
                </div>
              </div>

              <div className="flex justify-center pt-4">
                <Button 
                  type="submit" 
                  className="px-12 py-6 text-lg bg-gradient-to-r from-indigo-600 to-blue-600 hover:from-indigo-700 hover:to-blue-700"
                >
                  <Plane className="w-5 h-5 mr-2" />
                  PREDECIR
                </Button>
              </div>
            </form>
          </CardContent>
        </Card>

        {/* Results Section */}
        {predictionResult && (
          <>
            <div className="grid md:grid-cols-2 gap-6 mb-8">
              {/* Vuelo Puntual */}
              <Card className="shadow-lg border-2 border-green-200 bg-green-50">
                <CardHeader className="bg-green-100 border-b border-green-200">
                  <CardTitle className="flex items-center gap-3 text-green-800">
                    <CheckCircle2 className="w-8 h-8" />
                    <div>
                      <div className="text-sm font-normal text-green-600">Caso 1: Vuelo Puntual</div>
                      <div>Vuelo PUNTUAL</div>
                    </div>
                  </CardTitle>
                </CardHeader>
                <CardContent className="p-6 space-y-4">
                  <p className="text-gray-700">{predictionResult.onTime.description}</p>
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <span className="font-semibold text-gray-700">Probabilidad:</span>
                      <span className="text-2xl text-green-700">{predictionResult.onTime.probability}%</span>
                    </div>
                    <Progress value={predictionResult.onTime.probability} className="h-3 bg-green-200" />
                  </div>
                </CardContent>
              </Card>

              {/* Vuelo Retrasado */}
              <Card className="shadow-lg border-2 border-red-200 bg-red-50">
                <CardHeader className="bg-red-100 border-b border-red-200">
                  <CardTitle className="flex items-center gap-3 text-red-800">
                    <AlertTriangle className="w-8 h-8" />
                    <div>
                      <div className="text-sm font-normal text-red-600">Caso 2: Vuelo Retrasado</div>
                      <div>Vuelo RETRASADO</div>
                    </div>
                  </CardTitle>
                </CardHeader>
                <CardContent className="p-6 space-y-4">
                  <p className="text-gray-700">{predictionResult.delayed.description}</p>
                  <div className="space-y-2">
                    <div className="flex items-center justify-between">
                      <span className="font-semibold text-gray-700">Probabilidad:</span>
                      <span className="text-2xl text-red-700">{predictionResult.delayed.probability}%</span>
                    </div>
                    <Progress value={predictionResult.delayed.probability} className="h-3 bg-red-200" />
                  </div>
                </CardContent>
              </Card>
            </div>

            {/* Weather Section */}
            {(originWeather || destinationWeather) && (
              <div className="grid md:grid-cols-2 gap-6">
                {originWeather && <WeatherCard weather={originWeather} type="origin" />}
                {destinationWeather && <WeatherCard weather={destinationWeather} type="destination" />}
              </div>
            )}
          </>
        )}

        {/* Error Message Info */}
        {!predictionResult && Object.keys(errors).length === 0 && (
          <Card className="shadow-lg bg-blue-50 border-blue-200">
            <CardContent className="p-6 text-center text-gray-600">
              <p className="text-lg">Complete el formulario para obtener la predicción del vuelo</p>
            </CardContent>
          </Card>
        )}
      </div>
    </div>
  );
}