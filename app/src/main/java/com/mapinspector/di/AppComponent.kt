package com.mapinspector.di

import com.mapinspector.di.network.NetworkModule
import com.mapinspector.di.network.ViewModelModule
import com.mapinspector.ui.map.fragments.BottomDialogFragment
import com.mapinspector.ui.map.fragments.MapFragment
import com.mapinspector.ui.map.fragments.MapListFragment
import com.mapinspector.ui.map.viewmodels.BottomDialogViewModel
import dagger.Component
import javax.inject.Singleton


@Component(modules = [NetworkModule::class, ViewModelModule::class])
@Singleton interface AppComponent {
    fun inject(fragment: MapFragment)
    fun inject(fragment: MapListFragment)
    fun inject(fragment: BottomDialogFragment)
    fun inject(viewModel: BottomDialogViewModel)
}