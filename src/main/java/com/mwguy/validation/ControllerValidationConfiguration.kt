package com.mwguy.validation

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.validation.Validator

class ControllerValidationConfiguration {
    @Bean
    @ConditionalOnMissingBean(ControllerValidationBeanPostProcessor::class)
    fun controllerValidationBeanPostProcessor(validators: List<Validator>): ControllerValidationBeanPostProcessor =
        ControllerValidationBeanPostProcessor(validators)
}
