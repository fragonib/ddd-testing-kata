package clean.the.forest.area.application

import clean.the.forest.area.model.Area
import reactor.core.publisher.Mono


open class AddAreaUseCase(
        private val areaRepository: AreaRepository,
) {

    open fun addArea(area: Area): Mono<Area> {
        return areaRepository.addArea(area)
    }

}
