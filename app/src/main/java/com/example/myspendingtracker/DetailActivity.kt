package com.example.myspendingtracker

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
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

class DetailActivity : AppCompatActivity() {
    private lateinit var transaction: Transaction

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val labelLayout: TextInputLayout = findViewById(R.id.labelLayout)
        val labelInput: TextInputEditText = findViewById(R.id.labelInput)
        val amountInput: TextInputEditText = findViewById(R.id.amountInput)
        val descriptionInput: TextInputEditText = findViewById(R.id.descriptionInput)
        val updateBtn: Button = findViewById(R.id.updateBtn)
        val rootView: View = findViewById(R.id.rootView)


        rootView.setOnClickListener {
            this.window.decorView.clearFocus()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }

        transaction = intent.getSerializableExtra("transaction") as Transaction
        labelInput.setText(transaction.label)
        amountInput.setText(transaction.amount.toString())
        descriptionInput.setText(transaction.description)

        labelInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
            if (it!!.count() > 0) {
                labelLayout.error = null
            }
        }

        amountInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
            if (it!!.count() > 0) {
                labelLayout.error = null
            }

        }

        descriptionInput.addTextChangedListener {
            updateBtn.visibility = View.VISIBLE
        }

        enableEdgeToEdge()


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rootView)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun updateTransaction(view: View) {
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
            val transaction = Transaction(transaction.id, label, amount, description)
            update(transaction)
        }

    }

    fun transferToMainActivity(view: View) {
        finish()
    }

    private fun update(transaction: Transaction) {
        val db = Room.databaseBuilder(
            this,
            AppDatabase::class.java, "transactions"
        )
            .build()

        GlobalScope.launch {
            db.transactionDao().update(transaction)
            finish()
        }
    }

}