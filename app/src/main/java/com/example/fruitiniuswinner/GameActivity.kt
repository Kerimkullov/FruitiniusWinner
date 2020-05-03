package com.example.fruitiniuswinner

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_game.*
import  com.example.fruitiniuswinner.R.drawable.*

class GameActivity : AppCompatActivity() {
    private val images: MutableList<Int> =
        mutableListOf(
            apelsin,
            arbuz,
            kivi,
            limon,
            malina,
            seven,
            apelsin,
            arbuz,
            kivi,
            limon,
            malina,
            seven
        )
    private var gameIndex = 1
    private var sumClicks = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        next.setOnClickListener {
            setGame()
            gameIndex++
            textView.text = "Level ${gameIndex}"
            sumClicks = 0
            sumClicksText.text = "Move(s) '${sumClicks}'"
            secundomer.base = SystemClock.elapsedRealtime();
        }
        refresh.setOnClickListener {
            setGame()
            textView.text = "Level ${gameIndex}"
            sumClicks = 0
            sumClicksText.text = "Move(s) '${sumClicks}'"
            secundomer.base = SystemClock.elapsedRealtime();
            secundomer.start()
        }
    }
    private fun setGame(){

        val buttons = arrayOf(
            button1, button2, button3, button4, button5, button6, button7, button8,
            button9, button10, button11, button12
        )

        val cardBack = icon
        var clicked = 0
        var turnOver = false
        var lastClicked = -1
        var score = 0


        images.shuffle()
        for (i in 0..11) {
            buttons[i].setBackgroundResource(cardBack)
            buttons[i].text = "cardBack"
            buttons[i].textSize = 0.0F
            buttons[i].setOnClickListener {
                if (buttons[i].isClickable) {
                    sumClicks++
                    sumClicksText.text = "Move(s) '${sumClicks}'"
                }
                if (buttons[i].text == "cardBack" && !turnOver) {
                    buttons[i].setBackgroundResource(images[i])
                    buttons[i].setText(images[i])
                    if (clicked == 0) {
                        lastClicked = i
                    }
                    clicked++
                }
                if (clicked == 2) {
                    turnOver = true
                    if (buttons[i].text == buttons[lastClicked].text) {
                        buttons[i].isClickable = false
                        buttons[lastClicked].isClickable = false
                        turnOver = false
                        clicked = 0
                        score ++
                    } else if (buttons[i].text != buttons[lastClicked].text) {
                        val timer = object : CountDownTimer(500, 500) {
                            override fun onTick(millisUntilFinished: Long) {}

                            override fun onFinish() {
                                buttons[i].setBackgroundResource(cardBack)
                                buttons[lastClicked].setBackgroundResource(cardBack)
                                buttons[i].text = "cardBack"
                                buttons[lastClicked].text = "cardBack"
                                turnOver = false
                            }
                        }
                        timer.start()
                        clicked = 0
                    }
                } else if (clicked == 0) {
                    turnOver = false
                }
                if(score == 6){
                    secundomer.stop()
                }
            }
        }
    }
}