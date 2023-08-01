package com.mwguy.validation.exception

import org.springframework.validation.ObjectError

data class ValidationFailedException(val errors: List<ObjectError>) : Exception() {
    override val message: String
        get() = errors.map { it.toString() }.toString()
}
