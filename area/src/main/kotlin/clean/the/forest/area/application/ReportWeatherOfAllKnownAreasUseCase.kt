package clean.the.forest.area.application

import clean.the.forest.area.model.WeatherReport
import reactor.core.publisher.Flux


open class ReportWeatherOfAllKnownAreasUseCase(
        private val areaRepository: AreaRepository,
        private val weatherProvider: WeatherProvider
) {

    open fun report(): Flux<WeatherReport> {

        return areaRepository.allKnown()
                .flatMap { area ->
                    weatherProvider.byGeoPosition(area.position)
                            .map { weatherCondition -> Pair(area, weatherCondition) }
                }
                .map { (area, weatherCondition) -> WeatherReport(area = area, weatherCondition = weatherCondition) }
    }

}
