package clean.the.forest.weather.infrastructure

import clean.the.forest.weather.application.ReportAllKnownAreasUseCase
import clean.the.forest.weather.application.ReportParticularAreaUseCase
import clean.the.forest.weather.model.WeatherReport
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/weather")
class WeatherController(
    val reportAllKnownAreas: ReportAllKnownAreasUseCase,
    val reportParticularAreaUseCase: ReportParticularAreaUseCase
) {

    @GetMapping
    fun allKnownAreas(): Flux<WeatherReport> {
        return reportAllKnownAreas.report()
    }

    @GetMapping
    fun particularArea(@RequestParam("location") areaName: String): Mono<WeatherReport> {
        return reportParticularAreaUseCase.report(areaName)
    }

}