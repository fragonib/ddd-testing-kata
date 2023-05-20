package clean.the.forest.area.application

import clean.the.forest.area.domain.WeatherReport
import reactor.core.publisher.Flux


open class ReportWeatherOfAllKnownAreasUseCase(
        private val areaRepository: AreaRepository,
        private val weatherGateway: WeatherGateway
) {

    open fun report(): Flux<WeatherReport> {

        return areaRepository.allKnown()
                .flatMap { area ->
                    weatherGateway.byGeoPosition(area.position)
                            .map { weatherCondition -> Pair(area, weatherCondition) }
                }
                .map { (area, weatherCondition) -> WeatherReport(area = area, weatherCondition = weatherCondition) }
    }

}
