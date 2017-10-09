package com.mycollab.schedule.email

/**
 * @author MyCollab Ltd
 * @since 6.0.0
 */
class MailStyles {
    companion object {
        private val _instance: MailStyles = MailStyles()

        fun instance(): MailStyles = _instance
    }

    private val styles = mapOf("footer_background" to "#3A3A3A", "font" to "14px Arial, 'Times New Roman', sans-serif",
            "small_font" to "12px Arial, 'Times New Roman', sans-serif",
            "background" to "#f5faff", "link_color" to "#006DAC",
            "border_color" to "#e5e5e5", "meta_color" to "#999",
            "action_color" to "#24a2e3")

    fun get(name: String): String = styles[name] ?: ""

    fun cell(width: String): String = "width: $width; padding: 10px; vertical-align: top;"
}