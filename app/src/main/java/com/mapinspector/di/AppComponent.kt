package com.mapinspector.di

import com.mapinspector.utils.SharedPreferences
import com.mapinspector.di.module.ContextModule
import com.mapinspector.di.module.NetworkModule
import com.mapinspector.di.module.SharedPreferencesModule
import com.mapinspector.di.module.ViewModelModule
import com.mapinspector.ui.map.fragments.BottomDialogFragment
import com.mapinspector.ui.map.fragments.MapFragment
import com.mapinspector.ui.map.fragments.MapListFragment
import com.mapinspector.viewmodel.BottomDialogViewModel
import com.mapinspector.viewmodel.MapListViewModel
import dagger.Component
import javax.inject.Singleton

@Component(modules = [NetworkModule::class, ViewModelModule::class, SharedPreferencesModule::class, ContextModule::class])
@Singleton interface AppComponent {

    fun inject(fragment: MapFragment)
    fun inject(fragment: MapListFragment)
    fun inject(fragment: BottomDialogFragment)

    fun inject(viewModel: BottomDialogViewModel)
    fun inject(viewModel: MapListViewModel)

    fun inject(sharedPref: SharedPreferences)
}