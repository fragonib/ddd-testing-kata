package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.ReportWeatherOfAllKnownAreasUseCase
import clean.the.forest.area.application.ReportCheckabilityOfParticularAreaUseCase
import clean.the.forest.area.model.WeatherReport
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.kotlin.core.publisher.toFlux


@RestController
@RequestMapping("/weather")
class WeatherController(
    val reportWeatherOfAllKnownAreas: ReportWeatherOfAllKnownAreasUseCase,
    val reportCheckabilityOfParticularArea: ReportCheckabilityOfParticularAreaUseCase
) {

    @GetMapping
    fun report(
        @RequestParam(name = "location", required = false) particularAreaName: String?
    ): Flux<out WeatherReport> {
        return if (particularAreaName == null)
            reportWeatherOfAllKnownAreas.report()
        else
            reportCheckabilityOfParticularArea.report(particularAreaName).toFlux()
    }

}