package koinModules

import koinModules.`interface`.BaseViewModel

actual fun createViewModel(): BaseViewModel {
    return IOSViewModel()
}
