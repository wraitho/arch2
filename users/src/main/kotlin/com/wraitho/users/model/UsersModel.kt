package com.wraitho.users.model

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.wraitho.commons.data.User
import com.wraitho.network.RetrofitException
import com.wraitho.users.model.communication.*
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class UsersModel(private val userApi: Model.UsersApi, private val nameFilter: Model.NameFilter) : ViewModel() {

    val uiEvents: PublishSubject<UiEvent> = PublishSubject.create<UiEvent>()
    val uiModel = MutableLiveData<Result<UsersResult>>()
    val navigationEvents = SingleLiveData<NavigationUpdate>()
    var lastLoadedId = -1L;

    init {
        val uiEventTransformer: ObservableTransformer<UiEvent, Observable<UsersResult>> = ObservableTransformer { event ->
            event.map { getResults(it) }
        }

        val uiModelTransformer: ObservableTransformer<Observable<UsersResult>, Result<UsersResult>> = ObservableTransformer { users ->
            users.flatMap { t -> t }
                    .map { Result.success(it) }
                    .onErrorReturn({ throwable -> Result.error(getErrorType(throwable)) })
                    .startWith(Result.loading())
        }

        uiEvents
                .compose(uiEventTransformer)
                .compose(uiModelTransformer)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { users -> uiModel.value = users }

    }

    private fun getResults(uiEvent: UiEvent): Observable<UsersResult> {
        return when (uiEvent) {
            is LoadMoreUsersEvent -> {
                if (lastLoadedId == -1L) {
                    userApi.getUsers(uiEvent.lastLoadedId).map {
                        lastLoadedId = it.last().id
                        UsersResult(UsersResult.Action.INITIAL_LOAD, it)
                    }
                } else {
                    if (uiEvent.lastLoadedId == 0L) {
                        Observable.just(UsersResult(UsersResult.Action.DO_NOTHING, uiModel.value?.data!!.users))
                    } else {
                        userApi.getUsers(uiEvent.lastLoadedId).map { UsersResult(UsersResult.Action.APPEND, it) }
                    }
                }
            }
            is NameSearchEvent -> nameFilter.filter(uiModel.value?.data!!.users, uiEvent.name).map { t -> UsersResult(UsersResult.Action.RELOAD, t) }
            is UserSelectedEvent -> {
                navigationEvents.postValue(GoToUserScreen(uiEvent.userId))
                return Observable.just(UsersResult(UsersResult.Action.DO_NOTHING, uiModel.value?.data!!.users))
            }
            is Back -> {
                navigationEvents.postValue(GoBack())
                return Observable.just(UsersResult(UsersResult.Action.DO_NOTHING, uiModel.value?.data!!.users))
            }
        }
    }

    private fun getErrorType(throwable: Throwable): Error {

        return if (throwable is RetrofitException) {

            when (throwable.kind) {
                RetrofitException.Kind.NETWORK -> NetworkError(throwable)
                RetrofitException.Kind.HTTP -> ServerError(throwable)
                RetrofitException.Kind.UNEXPECTED -> UnknownError(throwable)
                else -> UnknownError(throwable)
            }
        } else {
            UnknownError(throwable)
        }
    }

    class Factory @Inject constructor(private val usersApi: Model.UsersApi, private val nameFilter: Model.NameFilter) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return UsersModel(usersApi, nameFilter) as T
        }
    }

}
