package hu.bme.aut.alkfejl.ludospringboot.data.datasources;

import hu.bme.aut.alkfejl.ludospringboot.data.entities.GameEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GameEntityRepository : JpaRepository<GameEntity, Int>

