package hu.bme.aut.alkfejl.ludospringboot.data.datasources;

import hu.bme.aut.alkfejl.ludospringboot.data.entities.PlayerEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerEntityRepository : JpaRepository<PlayerEntity, Int>
