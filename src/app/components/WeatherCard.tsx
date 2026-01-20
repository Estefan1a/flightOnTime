import { Cloud, CloudRain, CloudSnow, Sun, Wind, Droplets, Eye, Thermometer } from "lucide-react";
import { Card, CardContent, CardHeader, CardTitle } from "./ui/card";

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

interface WeatherCardProps {
  weather: WeatherData;
  type: "origin" | "destination";
}

const weatherIcons = {
  sunny: Sun,
  cloudy: Cloud,
  rainy: CloudRain,
  snowy: CloudSnow,
};

const weatherColors = {
  sunny: {
    bg: "bg-yellow-50",
    border: "border-yellow-200",
    header: "bg-yellow-100",
    text: "text-yellow-800",
    icon: "text-yellow-600",
  },
  cloudy: {
    bg: "bg-gray-50",
    border: "border-gray-200",
    header: "bg-gray-100",
    text: "text-gray-800",
    icon: "text-gray-600",
  },
  rainy: {
    bg: "bg-blue-50",
    border: "border-blue-200",
    header: "bg-blue-100",
    text: "text-blue-800",
    icon: "text-blue-600",
  },
  snowy: {
    bg: "bg-cyan-50",
    border: "border-cyan-200",
    header: "bg-cyan-100",
    text: "text-cyan-800",
    icon: "text-cyan-600",
  },
};

export function WeatherCard({ weather, type }: WeatherCardProps) {
  const WeatherIcon = weatherIcons[weather.condition];
  const colors = weatherColors[weather.condition];

  return (
    <Card className={`shadow-lg border-2 ${colors.border} ${colors.bg}`}>
      <CardHeader className={`${colors.header} border-b ${colors.border}`}>
        <CardTitle className={`flex items-center gap-3 ${colors.text}`}>
          <WeatherIcon className="w-8 h-8" />
          <div>
            <div className="text-sm font-normal opacity-80">
              {type === "origin" ? "Clima en Origen" : "Clima en Destino"}
            </div>
            <div>{weather.city}</div>
          </div>
        </CardTitle>
      </CardHeader>
      <CardContent className="p-6 space-y-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <Thermometer className={`w-6 h-6 ${colors.icon}`} />
            <span className="text-3xl font-bold text-gray-800">{weather.temperature}°C</span>
          </div>
          <span className="text-gray-600 capitalize">{weather.description}</span>
        </div>

        <div className="grid grid-cols-3 gap-4 pt-4 border-t border-gray-200">
          {/* Humedad */}
          <div className="flex flex-col items-center gap-1">
            <Droplets className="w-5 h-5 text-blue-500" />
            <span className="text-xs text-gray-500">Humedad</span>
            <span className="font-semibold text-gray-700">{weather.humidity}%</span>
          </div>

          {/* Viento */}
          <div className="flex flex-col items-center gap-1">
            <Wind className="w-5 h-5 text-gray-500" />
            <span className="text-xs text-gray-500">Viento</span>
            <span className="font-semibold text-gray-700">{weather.windSpeed} km/h</span>
          </div>

          {/* Visibilidad */}
          <div className="flex flex-col items-center gap-1">
            <Eye className="w-5 h-5 text-indigo-500" />
            <span className="text-xs text-gray-500">Visibilidad</span>
            <span className="font-semibold text-gray-700">{weather.visibility} km</span>
          </div>
        </div>
      </CardContent>
    </Card>
  );
}
