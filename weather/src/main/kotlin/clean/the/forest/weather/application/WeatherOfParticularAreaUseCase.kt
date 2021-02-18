package clean.the.forest.weather.application

import clean.the.forest.weather.infraestructure.AreaRepository
import clean.the.forest.weather.infraestructure.WeatherProvider
import clean.the.forest.weather.model.AreaName
import clean.the.forest.weather.model.WeatherReport
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.time.LocalDateTime


@Component
class WeatherOfParticularAreaUseCase(
    private val areaRepository: AreaRepository,
    private val weatherProvider: WeatherProvider
) {

    fun report(name: AreaName): Mono<WeatherReport> {
        return areaRepository.findByName(name)
            .flatMap { area ->
                weatherProvider.reportWeatherByGeoPos(area.position)
                    .map { weatherCondition -> Pair(area, weatherCondition) }
            }
            .map { (area, weatherCondition) ->
                WeatherReport(
                    area = area,
                    weatherCondition = weatherCondition,
                    date = LocalDateTime.now()
                )
            }
    }

}