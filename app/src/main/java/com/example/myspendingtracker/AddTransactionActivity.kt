package com.example.myspendingtracker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.room.Room
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AddTransactionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        val rootView: View = findViewById(R.id.main)
        rootView.setOnClickListener {
            this.window.decorView.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }


        val labelLayout: TextInputLayout = findViewById(R.id.labelLayout)
        val labelInput: TextInputEditText = findViewById(R.id.labelInput)
        val amountInput: TextInputEditText = findViewById(R.id.amountInput)


        labelInput.addTextChangedListener {
            if (it!!.count() > 0) {
                labelLayout.error = null
            }
        }

        amountInput.addTextChangedListener {
            if (it!!.count() > 0) {
                labelLayout.error = null
            }
        }
        enableEdgeToEdge()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun addNewTransaction(view: View) {
        val labelInput: TextInputEditText = findViewById(R.id.labelInput)
        val descriptionInput: TextInputEditText = findViewById(R.id.descriptionInput)
        val amountInput: TextInputEditText = findViewById(R.id.amountInput)
        val labelLayout: TextInputLayout = findViewById(R.id.labelLayout)
        val amountLayout: TextInputLayout = findViewById(R.id.amountLayout)

        val label = labelInput.text.toString()
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = descriptionInput.text.toString()

        if (label.isEmpty()) {
            labelLayout.error = "Please enter a valid label"
        }

        if (amount == null) {
            amountLayout.error = "Please enter a valid amount"
        } else {
            val transaction = Transaction(0, label, amount, description)
            insert(transaction)
        }

    }

    fun transferToMainActivity(view: View) {
        finish()
    }

    private fun insert(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "transactions"
        )
            .build()

        GlobalScope.launch {
            db.transactionDao().insertAll(transaction)
            finish()
        }
    }

}