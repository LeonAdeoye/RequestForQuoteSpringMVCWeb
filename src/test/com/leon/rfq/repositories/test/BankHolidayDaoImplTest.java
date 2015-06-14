package com.leon.rfq.repositories.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

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

import com.leon.rfq.common.EnumTypes.LocationEnum;
import com.leon.rfq.repositories.BankHolidayDaoImpl;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class BankHolidayDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired(required=true)
	private DataSourceTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	@Autowired(required=true)
	private BankHolidayDaoImpl bankHolidayDaoImpl;
	
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
		assertNotNull("autowired bankHolidayDaoImpl should not be null", this.bankHolidayDaoImpl);
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllBankHolidays()
	{
		int beforeCount = this.bankHolidayDaoImpl.getAll().size();
		this.bankHolidayDaoImpl.insert(LocationEnum.FRANKFURT, LocalDate.now(), "testUser");
		assertNotNull("getAll method should return a non-null list of bank holidays", this.bankHolidayDaoImpl.getAll());
		assertEquals("count of bank holidays should have been incremented ", beforeCount + 1, this.bankHolidayDaoImpl.getAll().size());
	}
	
	@Test
    public void updateValidity_ValidbankHolidayCode_UpdatesValidityReturnsTrue()
	{
		this.bankHolidayDaoImpl.insert(LocationEnum.FRANKFURT, LocalDate.now(), "testUser");

		assertTrue("updateValidity method should update validity to the provided value for the saved bank holiday", this.bankHolidayDaoImpl
				.updateValidity(LocationEnum.FRANKFURT, LocalDate.now(), false, "testUser"));
	}
	
	@Test
    public void updateValidity_NonExistantbankHolidayCode_ReturnsFalse()
	{
		assertFalse("updateValidity method should return false for a non-existant bank holiday", this.bankHolidayDaoImpl
				.updateValidity(LocationEnum.FRANKFURT, LocalDate.now(), false, "testUser"));
	}
		
	@Test
    public void insert_duplicatedbankHolidayCode_SaveFailsAndReturnsFalse()
	{
		this.bankHolidayDaoImpl.insert(LocationEnum.FRANKFURT, LocalDate.now(), "testUser");
		assertFalse("second save method should return false because bank holiday already exists", this.bankHolidayDaoImpl.insert(LocationEnum.FRANKFURT, LocalDate.now(), "testUser"));
	}
	
	
	@Test
    public void delete_ValidbankHolidayCode_DeleteSucceedsAndReturnsTrue()
	{
		this.bankHolidayDaoImpl.insert(LocationEnum.FRANKFURT, LocalDate.now(), "testUser");
		assertTrue("delete method should delete bank holiday and return true", this.bankHolidayDaoImpl.delete(LocationEnum.FRANKFURT));
		assertFalse("bankHolidayExists should return false because bankHolidayCode no longer exists", this.bankHolidayDaoImpl.bankHolidayExists(LocationEnum.FRANKFURT, LocalDate.now()));
		
	}
	
	@Test
    public void delete_nonexistentbankHolidayCode_DeleteFailsAndReturnsFalse()
	{
		assertFalse("delete method should return false because the bank holiday does not exist", this.bankHolidayDaoImpl.delete(Integer.MAX_VALUE));
	}
		
	@Test
    public void bankHolidayExists_ExistingbankHolidayCode_ReturnsTrue()
	{
		this.bankHolidayDaoImpl.insert(LocationEnum.FRANKFURT, LocalDate.now(), "testUser");
		
		assertTrue("bankHolidayExists should return true because bankHolidayCode exists", this.bankHolidayDaoImpl.bankHolidayExists(LocationEnum.FRANKFURT, LocalDate.now()));
	}
	
	@Test
    public void bankHolidayExists_NonexistentbankHolidayCode_ReturnsFalse()
	{
		assertFalse("bankHolidayExists should return false because bankHolidayCode does not exists", this.bankHolidayDaoImpl.bankHolidayExists(LocationEnum.FRANKFURT, LocalDate.now()));
	}
}
