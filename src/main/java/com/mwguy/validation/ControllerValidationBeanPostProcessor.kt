package com.mwguy.validation

import com.mwguy.validation.exception.ValidationFailedException
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.cglib.proxy.Enhancer
import org.springframework.cglib.proxy.MethodInterceptor
import org.springframework.cglib.proxy.MethodProxy
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.stereotype.Controller
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.ValidationUtils
import org.springframework.validation.Validator
import org.springframework.validation.annotation.Validated
import java.lang.reflect.Method

class ControllerValidationBeanPostProcessor(validators: List<Validator>) : BeanPostProcessor {
    private val interceptor = ControllerMethodInterceptor(validators)

    override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
        val annotation = AnnotationUtils.findAnnotation(bean.javaClass, Controller::class.java)
        if (annotation != null) {
            return Enhancer().apply {
                setSuperclass(bean.javaClass)
                setCallback(interceptor)
            }.create()
        }

        return bean
    }

    private class ControllerMethodInterceptor(val validators: List<Validator>) : MethodInterceptor {
        override fun intercept(`object`: Any, method: Method, arguments: Array<out Any>, proxy: MethodProxy): Any {
            for (index in method.parameters.indices) {
                val parameter = method.parameters[index]
                if (parameter.isAnnotationPresent(Validated::class.java)) {
                    val argument = arguments[index]
                    val result = BeanPropertyBindingResult(argument, parameter.name)

                    for (validator in validators) {
                        runCatching {
                            ValidationUtils.invokeValidator(validator, argument, result)
                        }
                    }

                    val errors = result.allErrors
                    if (errors.isNotEmpty()) {
                        throw ValidationFailedException(errors)
                    }
                }
            }

            return proxy.invokeSuper(`object`, arguments)
        }
    }
}
