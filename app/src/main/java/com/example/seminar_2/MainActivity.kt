package com.example.seminar_2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seminar_2.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            createSpeedometerValues()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    binding.speedometer.text = it.toString()
                }
        }
    }

    fun createSpeedometerValues() = Observable.create<Int> {
        var isAccelerate = true
        var speed = 0
        while (true) {
            Thread.sleep(100)
            if (isAccelerate) {
                speed++
                if (speed == 100) isAccelerate = false
            } else {
                speed--
                if (speed == 0) isAccelerate = true
            }
            it.onNext(speed)
        }
    }.subscribeOn(Schedulers.io())
}