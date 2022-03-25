package ru.profitsw2000.nasamaterialdesign

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayViewModel
import ru.profitsw2000.nasamaterialdesign.retrofit.PODRetrofitImpl
import ru.profitsw2000.nasamaterialdesign.retrofit.PictureOfTheDayAPI

val appModule = module {
    //Repositories
    //single<PictureOfTheDayAPI> { PODRetrofitImpl() }

    //View models
    //viewModel { PictureOfTheDayViewModel(get()) }
}