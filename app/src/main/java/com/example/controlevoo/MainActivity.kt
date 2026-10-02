package com.example.controlevoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import com.example.controlevoo.ui.theme.ControleVooTheme
import java.util.Locale

data class Viagem(
    val data: String,
    val kmInicial: Double,
    val kmFinal: Double,
    val litros: Double,
    val combustivel: Double,
    val pedagio: Double
) {
    val distancia: Double
        get() = kmFinal - kmInicial

    val custoTotal: Double
        get() = combustivel + pedagio
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ControleVooTheme {
                AppControleViagens()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppControleViagens() {

    var data by remember { mutableStateOf("") }
    var kmInicial by remember { mutableStateOf("") }
    var kmFinal by remember { mutableStateOf("") }
    var litros by remember { mutableStateOf("") }
    var combustivel by remember { mutableStateOf("") }
    var pedagio by remember { mutableStateOf("") }

    val viagens = remember { mutableStateListOf<Viagem>() }

    val totalKm = viagens.sumOf { it.distancia }
    val totalGasto = viagens.sumOf { it.custoTotal }
    val totalLitros = viagens.sumOf { it.litros }

    val mediaKmLitro =
        if (totalLitros > 0) totalKm / totalLitros else 0.0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Controle de Viagens")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {
                Text(
                    text = "Cadastrar viagem",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                CampoTexto(
                    valor = data,
                    aoAlterar = { data = it },
                    label = "Data",
                    teclado = KeyboardType.Text
                )
            }

            item {
                CampoTexto(
                    valor = kmInicial,
                    aoAlterar = { kmInicial = it },
                    label = "KM inicial",
                    teclado = KeyboardType.Decimal
                )
            }

            item {
                CampoTexto(
                    valor = kmFinal,
                    aoAlterar = { kmFinal = it },
                    label = "KM final",
                    teclado = KeyboardType.Decimal
                )
            }

            item {
                CampoTexto(
                    valor = litros,
                    aoAlterar = { litros = it },
                    label = "Litros abastecidos",
                    teclado = KeyboardType.Decimal
                )
            }

            item {
                CampoTexto(
                    valor = combustivel,
                    aoAlterar = { combustivel = it },
                    label = "Valor do combustível (R$)",
                    teclado = KeyboardType.Decimal
                )
            }

            item {
                CampoTexto(
                    valor = pedagio,
                    aoAlterar = { pedagio = it },
                    label = "Pedágio (R$)",
                    teclado = KeyboardType.Decimal
                )
            }

            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {

                        val inicial = kmInicial
                            .replace(",", ".")
                            .toDoubleOrNull()

                        val final = kmFinal
                            .replace(",", ".")
                            .toDoubleOrNull()

                        val litrosValor = litros
                            .replace(",", ".")
                            .toDoubleOrNull()

                        val combustivelValor = combustivel
                            .replace(",", ".")
                            .toDoubleOrNull()

                        val pedagioValor = pedagio
                            .replace(",", ".")
                            .toDoubleOrNull()

                        if (
                            data.isNotBlank() &&
                            inicial != null &&
                            final != null &&
                            litrosValor != null &&
                            combustivelValor != null &&
                            pedagioValor != null &&
                            final >= inicial &&
                            litrosValor > 0
                        ) {

                            viagens.add(
                                Viagem(
                                    data = data,
                                    kmInicial = inicial,
                                    kmFinal = final,
                                    litros = litrosValor,
                                    combustivel = combustivelValor,
                                    pedagio = pedagioValor
                                )
                            )

                            // Limpa os campos após cadastrar
                            data = ""
                            kmInicial = ""
                            kmFinal = ""
                            litros = ""
                            combustivel = ""
                            pedagio = ""
                        }
                    }
                ) {
                    Text("Cadastrar viagem")
                }
            }

            item {
                HorizontalDivider()

                Text(
                    text = "Resumo",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Text(
                            "Total de viagens: ${viagens.size}"
                        )

                        Text(
                            "Total de KM: ${
                                String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    totalKm
                                )
                            } km"
                        )

                        Text(
                            "Total gasto: ${
                                String.format(
                                    Locale.getDefault(),
                                    "R$ %.2f",
                                    totalGasto
                                )
                            }"
                        )

                        Text(
                            "Média: ${
                                String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    mediaKmLitro
                                )
                            } km/l"
                        )
                    }
                }
            }

            item {
                Text(
                    text = "Viagens realizadas",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            if (viagens.isEmpty()) {

                item {
                    Text("Nenhuma viagem cadastrada.")
                }

            } else {

                items(viagens) { viagem ->

                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {

                            Text(
                                text = "📅 ${viagem.data}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                "Distância: ${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        viagem.distancia
                                    )
                                } km"
                            )

                            Text(
                                "Combustível: R$ ${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        viagem.combustivel
                                    )
                                }"
                            )

                            Text(
                                "Pedágio: R$ ${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        viagem.pedagio
                                    )
                                }"
                            )

                            Text(
                                "Custo total: R$ ${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        viagem.custoTotal
                                    )
                                }"
                            )

                            Text(
                                "Consumo: ${
                                    String.format(
                                        Locale.getDefault(),
                                        "%.2f",
                                        viagem.distancia / viagem.litros
                                    )
                                } km/l"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CampoTexto(
    valor: String,
    aoAlterar: (String) -> Unit,
    label: String,
    teclado: KeyboardType
) {
    OutlinedTextField(
        value = valor,
        onValueChange = aoAlterar,
        label = {
            Text(label)
        },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = teclado
        )
    )
}
