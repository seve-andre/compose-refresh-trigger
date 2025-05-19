package com.mitch.refreshtrigger.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified VM : ViewModel> viewModelProviderFactory(
    crossinline create: () -> VM
): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        create()
    }
}
