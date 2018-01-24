package top.kanetah.planhv2.api.typeHandler

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
    override fun getNullableResult(rs: ResultSet?, columnIndex: Int): IntArray? {
        return null
    }

    override fun getNullableResult(rs: ResultSet?, columnName: String?): IntArray? {
        return null
    }

    override fun getNullableResult(cs: CallableStatement?, columnIndex: Int): IntArray? {
        return null
    }
    
    override fun setNonNullParameter(ps: PreparedStatement?, i: Int, parameter: IntArray?, jdbcType: JdbcType?) {
        ps?.setString(i, parameter?.toTypedArray()?.contentDeepToString())
    }
}
