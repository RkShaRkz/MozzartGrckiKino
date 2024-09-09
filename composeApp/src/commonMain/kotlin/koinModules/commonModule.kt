package koinModules

import koinModules.`interface`.BaseViewModel
import org.koin.dsl.module

val commonModule = module {
//    single<Repository> { RepositoryImpl() }
//    factory<ViewModel> { ViewModel(get()) }
    factory<BaseViewModel> { createViewModel() }
}

expect fun createViewModel(): BaseViewModel
