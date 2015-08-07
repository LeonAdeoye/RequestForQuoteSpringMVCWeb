package com.leon.rfq.repositories;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

public class YesNoBooleanTypeHandler extends BaseTypeHandler<Boolean>
{
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Boolean parameter, JdbcType jdbcType) throws SQLException
    {
        ps.setString(i, convert(parameter));
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, String columnName) throws SQLException
    {
        return convert(rs.getString(columnName));
    }

    @Override
    public Boolean getNullableResult(ResultSet rs, int columnIndex) throws SQLException
    {
        return convert(rs.getString(columnIndex));
    }

    @Override
    public Boolean getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
    {
        return convert(cs.getString(columnIndex));
    }

    private String convert(Boolean b)
    {
        return b ? "Y" : "N";
    }

    private Boolean convert(String s)
    {
        return s.equals("Y");
    }

}
