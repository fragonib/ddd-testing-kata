package clean.the.forest.area.infrastructure

import clean.the.forest.area.application.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class AreaConfig {

    @Bean
    fun inMemoryAreaRepository(): InMemoryAreaRepository {
        return InMemoryAreaRepository()
    }

    @Bean
    fun openWeatherProvider(
        @Value("\${open-weather-map.base-url}") baseUrl: String,
        @Value("\${open-weather-map.api-key}") apiKey: String
    ): WeatherGateway {
        return OpenWeatherGateway(baseUrl, apiKey)
    }

    @Bean
    fun reportOfAllKnownAreasUseCase(
            areaRepository: AreaRepository,
            weatherGateway: WeatherGateway
    ): ReportWeatherOfAllKnownAreasUseCase {
        return ReportWeatherOfAllKnownAreasUseCase(areaRepository, weatherGateway)
    }

    @Bean
    fun reportOfParticularAreaUseCase(
            areaRepository: AreaRepository,
            weatherGateway: WeatherGateway
    ): ReportWeatherOfParticularAreaUseCase {
        return ReportWeatherOfParticularAreaUseCase(areaRepository, weatherGateway)
    }

    @Bean
    fun addAreaUseCase(areaRepository: AreaRepository): AddAreaUseCase {
        return AddAreaUseCase(areaRepository)
    }

}
