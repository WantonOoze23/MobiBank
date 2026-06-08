package com.example.mobibank.feature.transactions

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mobibank.feature.accounts.AccountsListState
import com.example.mobibank.feature.accounts.AccountsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    accountId: String,
    accountCurrency: String,
    onNavigateBack: () -> Unit,
    viewModel: TransactionsViewModel = koinViewModel(),
    accountsViewModel: AccountsViewModel = koinViewModel()
) {
    val context = LocalContext.current

    var cardNumber by remember { mutableStateOf("") }
    var amountText by remember { mutableStateOf("") }

    val accountsState by accountsViewModel.listState.collectAsStateWithLifecycle()
    val selectedAccount = (accountsState as? AccountsListState.Success)?.accounts?.find { it.id == accountId }

    val currencySymbol = when (selectedAccount?.currency?.name) {
        "USD" -> "$"
        "EUR" -> "€"
        else -> "₴"
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Переказ на картку") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { Icon(Icons.Default.ArrowBack, "Назад") }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Сума переказу",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = amountText,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                        amountText = it
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                ),
                prefix = {
                    Text(currencySymbol, fontSize = 36.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
            )

            Text(
                text = "Комісія: 0 ₴",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = cardNumber,
                onValueChange = {
                    if (it.length <= 16 && it.all { char -> char.isDigit() }) {
                        cardNumber = it
                    }
                },
                label = { Text("Картка отримувача") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                visualTransformation = CardNumberVisualTransformation(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val amount = amountText.toDoubleOrNull()
                    if (amount != null && amount > 0 && cardNumber.length == 16) {
                        viewModel.transfer(accountId, cardNumber, amount)
                        Toast.makeText(context, "Відправка...", Toast.LENGTH_SHORT).show()
                    }
                    onNavigateBack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = cardNumber.length == 16 && amountText.isNotBlank() && (amountText.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text("Надіслати", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

class CardNumberVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
        var out = ""
        for (i in trimmed.indices) {
            out += trimmed[i]
            if (i % 4 == 3 && i != 15) out += " "
        }

        val creditCardOffsetTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 3) return offset
                if (offset <= 7) return offset + 1
                if (offset <= 11) return offset + 2
                if (offset <= 16) return offset + 3
                return 19
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 4) return offset
                if (offset <= 9) return offset - 1
                if (offset <= 14) return offset - 2
                if (offset <= 19) return offset - 3
                return 16
            }
        }

        return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
    }
}