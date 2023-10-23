plugins {
	id("org.springframework.boot") version "3.1.5" apply false
	id("io.spring.dependency-management") version "1.1.3" apply false
	kotlin("jvm") version "1.9.10" apply false
	kotlin("plugin.spring") version "1.9.10" apply false
	kotlin("plugin.jpa") version "1.9.10" apply false
}

repositories {
	mavenCentral()
	maven(url = "https://repo.spring.io/milestone")
	maven(url = "https://repository.jboss.org/maven2")
}

group = "hu.bme.aut.alkfejl.ludospringboot"
version = "0.0.1-SNAPSHOT"
description = "Ludo Spring Boot server"
