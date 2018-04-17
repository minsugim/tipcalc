package edu.washington.minsugim.tipcalc

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.text.TextWatcher
import android.text.Editable
import java.text.NumberFormat
import android.widget.Spinner
import android.widget.ArrayAdapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val charge = findViewById<EditText>(R.id.charge)
        val tip = findViewById<Button>(R.id.tip)
        tip.isEnabled = false

        fun chargeToDouble(amount: String): Double {
            val remove = Regex("[\$,.]")
            val cleanString:String = remove.replace(amount, "")
            return cleanString.toDouble()
        }

        // Currency input code inspired from https://stackoverflow.com/a/5233488
        charge.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val currCharge = ""
                if(!s.toString().equals(currCharge)){
                    charge.removeTextChangedListener(this)
                    val parsed = chargeToDouble(charge.text.toString())
                    val formatted:String = NumberFormat.getCurrencyInstance().format((parsed/100))
                    charge.setText(formatted)
                    charge.setSelection(formatted.length)

                    charge.addTextChangedListener(this)
                }
                tip.isEnabled = true
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter.createFromResource(this,
        R.array.tip_amounts, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.setAdapter(adapter)

        tip.setOnClickListener {
            var currCharge = chargeToDouble(charge.text.toString()) / 100
            val tipAmount = spinner.selectedItem.toString().toDouble()
            currCharge *= tipAmount / 100 + 1
            val tipToast: Toast = Toast.makeText(applicationContext, "$" + "%.2f".format(currCharge), Toast.LENGTH_LONG)
            tipToast.show()
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
