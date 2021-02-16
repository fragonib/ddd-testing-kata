package clean.the.forest.kata

import java.time.LocalDateTime


class CheckWeatherUseCase {

    fun weatherReport(area: Area): WeatherReport {
        return WeatherReport(
            area = area,
            weather = "Dummy",
            date = LocalDateTime.now(),
            checkout = true
        )
    }

}