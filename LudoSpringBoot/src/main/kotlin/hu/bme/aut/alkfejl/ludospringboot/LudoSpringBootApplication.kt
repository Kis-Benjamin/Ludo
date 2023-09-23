package hu.bme.aut.alkfejl.ludospringboot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LudoSpringBootApplication

fun main(args: Array<String>) {
	runApplication<LudoSpringBootApplication>(*args)
}
