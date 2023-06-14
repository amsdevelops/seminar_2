package com.example.seminar_2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.seminar_2.databinding.ActivityMainBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.random.Random
import kotlin.random.nextInt
import kotlin.random.nextLong

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var isAccelerate = true
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            Observable.combineLatest(createSpeedometerValues(), createSpeedometerValues2()) { speed1, speed2 ->
                (speed1 + speed2) / 2
            }
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    if (binding.metricsswitch.isChecked) {
                        (it / 1.6).toInt()
                    } else {
                        it
                    }
                }
                .filter {
                    if (binding.by5switch.isChecked) it % 5 == 0 else true
                }
                .map {
                    if (binding.metricsswitch.isChecked) {
                        it.toString() + getString(R.string.mph)
                    } else {
                        it.toString() + getString(R.string.kmh)
                    }
                }
                .subscribe {
                    binding.speedometer.text = it.toString()
                }
        }
    }

    fun createSpeedometerValues() = Observable.create<Int> {
        var speed = 0
        while (true) {
            Thread.sleep(Random.nextLong(500))
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

    fun createSpeedometerValues2() = Observable.create<Int> {
        var speed = 0
        while (true) {
            Thread.sleep(Random.nextLong(500))
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