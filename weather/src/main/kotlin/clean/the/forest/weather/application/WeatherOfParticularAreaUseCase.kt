package clean.the.forest.weather.application

import clean.the.forest.weather.infraestructure.AreaRepository
import clean.the.forest.weather.model.AreaName
import clean.the.forest.weather.model.WeatherCondition
import clean.the.forest.weather.model.WeatherReport
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.lang.IllegalStateException
import java.time.LocalDateTime


@Component
class WeatherOfParticularAreaUseCase(
    private val areaRepository: AreaRepository
) {

    private val weatherConditions: Map<AreaName, WeatherCondition> =
        mapOf(
            "Ipiñaburu" to "Clouds",
            "Ibarra" to "Clear",
            "Zegama" to "Drizzle"
        )

    fun report(name: AreaName): Mono<WeatherReport> {
        return areaRepository.findByName(name)
            .map { area ->
                val weatherCondition = (weatherConditions[area.name]
                    ?: throw IllegalStateException("There is no weather condition registered for [${area.name}]"))
                Pair(area, weatherCondition)
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