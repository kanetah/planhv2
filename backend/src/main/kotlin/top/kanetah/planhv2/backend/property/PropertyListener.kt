package top.kanetah.planhv2.backend.property

import org.springframework.boot.context.event.ApplicationStartingEvent
import org.springframework.context.ApplicationListener
import org.springframework.core.io.support.PropertiesLoaderUtils
import java.nio.charset.Charset
import java.util.*

/**
 * created by kane on 2018/1/31
 */
@Suppress("unused")
class PropertyListener : ApplicationListener<ApplicationStartingEvent> {

    private val propertyFileName = "planhv2.properties"

    override fun onApplicationEvent(event: ApplicationStartingEvent?) {
        loadAllProperties(propertyFileName)
    }

    companion object {
        private val propertiesMap = HashMap<String, String>()

        private fun processProperties(props: Properties) {
            propertiesMap.clear()
            for (key in props.keys()) {
                val keyStr = key.toString()
                propertiesMap[keyStr] = String(props.getProperty(keyStr).toByteArray(), Charset.defaultCharset())
            }
        }

        fun loadAllProperties(propertiesFileName: String) {
            processProperties(
                    PropertiesLoaderUtils.loadAllProperties(propertiesFileName)
            )
        }

        fun getProperty(name: String) = propertiesMap[name]

        fun getAllProperties() = propertiesMap
    }
}

fun main() {
    PropertyListener.loadAllProperties("planhv2.properties")
    val title = PropertyListener.getProperty("class-title")
    println(title)
}
