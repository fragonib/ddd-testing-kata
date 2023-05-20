import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

fun <T, U> Flux<T>.mapNotNull(mapper: (T) -> U?): Flux<U> =
        this.flatMap { Mono.justOrEmpty(mapper(it)) }
