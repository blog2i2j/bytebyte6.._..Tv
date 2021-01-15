package com.bytebyte6.view

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bytebyte6.base.mvi.Result
import com.bytebyte6.base.mvi.isSuccess
import com.bytebyte6.base_ui.BaseViewModel
import com.bytebyte6.data.entity.Tv
import com.bytebyte6.data.entity.User
import com.bytebyte6.base.onIo
import com.bytebyte6.usecase.CreateUserUseCase
import com.bytebyte6.usecase.InitDataUseCase

class LauncherViewModel(
    private val initDataUseCase: InitDataUseCase,
    private val createUserUseCase: CreateUserUseCase
) : BaseViewModel() {

    private val eventObserver = Observer<Result<User>> {
        it.isSuccess()?.apply {
            AppCompatDelegate.setDefaultNightMode(
                if (nightMode)
                    AppCompatDelegate.MODE_NIGHT_YES
                else
                    AppCompatDelegate.MODE_NIGHT_NO
            )
        }
        removeObserver()
    }

    private fun removeObserver() {
        createUserUseCase.result().removeObserver(eventObserver)
    }

    init {
        //1、没有用户就创建用户
        addDisposable(
            createUserUseCase.execute(User(name = "Admin")).onIo()
        )
        createUserUseCase.result().observeForever(eventObserver)
    }

    fun init(): LiveData<Result<List<Tv>>> {
        addDisposable(
            initDataUseCase.execute("init").onIo()
        )
        return initDataUseCase.result()
    }
}