package clean.the.forest.area.application

import clean.the.forest.area.infrastructure.AreaRepository
import clean.the.forest.area.infrastructure.WeatherProvider
import clean.the.forest.area.model.AreaName
import clean.the.forest.area.model.WeatherReport
import reactor.core.publisher.Mono


open class ReportWeatherOfParticularAreaUseCase(
        private val areaRepository: AreaRepository,
        private val weatherProvider: WeatherProvider
) {

    open fun report(name: AreaName): Mono<WeatherReport> {

        return areaRepository.findByName(name)
                .flatMap { area ->
                    weatherProvider.byGeoPosition(area.position)
                            .map { weatherCondition -> Pair(area, weatherCondition) }
                }
                .map { (area, weatherCondition) -> WeatherReport(area = area, weatherCondition = weatherCondition) }
    }

}
