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
    val reportAllKnownAreas: ReportWeatherOfAllKnownAreasUseCase,
    val reportCheckabilityOfParticularAreaUseCase: ReportCheckabilityOfParticularAreaUseCase
) {

    @GetMapping
    fun report(
        @RequestParam(name = "location", required = false) particularAreaName: String?
    ): Flux<WeatherReport> {
        return if (particularAreaName == null)
            reportAllKnownAreas.report()
        else
            reportCheckabilityOfParticularAreaUseCase.report(particularAreaName).toFlux()
    }

}