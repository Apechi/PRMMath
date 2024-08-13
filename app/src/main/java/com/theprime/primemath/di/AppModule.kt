package com.theprime.primemath.di

import androidx.room.Room
import com.theprime.primemath.data.AppDatabase
import com.theprime.primemath.ui.fragment.addSubList.AddSubListViewModel
import com.theprime.primemath.ui.fragment.game.battle.BattleFragmentViewModel
import com.theprime.primemath.ui.fragment.choosePlayer.ChoosePlayerViewModel
import com.theprime.primemath.ui.fragment.divisibilityList.DivisibilityListViewModel
import com.theprime.primemath.ui.fragment.game.divisibility.DivisibilityGameViewModel
import com.theprime.primemath.ui.fragment.game.normal.NormalGameViewModel
import com.theprime.primemath.ui.fragment.game.table.TableGameViewModel
import com.theprime.primemath.ui.fragment.game.units.UnitsGameViewModel
import com.theprime.primemath.ui.fragment.mulDivList.MulDivListViewModel
import com.theprime.primemath.ui.fragment.unitsList.UnitsListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            AppDatabase.DATABASE_FILE
        ).build()
    }

    viewModel { AddSubListViewModel(get()) }
    viewModel { MulDivListViewModel(get()) }
    viewModel { DivisibilityListViewModel(get()) }
    viewModel { UnitsListViewModel(get()) }
    viewModel { NormalGameViewModel() }
    viewModel { DivisibilityGameViewModel() }
    viewModel { TableGameViewModel() }
    viewModel { UnitsGameViewModel() }
    viewModel { BattleFragmentViewModel(get()) }
    viewModel {
        ChoosePlayerViewModel(
            get()
        )
    }
}