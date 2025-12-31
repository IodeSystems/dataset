package com.iodesystems.db.util

import java.util.*

object StringUtil {

  // Convert snake_case to camelCase, keeping the first letter lowercase
  fun String.snakeToCamelCase(): String {
    return this.split("_")
      .mapIndexed { index, s ->
        if (index == 0) s.lowercase()
        else s.lowercase().replaceFirstChar { char -> char.titlecase(Locale.getDefault()) }
      }.joinToString("")
  }

  fun String.camelToTitleCase(): String {
    // Split the string by capital letters
    return this.split("(?=[A-Z])".toRegex())
      .joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.titlecase(Locale.getDefault()) } }
  }

  fun String.isSnakeCase(): Boolean {
    return this.matches(Regex("^[a-z]+(_[a-z]+)*$"))
  }

  fun String.isCamelCase(): Boolean {
    return this.matches(Regex("^[a-z]+([A-Z][a-z]+)*$"))
  }

  fun String.snakeToTitleCase(): String {
    return this.split("_")
      .joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.titlecase(Locale.getDefault()) } }
  }

  fun String.titleCase(): String {
    return this.split(" ")
      .joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.titlecase(Locale.getDefault()) } }
  }
}
