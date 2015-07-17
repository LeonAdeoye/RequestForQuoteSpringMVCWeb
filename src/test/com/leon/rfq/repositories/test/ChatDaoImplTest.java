package com.leon.rfq.repositories.test;

import static org.junit.Assert.assertNotNull;

import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.leon.rfq.domains.ChatMessageImpl;
import com.leon.rfq.domains.UserDetailImpl;
import com.leon.rfq.repositories.ChatDao;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class ChatDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private DataSourceTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	@Autowired(required=true)
	private ChatDao chatDao;
	
	@BeforeClass
	public static void setup()
	{
	}
	
	@AfterClass
	public static void teardown()
	{
	}
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
	
	@Test
    public void save_Message_ShouldBeSaved()
	{
		UserDetailImpl owner = new UserDetailImpl();
		owner.setUserId("testSender");
		UserDetailImpl recipient = new UserDetailImpl();
		recipient.setUserId("testRecipient");
		
		Set<UserDetailImpl> recipients = new HashSet<>();
		recipients.add(owner);
		recipients.add(recipient);
		
		// Act
		this.chatDao.save(new ChatMessageImpl(owner, recipients, "Test message", Integer.MAX_VALUE));
	}
	
}
