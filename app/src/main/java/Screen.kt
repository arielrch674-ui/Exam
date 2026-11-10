import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 1. Modelo de datos
data class Producto(val nombre: String, val precio: Double)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemartScreen() {
    // Catálogo con los 4 productos oficiales del Pokémart
    val catalogo = remember {
        listOf(
            Producto("Poción", 20.00),
            Producto("Pokebola", 25.00),
            Producto("Antídoto", 12.50),
            Producto("Revivir", 60.00)
        )
    }

    // Estado de las cantidades inicializadas en 0
    val cantidades = remember { mutableStateListOf(0, 0, 0, 0) }

    // --- CÁLCULOS DERIVADOS ---
    val articulos = cantidades.sum()

    val subtotal = catalogo.indices.sumOf { catalogo[it].precio * cantidades[it] }

    val descuento = if (subtotal >= 150.0) subtotal * 0.10 else 0.0
    val subtotalConDescuento = subtotal - descuento

    val envio = if (articulos == 0) {
        0.0
    } else if (subtotalConDescuento >= 120.0) {
        0.0
    } else {
        15.0
    }

    val total = subtotalConDescuento + envio

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pokémart") }
            )
        },
        bottomBar = {
            // El contenedor del resumen y botones de acción
            ResumenCarrito(
                articulos = articulos,
                subtotal = subtotal,
                descuento = descuento,
                envio = envio,
                total = total,
                onVaciarCarrito = {
                    for (i in cantidades.indices) cantidades[i] = 0
                },
                onConfirmarPedido = {
                    /* Acción de confirmar */
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(catalogo) { index, producto ->
                ProductoItem(
                    producto = producto,
                    cantidad = cantidades[index],
                    onCantidadChange = { nuevaCantidad ->
                        // Validación de rango 0 a 10
                        if (nuevaCantidad in 0..10) {
                            cantidades[index] = nuevaCantidad
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ProductoItem(
    producto: Producto,
    cantidad: Int,
    onCantidadChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = producto.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = "$${String.format("%.2f", producto.precio)}", style = MaterialTheme.typography.bodyMedium)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón menos (-)
                IconButton(
                    onClick = { onCantidadChange(cantidad - 1) },
                    enabled = cantidad > 0
                ) {
                    Text("-", style = MaterialTheme.typography.titleLarge)
                }

                Text(text = "$cantidad", style = MaterialTheme.typography.bodyLarge)

                // Botón más (+)
                IconButton(
                    onClick = { onCantidadChange(cantidad + 1) },
                    enabled = cantidad < 10
                ) {
                    Text("+", style = MaterialTheme.typography.titleLarge)
                }
            }
        }
    }
}

@Composable
fun ResumenCarrito(
    articulos: Int,
    subtotal: Double,
    descuento: Double,
    envio: Double,
    total: Double,
    onVaciarCarrito: () -> Unit,
    onConfirmarPedido: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        tonalElevation = 8.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Filas de información financiera
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Artículos:")
                Text("$articulos")
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Subtotal:")
                Text("$${String.format("%.2f", subtotal)}")
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Descuento (10%):")
                Text("-$${String.format("%.2f", descuento)}")
            }
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Envío:")
                Text(if (envio == 0.0 && articulos > 0) "Gratis" else "$${String.format("%.2f", envio)}")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text("Total:", style = MaterialTheme.typography.titleMedium)
                Text("$${String.format("%.2f", total)}", style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de control inferiores
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onVaciarCarrito,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Vaciar carrito")
                }
                Button(
                    onClick = onConfirmarPedido,
                    modifier = Modifier.weight(1f),
                    enabled = articulos > 0 // Deshabilitado si el carrito está vacío
                ) {
                    Text("Confirmar pedido")
                }
            }
        }
    }
}
