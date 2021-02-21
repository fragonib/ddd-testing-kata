package clean.the.forest.weather.application

import clean.the.forest.weather.infrastructure.AreaRepository
import clean.the.forest.weather.infrastructure.WeatherProvider
import clean.the.forest.weather.model.AreaName
import clean.the.forest.weather.model.WeatherReport
import reactor.core.publisher.Mono
import java.time.LocalDateTime


open class ReportParticularAreaUseCase(
    private val areaRepository: AreaRepository,
    private val weatherProvider: WeatherProvider
) {

    open fun report(name: AreaName): Mono<WeatherReport> {

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