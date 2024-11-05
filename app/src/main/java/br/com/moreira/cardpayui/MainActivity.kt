package br.com.moreira.cardpayui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editCreditCardNumber: EditText
    private lateinit var editDate: EditText
    private lateinit var editCVV: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular os EditTexts
        editCreditCardNumber = findViewById(R.id.editCreditCardNumber)
        editDate = findViewById(R.id.editMonthAndYear) // Nome do campo de data
        editCVV = findViewById(R.id.editCVV)

        // Configurar validadores
        setupCardNumberValidator()
        setupDateValidator()
        setupCVVValidator()
    }

    private fun setupCardNumberValidator() {
        editCreditCardNumber.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                s?.let {
                    val originalString = it.toString().replace(" ", "")

                    // Formatar com espaços a cada 4 dígitos
                    val formattedString = StringBuilder()
                    for (i in originalString.indices) {
                        if (i > 0 && i % 4 == 0) {
                            formattedString.append(' ')
                        }
                        formattedString.append(originalString[i])
                    }

                    // Atualizar o campo de texto
                    isFormatting = true
                    editCreditCardNumber.setText(formattedString.toString())
                    editCreditCardNumber.setSelection(formattedString.length) // Coloca o cursor no final
                    isFormatting = false

                    // Validar o número do cartão
                    if (originalString.length < 16) {
                        editCreditCardNumber.error = "Número de cartão inválido"
                    } else {
                        editCreditCardNumber.error = null
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupDateValidator() {
        editDate.addTextChangedListener(object : TextWatcher {
            private var isFormatting = false

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting) return

                s?.let {
                    val originalString = it.toString().replace("/", "")
                    val formattedString = StringBuilder()

                    // Adicionar uma barra após os dois primeiros dígitos
                    for (i in originalString.indices) {
                        if (i == 2) {
                            formattedString.append("/")
                        }
                        formattedString.append(originalString[i])
                    }

                    isFormatting = true
                    editDate.setText(formattedString.toString())
                    editDate.setSelection(formattedString.length) // Coloca o cursor no final
                    isFormatting = false

                    // Validar a data no formato MM/AA
                    if (!isValidMonthYear(formattedString.toString())) {
                        editDate.error = "Data inválida"
                    } else {
                        editDate.error = null
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun isValidMonthYear(date: String): Boolean {
        if (date.length != 5 || date[2] != '/') return false

        val month = date.substring(0, 2).toIntOrNull()
        val year = date.substring(3, 5).toIntOrNull()

        // Validar o mês entre 01 e 12 e o ano com 2 dígitos
        return month != null && month in 1..12 && year != null && year in 0..99
    }

    private fun setupCVVValidator() {
        editCVV.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Validar CVV de 3 dígitos
                s?.let {
                    if (it.length != 3) {
                        editCVV.error = "CVV inválido"
                    } else {
                        editCVV.error = null
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}