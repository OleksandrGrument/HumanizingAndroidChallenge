package com.grument.humanizing.view.main

import android.os.Bundle
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.design.activity.RobotActivity
import com.grument.humanizing.R
import com.grument.humanizing.view.VMFactory
import com.grument.humanizing.view.main.adapter.ChatAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : RobotActivity() {

    private val viewModel by lazy { VMFactory(application).create(MainViewModel::class.java) }

    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initialize()
    }

    private fun initialize() {
        initViews()
        initObservers()
        QiSDK.register(this, viewModel)
    }

    private fun initViews() {

        adapter = ChatAdapter()
        chatRv.adapter = adapter

        animationBt.setOnClickListener {
            viewModel.showAnimation()
        }
        jokeBt.setOnClickListener {
            viewModel.tellAnyJoke()
        }
    }

    private fun initObservers() {

        viewModel.observeShowAnimation().observe(this, {
            animationBt.isEnabled = !it
        })
        viewModel.observeChatEvent().observe(this, {
            adapter.handleEvent(it)
        })
        viewModel.observeNewJoke().observe(this, {
            jokeEt.setText(it)
        })
    }

    override fun onDestroy() {
        // Unregister the RobotLifecycleCallbacks for this Activity.
        QiSDK.unregister(this, viewModel)
        super.onDestroy()
    }
}




