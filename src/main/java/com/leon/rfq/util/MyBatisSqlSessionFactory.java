package com.leon.rfq.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisSqlSessionFactory
{
	private static SqlSessionFactory sqlSessionFactory;
	
	public static SqlSessionFactory getSqlSessionFactory()
	{
		if(sqlSessionFactory==null)
		{
			InputStream inputStream = null;
			try
			{
				inputStream = Resources.getResourceAsStream("mybatis-config.xml");
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
			}
			catch (IOException e)
			{
				throw new RuntimeException(e.getCause());
			}
			finally
			{
				if(inputStream != null)
				{
					try
					{
						inputStream.close();
					}
					catch (IOException e)
					{
					}
				}
			}
		}
		return sqlSessionFactory;
	}
	
	public static SqlSession getSqlSession()
	{
		return getSqlSessionFactory().openSession();
	}
	
	public static Connection getConnection()
	{
		// TO-DO Remove connection details.
		String driver = "com.mysql.jdbc.Driver";
		String url = "jdbc:mysql://localhost/rfq_development";
		String username = "root";
		String password = "liverpool";
		Connection connection = null;
		try
		{
			Class.forName(driver);
			connection = DriverManager.getConnection(url, username, password);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		return connection;
	}
}
