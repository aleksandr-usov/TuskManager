package com.example.tuskmanager

import java.util.*
import java.util.concurrent.TimeUnit


fun Date.plus(value: Long, unit: TimeUnit): Date {
    return Date(this.time + unit.toMillis(value))
}

fun Date.minus(value: Long, unit: TimeUnit): Date {
    return Date(this.time - unit.toMillis(value))
}