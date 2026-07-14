export type DailyWeatherForecast = {
  date: string;
  weatherCode: number;
  summary: string;
  maximumTemperature: number;
  minimumTemperature: number;
  precipitationProbability: number;
  rainy: boolean;
};

export type TripWeatherForecast = {
  available: boolean;
  rainyDaySuggested: boolean;
  message: string;
  days: DailyWeatherForecast[];
};
