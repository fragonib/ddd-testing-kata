package clean.the.forest.weather.infraestructure.config

import clean.the.forest.weather.application.WeatherOfParticularAreaUseCase
import clean.the.forest.weather.infraestructure.AreaRepository
import clean.the.forest.weather.infraestructure.InMemoryAreaRepository
import clean.the.forest.weather.infraestructure.OpenWeatherProvider
import clean.the.forest.weather.infraestructure.WeatherProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.beans.factory.annotation.Value


@Configuration
class WeatherConfig {

    @Bean
    fun inMemoryAreaRepository(): InMemoryAreaRepository {
        return InMemoryAreaRepository()
    }

    @Bean
    fun weatherOfParticularAreaUseCase(
        areaRepository: AreaRepository, weatherProvider: WeatherProvider
    ): WeatherOfParticularAreaUseCase {
        return WeatherOfParticularAreaUseCase(areaRepository, weatherProvider)
    }

    @Bean
    fun openWeatherProvider(@Value("\${OPENWEATHER_APIKEY}") apiKey: String): WeatherProvider {
        return OpenWeatherProvider(apiKey)
    }

}