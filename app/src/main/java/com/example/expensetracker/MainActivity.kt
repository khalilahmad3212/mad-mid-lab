package com.example.expensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExpenseTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    App()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExpenseTrackerTheme {
        Greeting("Android")
    }
}

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getData(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }
}


@Composable
fun SplashScreen(navController: NavController) {

    LaunchedEffect(key1 = "Splash-Screen") {
        delay(3000);
        navController.navigate("main-screen");
    }


    Column {
        val name = "Expense Tracker"
        Text(text = name)
    }
}

@Composable
fun AddTransaction(navController: NavController) {
    var title by rememberSaveable {
        mutableListOf("")
    }
    val amount by rememberSaveable {
        mutableListOf(0)
    }
    val type by rememberSaveable {
        mutableListOf("")
    }

    val context = LocalContext.current

    Column {
        Surface {

            TextField(value = name, onValueChange = { title = it })
            TextField(value = amount, onValueChange = { amount = it })
            TextField(value = type, onValueChange = { type = it })
        }
        Button(onClick = {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE)

            val editor = sharedPreferences.edit()
            editor.putInt("title", title)
            editor.putInt("amount", amount)
            editor.putString("type", type)

            editor.apply()

            navController.navigate()
        }) {
            Text(text = "Add Transaction")
        }
    }
}

@Composable
fun AddIncome(navController: NavController) {

    val amount by rememberSaveable {
        mutableListOf(0)
    }
    val context = LocalContext.current

    Column {
        Surface {
            TextField(value = amount, onValueChange = { amount = it })
        }
        Button(onClick = {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE)

            val editor = sharedPreferences.edit()
            editor.putInt("totalIncome", amount)

            editor.apply()

            navController.navigate("main-screen")
        }) {
            Text(text = "Add Income")
        }
    }
}

@Composable
fun AddExpense(navController: NavController) {

    val amount by rememberSaveable {
        mutableListOf(0)
    }
    val context = LocalContext.current

    Column {
        Surface {
            TextField(value = amount, onValueChange = { amount = it })
        }
        Button(onClick = {
            val sharedPreferences: SharedPreferences =
                context.getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE)

            val editor = sharedPreferences.edit()
            editor.putInt("totalExpense", amount)

            editor.apply()

            navController.navigate("main-screen")
        }) {
            Text(text = "Add Income")
        }
    }
}

@Composable
fun StatsScreen(navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("TransactionPrefs", Context.MODE_PRIVATE)
    val totalIncome = sharedPreferences.getInt("totalIncome", 0)
    val totalExpense = sharedPreferences.getInt("totalExpense", 0)
    val pastTransactions = sharedPreferences.getString("pastTransactions", 0)
    /*
    * Past transaction should be fetched from sharedpreferences.
    * Don't have the code to save/retrieve list form shared preferences
    * */
    val pastTrasactions = arrayListOf({ description: "Hello", amount: 400, date: new Date() })

    Column {
        Text(text = "Income")
        Text(text = "RS: $totaIncome")

        Text(text = "Expenses")
        Text(text = "-RS: $totaExpense")
        Text(text = "Last Transaction")

        Surface {
            Row {
                Text(text = "Description")
                Text(text = "Amount")
                Text(text = "Date")

            }
            pastTrasactions.forEach(transaction -> {
            Row {
                Text(text = "${transaction.title}")
                Text(text = "${transaction.amount}")
                Text(text = "${transaction.date}")

            }
        })
        }
    }
}

@Composable
fun MainScreen(navController: NavController) {
    // get data from shared pereferences
    Column {
        Text(text = "")
    }
    Button(onClick = {
        navController.navigate("add-income")
    }) {
        Text(text = "Add Income")
    }
    Button(onClick = {
        navController.navigate("add-expense")
    }) {
        Text(text = "Add Expense")
    }
}
}


@Composable
fun App() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "SplashScreen") {

        composable(route = "SplashScreen") { SplashScreen(navController) }
        composable(route = "main-screen") { MainScreen(navController) }
        composable(route = "add-transaction") { AddTransaction(navController) }
        composable(route = "add-income") { AddTransaction(navController) }
        composable(route = "add-expense") { AddTransaction(navController) }
    }
}