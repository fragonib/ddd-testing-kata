package clean.the.forest.area.application

import clean.the.forest.area.infrastructure.AreaRepository
import clean.the.forest.area.infrastructure.WeatherProvider
import clean.the.forest.area.model.WeatherReport
import reactor.core.publisher.Flux
import java.time.LocalDateTime


open class ReportAllKnownAreasUseCase(
    private val areaRepository: AreaRepository,
    private val weatherProvider: WeatherProvider
) {

    open fun report(): Flux<WeatherReport> {

        return areaRepository.allKnown()
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