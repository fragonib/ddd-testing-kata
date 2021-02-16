package clean.the.forest.kata

import java.time.LocalDateTime


class CheckWeatherUseCase {

    fun checkWeather(area: Area): WeatherReport {
        return WeatherReport(
            area = area,
            weather = "Dummy",
            date = LocalDateTime.now(),
            checkout = true
        )
    }

}