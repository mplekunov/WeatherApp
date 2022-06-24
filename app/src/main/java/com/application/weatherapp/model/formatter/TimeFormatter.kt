package com.application.weatherapp.model.formatter

import java.time.LocalDateTime
import java.time.ZonedDateTime

object TimeFormatter {
    fun formatZonedTime(zonedTime: ZonedDateTime) : LocalDateTime {
        return zonedTime
            .toOffsetDateTime()
            .atZoneSameInstant(ZonedDateTime.now().zone)
            .toLocalDateTime()
    }
}