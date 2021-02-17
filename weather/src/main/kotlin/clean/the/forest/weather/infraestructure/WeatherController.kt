package clean.the.forest.weather.infraestructure

import clean.the.forest.weather.application.WeatherOfParticularAreaUseCase
import clean.the.forest.weather.model.WeatherReport
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/weather")
class WeatherController(
    val weatherOfParticularArea: WeatherOfParticularAreaUseCase
) {

    @GetMapping
    fun particularArea(@RequestParam("location") areaName: String): Mono<WeatherReport> {
        return weatherOfParticularArea.report(areaName)
    }

}