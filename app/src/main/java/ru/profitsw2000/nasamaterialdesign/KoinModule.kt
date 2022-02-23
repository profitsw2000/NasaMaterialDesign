package ru.profitsw2000.nasamaterialdesign

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayViewModel

val appModule = module {
    //Repositories

    //View models
    viewModel { PictureOfTheDayViewModel(get()) }
}