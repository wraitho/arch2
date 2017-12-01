package com.wraitho.arch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.wraitho.users.ui.UsersFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                .beginTransaction()
                .add(R.id.usersFragment, UsersFragment.newInstance())
                .commit()

    }
}
