package hu.bme.aut.alkfejl.ludospringboot.web

import hu.bme.aut.alkfejl.ludospringboot.data.datasources.GameEntityRepository
import hu.bme.aut.alkfejl.ludospringboot.domain.model.*
import hu.bme.aut.alkfejl.ludospringboot.domain.services.Registrar
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class GameController(
    private val gameEntityRepository: GameEntityRepository,
    private val registrar: Registrar,
) {
    @GetMapping("/games")
    fun getAll(): List<Game> = gameEntityRepository.findAll().map { it.toDomainModel() }

    @PostMapping("/games")
    fun register(@RequestBody name: String) = registrar.register(name)

    @PostMapping("/games/{id}/select/{name}")
    fun select(@RequestParam id: Int, @RequestParam name: String) {
        val gameEntity = gameEntityRepository.findById(id).get()
        val game = gameEntity.toDomainModel()
        game.select(name)
        gameEntity.update(game)
        gameEntityRepository.save(gameEntity)
    }

    @PostMapping("/games/{id}/step/{name}")
    fun step(@PathVariable(name = "id") id: Int, @PathVariable(name = "name") name: String) {
        val gameEntity = gameEntityRepository.findById(id).get()
        val game = gameEntity.toDomainModel()
        game.step(name)
        gameEntity.update(game)
        gameEntityRepository.save(gameEntity)
    }
}
