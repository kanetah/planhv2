package top.kanetah.planhv2.backend.typeHandler

import org.apache.ibatis.type.BaseTypeHandler
import org.apache.ibatis.type.JdbcType
import org.apache.ibatis.type.MappedJdbcTypes
import org.apache.ibatis.type.MappedTypes
import java.sql.CallableStatement
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * created by kane on 2018/1/24
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes(IntArray::class)
class IntArrayTypeHandler : BaseTypeHandler<IntArray>() {
    override fun getNullableResult(
            rs: ResultSet?, columnIndex: Int
    ) = rs?.getString(columnIndex)?.toIntArray()

    override fun getNullableResult(
            rs: ResultSet?, columnName: String?
    ) = rs?.getString(columnName)?.toIntArray()

    override fun getNullableResult(
            cs: CallableStatement?, columnIndex: Int
    ) = cs?.getString(columnIndex)?.toIntArray()

    override fun setNonNullParameter(
            ps: PreparedStatement?, i: Int, parameter: IntArray?, jdbcType: JdbcType?
    ) {
        ps?.setString(i, parameter?.toTypedArray()?.contentDeepToString())
    }
}

fun String.toIntArray(
) = this.slice(IntRange(1, this.length - 2)).split(", ").let { stringArray ->
    IntArray(stringArray.size).also { intArray ->
        stringArray.withIndex().forEach { (key, value) -> intArray[key] = value.toInt() }
    }
}
