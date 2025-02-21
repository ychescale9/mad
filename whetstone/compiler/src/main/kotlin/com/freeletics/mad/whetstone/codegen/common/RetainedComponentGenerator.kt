package com.freeletics.mad.whetstone.codegen.common

import com.freeletics.mad.whetstone.CommonData
import com.freeletics.mad.whetstone.ComposeFragmentData
import com.freeletics.mad.whetstone.ComposeScreenData
import com.freeletics.mad.whetstone.RendererFragmentData
import com.freeletics.mad.whetstone.codegen.util.Generator
import com.freeletics.mad.whetstone.codegen.util.bindsInstanceParameter
import com.freeletics.mad.whetstone.codegen.util.bundle
import com.freeletics.mad.whetstone.codegen.util.componentAnnotation
import com.freeletics.mad.whetstone.codegen.util.componentFactory
import com.freeletics.mad.whetstone.codegen.util.composeProviderValueModule
import com.freeletics.mad.whetstone.codegen.util.compositeDisposable
import com.freeletics.mad.whetstone.codegen.util.coroutineScope
import com.freeletics.mad.whetstone.codegen.util.internalApiAnnotation
import com.freeletics.mad.whetstone.codegen.util.providedValue
import com.freeletics.mad.whetstone.codegen.util.savedStateHandle
import com.freeletics.mad.whetstone.codegen.util.scopeToAnnotation
import com.freeletics.mad.whetstone.codegen.util.simplePropertySpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.KModifier.ABSTRACT
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.SET
import com.squareup.kotlinpoet.STAR
import com.squareup.kotlinpoet.TypeSpec

internal val Generator<out CommonData>.retainedComponentClassName get() = ClassName("Retained${data.baseName}Component")

internal const val retainedComponentFactoryCreateName = "create"

internal const val providedValueSetPropertyName = "providedValues"

internal class RetainedComponentGenerator(
    override val data: CommonData,
) : Generator<CommonData>() {

    fun generate(): TypeSpec {
        return TypeSpec.interfaceBuilder(retainedComponentClassName)
            .addModifiers(KModifier.INTERNAL)
            .addAnnotation(internalApiAnnotation())
            .addAnnotation(scopeToAnnotation(data.scope))
            .addAnnotation(componentAnnotation(data.scope, data.dependencies, moduleClassName()))
            .addProperties(componentProperties())
            .addType(retainedComponentFactory())
            .build()
    }

    private fun moduleClassName(): ClassName? {
        return when (data) {
            is ComposeFragmentData -> composeProviderValueModule
            is ComposeScreenData -> composeProviderValueModule
            is RendererFragmentData -> null
        }
    }

    private fun componentProperties(): List<PropertySpec> {
        val properties = mutableListOf<PropertySpec>()
        properties += simplePropertySpec(data.stateMachine)
        if (data.navigation != null) {
            properties += simplePropertySpec(data.navigation!!.navigator)
            properties += simplePropertySpec(data.navigation!!.navigationHandler)
        }
        properties += when (data) {
            is ComposeFragmentData -> providedValueSetProperty()
            is ComposeScreenData -> providedValueSetProperty()
            is RendererFragmentData -> simplePropertySpec(data.factory)
        }
        return properties
    }

    private fun providedValueSetProperty(): PropertySpec {
        val type = SET.parameterizedBy(providedValue.parameterizedBy(STAR))
        return PropertySpec.builder(providedValueSetPropertyName, type).build()
    }

    private val retainedComponentFactoryClassName = retainedComponentClassName.peerClass("Factory")

    private fun retainedComponentFactory(): TypeSpec {
        val createFun = FunSpec.builder(retainedComponentFactoryCreateName)
            .addModifiers(ABSTRACT)
            .addParameter("dependencies", data.dependencies)
            .addParameter(bindsInstanceParameter("savedStateHandle", savedStateHandle))
            .addParameter(bindsInstanceParameter("arguments", bundle))
            .apply {
                if (data.rxJavaEnabled) {
                    addParameter(bindsInstanceParameter("compositeDisposable", compositeDisposable))
                }
                if (data.coroutinesEnabled) {
                    addParameter(bindsInstanceParameter("coroutineScope", coroutineScope))
                }
            }
            .returns(retainedComponentClassName)
            .build()
        return TypeSpec.interfaceBuilder(retainedComponentFactoryClassName)
            .addAnnotation(componentFactory)
            .addFunction(createFun)
            .build()
    }
}
