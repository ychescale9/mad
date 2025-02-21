Change Log
==========

Version 0.3.0-alpha05 *(2021-08-26)*
----------------------------

- Whetstone: add `ComposeDialogFragment` annotation
- Whetstone: call `setViewCompositionStrategy` from generated compose fragments

Version 0.3.0-alpha04 *(2021-08-19)*
----------------------------

- Whetstone: add `RendererDialogFragment` annotation
- Whetstone: support providing compose `ProvidedValue` into a set and automatically adding those to a `CompositionLocalProvider`

Version 0.3.0-alpha03 *(2021-08-16)*
----------------------------

- Whetstone: merge `RetainedComponent` annotation into the others
- Whetstone: support inset handling in generated `@ComposeFragment` classes
- Whetstone: generatec composables don't need `OnBackPressedDispatcher` in their ctor anymore
- Navigator: remove `CoroutineScope` parameter from `FragmentNavigationHandler`

Version 0.3.0-alpha02 *(2021-08-08)*
----------------------------

- Whetstone: added support to generate components tied to a `NavBackStackEntry` through `@NavEntryComponent`
- `LoadingTextResource.format` now returns `Nothing`

Version 0.3.0-alpha01 *(2021-08-05)*
----------------------------

- initial experimental release of the `whetstone-runtime` and `whetstone-compiler` artifacts
- initial experimental release of the `navigator` artifact

Version 0.2.0 *(2021-06-18)*
----------------------------

- update `StateMachine.state` to return `StateFlow<State>` instead of `Flow<State>`


Version 0.1.1 *(2021-06-16)*
----------------------------

- fix windows artifact of `state-machine` not being published


Version 0.1.0 *(2021-06-16)*
----------------------------

- initial release of the `state-machine` artifact
- initial release of the `text-resource` artifact
