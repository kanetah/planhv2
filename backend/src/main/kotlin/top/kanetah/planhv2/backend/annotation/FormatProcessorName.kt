package top.kanetah.planhv2.backend.annotation

/**
 * created by kane on 2018/1/31
 * 备注格式化处理器中文名
 */
@Target(AnnotationTarget.CLASS)
annotation class FormatProcessorName(
        val name: String = ""
)
