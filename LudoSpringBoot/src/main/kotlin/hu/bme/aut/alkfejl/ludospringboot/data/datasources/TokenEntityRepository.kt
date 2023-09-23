package hu.bme.aut.alkfejl.ludospringboot.data.datasources;

import hu.bme.aut.alkfejl.ludospringboot.data.entities.TokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TokenEntityRepository : JpaRepository<TokenEntity, Int>
