package hu.bme.aut.alkfejl.ludospringboot

import hu.bme.aut.alkfejl.ludospringboot.data.datasources.GameEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.data.entities.GameEntity
import hu.bme.aut.alkfejl.ludospringboot.domain.model.rollDice
import hu.bme.aut.alkfejl.ludospringboot.domain.model.toDomainModel
import hu.bme.aut.alkfejl.ludospringboot.domain.model.update
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LudoSpringBootApplication(
	private val gameEntityRepository: GameEntityRepository,
) : CommandLineRunner {
	override fun run(vararg args: String?) {
		val gameEntity = GameEntity.fromNames(listOf("Alice", "Bob", "Charlie", "David"))
		val game = gameEntity.toDomainModel()
		game.rollDice()
		gameEntity.update(game)
		gameEntityRepository.save(gameEntity)
		val gameEntities = gameEntityRepository.findAll()
		repeat(gameEntities.size) { gameIndex ->
			println(gameEntities[gameIndex])
			repeat(gameEntities[gameIndex].players.size) { playerIndex ->
				println(gameEntities[gameIndex].players[playerIndex])
				repeat(gameEntities[gameIndex].players[playerIndex].tokens.size) { tokenIndex ->
					println(gameEntities[gameIndex].players[playerIndex].tokens[tokenIndex])
				}
			}
		}
	}
}

fun main(args: Array<String>) {
	runApplication<LudoSpringBootApplication>(*args)
}
