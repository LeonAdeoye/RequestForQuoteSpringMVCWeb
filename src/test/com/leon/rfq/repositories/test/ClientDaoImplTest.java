package com.leon.rfq.repositories.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.domains.ClientDetailImpl;
import com.leon.rfq.repositories.ClientDaoImpl;


@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class ClientDaoImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private TransactionStatus status;
	
	@Autowired
	private ClientDaoImpl clientDaoImpl;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired clientDaoImpl should not be null", this.clientDaoImpl);
		assertNotNull("autowired transaction manager should not be null", this.transactionManager);
		
		this.status = this.transactionManager.getTransaction(new DefaultTransactionDefinition());
	}
	
	@After
	public void tearDown()
	{
		this.transactionManager.rollback(this.status);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllClients()
	{
		int beforeCount = this.clientDaoImpl.getAll().size();
		this.clientDaoImpl.insert("testClient", ClientTierEnum.Top, true, "testUser");
		assertNotNull("getAll method should return a non-null list of clients", this.clientDaoImpl.getAll());
		assertEquals("count of clients should have been incremented ", beforeCount + 1, this.clientDaoImpl.getAll().size());
	}
	
	@Test
    public void get_ValidClientName_ReturnsValidClientMatchingClientName()
	{
		this.clientDaoImpl.insert("testClient", ClientTierEnum.Top, true, "testUser");
		assertEquals("get method should return the client when a valid client name is provided", "testClient", this.clientDaoImpl.get("testClient").getName());
	}
	
	@Test
    public void get_NonExistantClientName_ReturnsNulls()
	{
		assertNull("get method should return null when a non-existant clientId is provided", this.clientDaoImpl.get("testClient"));
	}
		
	@Test
    public void insert_ValidParameters_SavesClientAndReturnsTrue()
	{
		assertTrue("Insert method should save a valid client and return true", this.clientDaoImpl.insert("testClient", ClientTierEnum.Top, true, "testUser"));
		assertEquals("get method should return the newly added client for the saved client name", "testClient", this.clientDaoImpl.get("testClient").getName());
	}
	
	@Test
    public void insert_duplicatedClientName_SaveFailsAndReturnsFalse()
	{
		this.clientDaoImpl.insert("testClient", ClientTierEnum.Top, true, "testUser");
		assertFalse("second save method should return false because clientId already exists", this.clientDaoImpl.insert("testClient", ClientTierEnum.Top, true, "testUser"));
	}
	
	@Test
    public void update_ValidUpdateParameters_ClientPropertiesShouldBeUpdated()
	{
		assertTrue("Insert method should save a valid client and return true", this.clientDaoImpl.insert("testClient", ClientTierEnum.Top, true, "insertUser"));
		ClientDetailImpl newlyAddedClient = this.clientDaoImpl.get("testClient");
		assertTrue("Update should return true", this.clientDaoImpl.update(newlyAddedClient.getClientId(), "updatedClientName", ClientTierEnum.Bottom , false, "updateUser"));
		assertNull("Client with previous client name now updated should NOT be returned", this.clientDaoImpl.get("testClient"));
		
		ClientDetailImpl newlyUpdatedClient = this.clientDaoImpl.get("updatedClientName");
		assertNotNull("Newly updated client should be returned", newlyUpdatedClient);
				
		assertEquals("Updated client's tier should be updated to new value", ClientTierEnum.Bottom, newlyUpdatedClient.getTier());
		assertEquals("Updated client's validity should be updated to new value", false, newlyUpdatedClient.getIsValid());
		assertEquals("Updated client's name should be updated to new value", "updatedClientName", newlyUpdatedClient.getName());
		assertEquals("Updated client's last updated by user should be updated to new value", "updateUser", newlyUpdatedClient.getLastUpdatedBy());
		assertEquals("Updated client's client ID should be the same as originally created value", newlyAddedClient.getClientId(), newlyUpdatedClient.getClientId());
	}
	
	@Test
    public void delete_ValidClientName_DeleteSucceedsAndReturnsTrue()
	{
		this.clientDaoImpl.insert("testClient", ClientTierEnum.Top, true, "testUser");
		assertEquals("previously saved client should exist", "testClient", this.clientDaoImpl.get("testClient").getName());
		assertTrue("delete method should delete client and return true", this.clientDaoImpl.delete("testClient"));
		assertNull("deleted client should not longer exist and get method should return null", this.clientDaoImpl.get("testClient"));
	}
	
	@Test
    public void delete_nonexistentClientName_DeleteFailsAndReturnsFalse()
	{
		assertFalse("delete method should return false because the client does not exist", this.clientDaoImpl.delete("nonExistantClientName"));
	}
		
	@Test
    public void clientExistsWithClientName_ExistingClientName_ReturnsTrue()
	{
		this.clientDaoImpl.insert("testClient", ClientTierEnum.Top, true, "testUser");
		
		assertTrue("clientExistsWithClientName should return true because client name exists", this.clientDaoImpl.clientExistsWithClientName("testClient"));
	}
	
	@Test
    public void clientExistsWithClientId_NonExistentClientName_ReturnsFalse()
	{
		assertFalse("clientExistsWithClientName should return false because client name does not exists", this.clientDaoImpl.clientExistsWithClientName("testClient"));
	}
}
