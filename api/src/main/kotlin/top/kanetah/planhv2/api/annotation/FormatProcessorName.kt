package top.kanetah.planhv2.api.annotation

/**
 * created by kane on 2018/1/31
 */
@Target(AnnotationTarget.CLASS)
annotation class FormatProcessorName(
        val name: String = ""
)
