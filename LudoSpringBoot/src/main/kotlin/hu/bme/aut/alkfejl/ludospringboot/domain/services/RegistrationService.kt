package hu.bme.aut.alkfejl.ludospringboot.domain.services

import hu.bme.aut.alkfejl.ludospringboot.data.datasources.GameEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.data.entities.GameEntity
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service

@Service
class RegistrationService {

    @Bean
    fun provideRegistrar(
        gameEntityRepository: GameEntityRepository,
    ) = Registrar(
        gameEntityRepository = gameEntityRepository,
    )
}

class Registrar(
    private val gameEntityRepository: GameEntityRepository,
) {
    private val names = mutableListOf<String>()

    fun register(name: String) {
        names.add(name)

        if (names.size == 4) {
            val game = GameEntity.fromNames(names)
            gameEntityRepository.save(game)
            names.clear()
        }
    }
}
