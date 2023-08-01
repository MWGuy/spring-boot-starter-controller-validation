# Spring controller validation ![Release](https://jitpack.io/v/MWGuy/spring-boot-starter-controller-validation.svg)

Spring boot starter that enables [bean validator](https://docs.spring.io/spring-framework/reference/core/validation/validator.html) in controller beans.

## Example

```kotlin
import org.springframework.validation.annotation.Validated
import com.mwguy.validation.EnableControllerValidation

@RestController
class SimpleController {
    @PostMapping
    suspend fun doPostJob(@RequestBody @Validated payload: PostPayLoad): String {
        return "Hello, ${payload.message}"
    }

    class PostPayLoad(val message: String? = null)
}

@Component
class SimpleValidator : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return clazz == SimpleController.PostPayLoad::class.java
    }

    override fun validate(target: Any, errors: Errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "message", "message.empty")
    }
}

@SpringBootApplication
@EnableControllerValidation
class SpringCoreValidationApplication

fun main(args: Array<String>) {
    runApplication<SpringCoreValidationApplication>(*args)
}
```

## Installing

First, add [jitpack](https://jitpack.io) maven repository:

```kotlin
repositories {
    maven("https://jitpack.io")
}
```

Second, add dependency:

```kotlin
dependencies {
    implementation("com.github.MWGuy:spring-boot-starter-controller-validation:1.0")
}
```

## Using library

First of all you need to enable controller validation with ``@EnableControllerValidation`` annotation.

Then you need to create controller advice which handles ``com.mwguy.validation.exception.ValidationFailedException``.

Example controller advice implementation:

```kotlin
import com.mwguy.validation.exception.ValidationFailedException

@ControllerAdvice
class ValidationControllerAdvice {
    @ExceptionHandler(ValidationFailedException::class)
    fun handleValidationException(exception: ValidationFailedException): ResponseEntity<List<ValidationErrorDetail>> {
        return ResponseEntity.badRequest().body(exception.errors.map {
            ValidationErrorDetail(it.objectName, it.defaultMessage ?: it.code)
        })
    }

    data class ValidationErrorDetail(val path: String, val message: String?)
}
```
