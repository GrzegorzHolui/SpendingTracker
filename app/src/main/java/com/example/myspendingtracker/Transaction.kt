package com.example.myspendingtracker

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
//  val label: String,
    @ColumnInfo(name = "labelInfo") val label: String,
    val amount: Double,
    val description: String
) : Serializable