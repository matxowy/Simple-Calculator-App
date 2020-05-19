package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ArithmeticException

class MainActivity : AppCompatActivity() {

    var lastNumeric = false //zmienna pozwalajaca określić czy ostatnia jest liczba
    var lastDot = false //zmienna pozwalajaca określić czy ostatnia jest kropka

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }


    fun onDigit(view: View) {
        tvInput.append((view as Button).text) //append dodaje do tvInput następne liczby
        lastNumeric = true
    }

    fun onClear(view: View) {
        tvInput.text = "" //zastępuje wszystko co jest w tvInput pustym stringiem czyli czyści
        lastNumeric = false
        lastDot = false
    }

    fun onDecimalPoint(view: View) {
        if (lastNumeric && !lastDot) { //dodaje kropkę wtedy gdy ostatnia jest liczba i ostatnia nie jest kropka
            tvInput.append(".")
            lastNumeric = false
            lastDot = true
        }
    }

    fun onOperator(view: View) {
        if (lastNumeric && !isOperatorAdded(tvInput.text.toString())) { //sprawdza czy ostatnio dadana jest liczba i czy nie został już dodany operator inny niż - przed całą liczbą
            tvInput.append((view as Button).text)
            lastNumeric = false
            lastDot = false
        }
    }

    private fun isOperatorAdded(value: String): Boolean { //funkcja pomocnicza pomagająca określić czy nie został dodany już operator i czy nie jest - oznaczajacym liczbę ujemną
        return if (value.startsWith("-")) {
            false
        } else {
            value.contains("/") || value.contains("*") || value.contains("+") || value.contains("-")

        }
    }


    fun onEqual(view: View) {
        if (lastNumeric) {
            var tvValue =
                tvInput.text.toString() //zmienna zapisująca to co jest w tvInput żeby można było coś z tym zrobić
            var prefix = "" //zmienna potrzebna przy znaku - w obliczeniach żeby były poprawne

            try {

                if (tvValue.startsWith("-")) { //jeżeli pobrana dana zaczyna się od - trzeba w prefixie dopisać - i zacząć czytać daną od indeksu 1(substring na to pozwala)
                    prefix = "-"
                    tvValue = tvValue.substring(1)
                }

                if (tvValue.contains("-")) { //jeżeli zawiera dana - to dzielimy wyrażenie na dwie zmienne w których jest liczba z lewej strony minusa i z prawej
                    var splitValue = tvValue.split("-")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()) { //jeżeli do prefixu jest zapisany - czyli wykonał się if to trzeba dodać do naszej zmiennej one - żeby wykonywały się dobrze obliczenia z minusem
                        one = prefix + one
                    }

                    tvInput.text = removeZeroAfterDot((one.toDouble() - two.toDouble()).toString()) //obliczanie wyrażenia i wyświetlanie go w tvInput

                } else if (tvValue.contains("+")) {
                    var splitValue = tvValue.split("+")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()) {
                        one = prefix + one
                    }

                    tvInput.text = removeZeroAfterDot((one.toDouble() + two.toDouble()).toString())

                } else if (tvValue.contains("/")) {
                    var splitValue = tvValue.split("/")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()) {
                        one = prefix + one
                    }

                    if (two.equals("0")) { //sprawdzenie czy dzielenie odbywa się przez zero
                        tvInput.text = "Niedozwolone"
                    }else tvInput.text = removeZeroAfterDot((one.toDouble() / two.toDouble()).toString())

                } else if (tvValue.contains("*")) {
                    var splitValue = tvValue.split("*")

                    var one = splitValue[0]
                    var two = splitValue[1]

                    if (!prefix.isEmpty()) {
                        one = prefix + one
                    }

                    tvInput.text = removeZeroAfterDot((one.toDouble() * two.toDouble()).toString())
                }


            } catch (e: ArithmeticException) {
                e.printStackTrace()
            }

        }
    }

    private fun removeZeroAfterDot(result: String) : String { //usuwanie zbędnego rozwinięcia dziesiętnego .0
        var value = result
        if (result.contains(".0")) value = result.substring(0, result.length - 2) //jeżeli wynik zawiera .0 wtedy substringiem zaczynamy od pozycji 0, ale kończymy dwa miejsca przed co pozwala nam uciąć .0

        return value
    }

}
