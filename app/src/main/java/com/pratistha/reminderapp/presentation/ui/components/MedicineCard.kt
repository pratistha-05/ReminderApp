package com.pratistha.reminderapp.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pratistha.reminderapp.data.local.Medicine

@Composable
fun MedicineCard(
    medicine: Medicine,
    onEdit: (Medicine) -> Unit
) {
    val isOutOfStock = medicine.quantity <= 0
    val isLowStock = medicine.quantity == 1

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
            1.dp,
            color = Color(0xFF51B295)
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFFFFF)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Medication,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = medicine.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = medicine.purpose,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Black,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = { onEdit(medicine) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFFE5F6F1),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Stock Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = when {
                        isLowStock || isOutOfStock -> MaterialTheme.colorScheme.onError
                        else -> MaterialTheme.colorScheme.tertiaryContainer
                    }
                ) {
                    Text(
                        text =  "${medicine.quantity} units left",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }

                if (isOutOfStock) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Out of Stock" ,
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMedicineCard() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            MedicineCard(
                medicine = Medicine(
                    id = "1",
                    name = "Paracetamol 650",
                    quantity = 10,
                    purpose = "Fever & Body Pain"
                ),
                onEdit = {}
            )
            MedicineCard(
                medicine = Medicine(
                    id = "2",
                    name = "Amoxicillin",
                    quantity = 1,
                    purpose = "Antibiotic"
                ),
                onEdit = {}
            )
            MedicineCard(
                medicine = Medicine(
                    id = "3",
                    name = "Cetirizine",
                    quantity = 0,
                    purpose = "Allergy"
                ),
                onEdit = {}
            )
        }
    }
}
