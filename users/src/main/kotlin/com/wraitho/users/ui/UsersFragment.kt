package com.wraitho.users.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.squareup.picasso.Picasso
import com.wraitho.commons.data.User
import com.wraitho.commons.hideMySs
import com.wraitho.commons.letMeVisible
import com.wraitho.network.NetworkModule
import com.wraitho.users.R
import com.wraitho.users.di.DaggerUsersComponent
import com.wraitho.users.di.UsersComponent
import com.wraitho.users.di.UsersModule
import com.wraitho.users.model.UsersModel
import com.wraitho.users.model.communication.*
import io.reactivex.Observable
import kotlinx.android.synthetic.main.content_users.*
import kotlinx.android.synthetic.main.error.*
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.loading.*
import org.jetbrains.anko.support.v4.toast
import javax.inject.Inject

class UsersFragment : Fragment() {
    @Inject lateinit var usersViewModelFactory: UsersModel.Factory

    private lateinit var usersComponent: UsersComponent
    private lateinit var usersListAdapter: UsersListAdapter
    private lateinit var usersViewModel: UsersModel

    companion object {
        @JvmStatic fun newInstance() = UsersFragment()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        inject()
        setupViewModel()
        return inflater?.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        // clicks from FAB updates the subject

        Observable.merge(
                usersSearch.clicks().map { NameSearchEvent("wraitho") },
                errorAction.clicks().map { Back() }
        ).subscribe(usersViewModel.uiEvents)

        // this could be provided by DI, Picasso as well but now for simplification...
        usersListAdapter = UsersListAdapter(usersViewModel.uiEvents, Picasso.with(context))
        usersList.adapter = usersListAdapter
        usersList.layoutManager = LinearLayoutManager(context)
        usersList.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        // ...
        // usersPullToRefresh.setOnRefreshListener { events.onNext() }

        startInitialQuery()
    }

    private fun setupViewModel() {
        usersViewModel = ViewModelProviders.of(this, usersViewModelFactory).get(UsersModel::class.java)

        usersViewModel.uiModel.observe(this, Observer {
            when (it?.status) {
                is ResultLoading -> showLoading()
                is ResultError -> showError(it.error!!)
                is ResultSuccess -> showResult(it.data!!)
            }
        })

        usersViewModel.navigationEvents.observe(this, Observer {
            when (it) {
                is GoBack -> activity.onBackPressed()
                is GoToUserScreen -> toast("Coming Soon")
            }
        })
    }

    private fun startInitialQuery() {
        usersViewModel.uiEvents.onNext(LoadMoreUsersEvent(0))
    }

    private fun showResult(usersResult: UsersResult) {
        loading.hideMySs()
        errorView.hideMySs()
        usersList.letMeVisible()

        when (usersResult.action) {
            UsersResult.Action.INITIAL_LOAD, UsersResult.Action.APPEND -> {
                usersListAdapter.users += usersResult.users
                usersListAdapter.onDataReady(true)
            }
            UsersResult.Action.DO_NOTHING -> {}
            UsersResult.Action.RELOAD -> {

            }
        }
    }

    private fun showError(error: Error) {
        loading.hideMySs()
        usersList.hideMySs()
        errorView.letMeVisible()
        val errorMessage = "${error::class.java.simpleName} \n ${error.throwable.message}"
        errorDescription.text = errorMessage
    }

    private fun showLoading() {
        usersList.hideMySs()
        errorView.hideMySs()
        loading.letMeVisible()
    }

    private fun inject() {
        usersComponent = DaggerUsersComponent
                .builder()
                .networkModule(NetworkModule())
                .usersModule(UsersModule())
                .build()
        usersComponent.inject(this)
    }
}