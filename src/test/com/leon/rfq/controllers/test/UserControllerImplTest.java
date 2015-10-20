package com.leon.rfq.controllers.test;

import static org.junit.Assert.assertNotNull;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.WebApplicationContext;

import com.leon.rfq.services.UserService;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
@WebAppConfiguration
public class UserControllerImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Autowired
	UserService userService;
	
	@Autowired
	private DataSourceTransactionManager transactionManager;
	private TransactionStatus status;
	
	@Before
	public void setup()
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		assertNotNull("autowired userService should not be null", this.userService);
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
}
