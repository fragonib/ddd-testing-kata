package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.AddAreaUseCase
import clean.the.forest.area.model.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono


@RestController
@RequestMapping("/area")
class AreaController(
    val addAreaUseCase: AddAreaUseCase
) {

    @PostMapping
    fun addArea(@RequestBody areaData: AreaDTO): Mono<ResponseEntity<Area>> {
        return addAreaUseCase.addArea(areaData.toModel())
            .map { ResponseEntity.status(HttpStatus.CREATED).body(it) }
            .onErrorResume(ConflictWithExistingArea::class.java) { Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).build()) }
    }

}

data class AreaDTO(
    val name: String,
    val lat: Double,
    val lon: Double,
    val countryCode: String
) {
    fun toModel(): Area {
        return Area(name, lat, lon, countryCode)
    }
}
