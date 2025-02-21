package com.freeletics.mad.whetstone

import androidx.fragment.app.DialogFragment
import com.freeletics.mad.whetstone.internal.EmptyNavigationHandler
import com.freeletics.mad.whetstone.internal.EmptyNavigator
import com.freeletics.mad.navigator.NavigationHandler
import com.freeletics.mad.navigator.Navigator
import com.gabrielittner.renderer.ViewRenderer
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.reflect.KClass

/**
 * By adding this annotation to a [com.gabrielittner.renderer.ViewRenderer] class, a
 * DialogFragment is generated that will have the same name with `Fragment` as suffix. The
 * DialogFragment will use the given [dialogFragmentBaseClass] as it's super class and
 * the [rendererFactory] as it's view.
 *
 * **StateMachine**
 *
 * The `StateMachine` will be collected while the `Fragment` is at least in a
 * [androidx.lifecycle.Lifecycle.State.STARTED] state and the emitted state will
 * be passed to the `render` function of the `Renderer`. The `actions` of the `Renderer will also
 * be collected in the mean time and dispatched to the `StateMachine`.
 *
 * **Navigation**
 *
 * If the 2 optional `navigator` and `navigationHandler` are set the generated `Fragment` will use
 * these together with it's `NavController` to set up navigation after it was created.
 *
 * **Scopes, Dagger and Anvil**
 *
 * There will also be a generated Dagger component that is tied to the composable function but
 * survives configuration changes and stays alive while the composable destination is on the
 * backstack.
 *
 * The generated component uses [ScopeTo] as it's scope where the [ScopeTo.marker]
 * parameter is the specified [scope] class. This scope can be used to scope classes
 * in the component an tie them to to component's life time. The annotated class is also used as
 * [com.squareup.anvil.annotations.MergeComponent.scope], so it can be used to contribute modules
 * and bindings to the generated component.
 *
 * E.g. for `@RendererFragment(scope = CoachScreen::class, ...)` the scope of the generated
 * component will be `@ScopeTo(CoachScreen::class)`, modules can be contributed with
 * `@ContributesTo(CoachScreen::class)` and bindings with
 * `@ContributesBinding(CoachScreen::class, ...)`.
 *
 * By default the following classes are available for injection in the component:
 * - everything accessible through the provided [dependencies] interface
 * - a [androidx.lifecycle.SavedStateHandle]
 * - a [android.os.Bundle] obtained from [androidx.navigation.NavController.currentBackStackEntry]
 * - if [coroutinesEnabled] is `true`, a [kotlinx.coroutines.CoroutineScope] that will be cancelled automatically
 * - if [rxJavaEnabled] is `true`, a [io.reactivex.disposables.CompositeDisposable] that will be cleared automatically
 *
 * The mentioned [dependencies] interface will be looked up by calling
 * [android.content.Context.getSystemService] on either the [android.app.Activity] context or the
 * [android.app.Application] context. The parameter passed to `getSystemService` is the fully
 * qualified name of [parentScope]. It is recommended to use the same marker class that is used as
 * Anvil scope for the [parentScope].
 */
@Target(CLASS)
@Retention(RUNTIME)
annotation class RendererDialogFragment(
    val scope: KClass<*>,
    val parentScope: KClass<*>,
    val dependencies: KClass<*>,

    //TODO should be KClass<out ViewRenderer.Factory<*, *>>
    // leaving out the constraint for now to be compatible with some custom factories using the same signature
    val rendererFactory: KClass<*>,
    //TODO should be KClass<out StateMachine<*, *>>
    // leaving out the constraint for now to be compatible with Renderer's LiveDataStateMachine
    val stateMachine: KClass<*>,
    val dialogFragmentBaseClass: KClass<out DialogFragment> = DialogFragment::class,

    val navigator: KClass<out Navigator> = EmptyNavigator::class,
    val navigationHandler: KClass<out NavigationHandler<*>> = EmptyNavigationHandler::class,

    val coroutinesEnabled: Boolean = false,
    val rxJavaEnabled: Boolean = false,
)
