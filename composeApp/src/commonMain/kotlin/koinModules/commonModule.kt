package koinModules

import koinModules.`interface`.AvailableGamesRepository
import koinModules.`interface`.BaseViewModel
import koinModules.`interface`.TableBetsRepository
import org.koin.dsl.module
import repository.AvailableGamesRepositoryImpl
import repository.TableBetsRepositoryImpl

val commonModule = module {
//    single<Repository> { RepositoryImpl() }
//    factory<ViewModel> { ViewModel(get()) }
    factory<BaseViewModel> { createViewModel() }
    single<ApiClient> { ApiClient() }
    single<TableBetsRepository> { TableBetsRepositoryImpl(get()) } // Inject ApiClient into TableBetsRepositoryImpl
    single<AvailableGamesRepository> { AvailableGamesRepositoryImpl(get()) } // inject apiclient into games repo impl
}

expect fun createViewModel(): BaseViewModel
