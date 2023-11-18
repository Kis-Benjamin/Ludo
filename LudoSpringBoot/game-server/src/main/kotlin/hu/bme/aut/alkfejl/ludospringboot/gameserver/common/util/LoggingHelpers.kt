package hu.bme.aut.alkfejl.ludospringboot.gameserver.common.util

import org.slf4j.Logger

infix fun Logger.info(message: String): String {
    info(message)
    return message
}

infix fun Logger.debug(message: String): String {
    debug(message)
    return message
}

infix fun Logger.warn(message: String): String {
    warn(message)
    return message
}

infix fun Logger.error(message: String): String {
    error(message)
    return message
}

infix fun Logger.trace(message: String): String {
    trace(message)
    return message
}
