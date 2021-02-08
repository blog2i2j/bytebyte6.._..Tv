package com.bytebyte6.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

interface RxUseCase2<I, O> {

    val result: MutableLiveData<Result<O>>

    fun result(): LiveData<Result<O>> = result

    fun execute(param: I): Single<O> = Single.create<O> {
        try {
            result.postValue((Result.Loading()))
            val o = run(param)
            result.postValue((Result.Success(o)))
            it.onSuccess(o)
        } catch (e: Exception) {
            result.postValue((Result.Error(e)))
            it.onError(e)
        }
    }

    fun run(param: I): O
}

abstract class RxUseCase<I, O> {

    private val result: MutableLiveData<Result<O>> = MutableLiveData()

    fun result(): LiveData<Result<O>> = result

    fun execute(param: I): Single<O> = Single.create<O> {
        try {
            result.postValue((Result.Loading()))
            val o = run(param)
            result.postValue((Result.Success(o)))
            it.onSuccess(o)
        } catch (e: Exception) {
            result.postValue((Result.Error(e)))
            it.onError(e)
        }
    }

    fun interval(param: I, period: Long = 2): Observable<Long> =
        Observable.interval(period, TimeUnit.SECONDS)
            .doOnNext {
                val resultType = run(param)
                result.postValue((Result.Success(resultType)))
            }.doOnError {
                result.postValue((Result.Error(it)))
            }

    abstract fun run(param: I): O
}

fun <T> Single<T>.onSingle(): Disposable {
    return subscribeOn(Schedulers.single())
        .subscribe({}, {
            it.printStackTrace()
        })
}

fun <T> Single<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io())
        .subscribe({}, {
            it.printStackTrace()
        })
}

fun <T> Single<T>.onComputation(): Disposable {
    return subscribeOn(Schedulers.computation())
        .subscribe({}, {
            it.printStackTrace()
        })
}

fun <T> Observable<T>.onIo(): Disposable {
    return subscribeOn(Schedulers.io())
        .subscribe({}, {
            it.printStackTrace()
        })
}