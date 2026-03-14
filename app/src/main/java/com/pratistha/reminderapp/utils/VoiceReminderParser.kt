object VoiceReminderParser {

    fun parse(text: String): Pair<String, String>? {

        val regex = Regex(
            "(?:create )?(?:a )?reminder(?: for)? (.*?) (?:at|are)? (\\d{1,2})(?::(\\d{2}))? ?(am|pm|a\\.m\\.|p\\.m\\.)",
            RegexOption.IGNORE_CASE
        )

        val match = regex.find(text) ?: return null

        val title = match.groupValues[1].trim()
        val hour = match.groupValues[2].toInt()
        val minutes = match.groupValues[3].ifEmpty { "00" }
        val ampm = match.groupValues[4].lowercase()

        val hour24 = when {
            ampm.contains("pm") && hour < 12 -> hour + 12
            ampm.contains("am") && hour == 12 -> 0
            else -> hour
        }

        val time = String.format("%02d:%02d", hour24, minutes.toInt())

        return title to time
    }
}
