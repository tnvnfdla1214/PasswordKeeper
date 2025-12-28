package com.passwordkeeper.domain.model

sealed class Item {
    abstract val id: Long
    abstract val title: String
    abstract val memo: String
    abstract val activityTime: Long

    data class Password(
        override val id: Long = 0,
        override val title: String,
        val userId: String,
        val password: String,
        override val memo: String = "",
        override val activityTime: Long = System.currentTimeMillis()
    ) : Item()

    data class Memo(
        override val id: Long = 0,
        override val title: String,
        val content: String,
        override val memo: String = "",
        override val activityTime: Long = System.currentTimeMillis()
    ) : Item()
}
