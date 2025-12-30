package com.passwordkeeper.domain.model

data class Password(
    val id: Long,
    val title: String ,
    val userId: String = "",
    val password: String = "",
    val memo: String = "",
    val activityTime: Long = System.currentTimeMillis(),
    val type: ItemType,
)