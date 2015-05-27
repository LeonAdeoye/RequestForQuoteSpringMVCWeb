package com.leon.rfq.services.test;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.leon.rfq.common.EnumTypes.ClientTierEnum;
import com.leon.rfq.repositories.ClientDao;
import com.leon.rfq.repositories.ClientDaoImpl;
import com.leon.rfq.services.ClientService;
import com.leon.rfq.services.ClientServiceImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
public class ClientServiceImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private ClientService clientService;
	
	@Before
	public void setUp()
	{
		assertNotNull("autowired clientDaoImpl should not be null", this.clientService);
	}
	
	@Test
    public void getAll_ValidScenario_ReturnsValidListOfAllClients()
	{
		assertNotNull("getAll method should return a non-null list of clients", this.clientService.getAll());
	}
	
	@Test
    public void get_NullClientName_ThrowsInvalidArgumentException()
	{
		catchException(this.clientService).get(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void get_EmptyStringClientName_ThrowsInvalidArgumentException()
	{
		catchException(this.clientService).get("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void get_ValidParameter_CallsDaoGetMethod()
	{
		// Arrange
		ClientService clientService = new ClientServiceImpl();
		ClientDao clientDaoMock = mock(ClientDaoImpl.class);
		clientService.setClientDao(clientDaoMock);
		// Act
		clientService.get("testClient");
		// Assert
		verify(clientDaoMock).get("testClient");
	}
	
	@Test
    public void getAll_NoParameters_CallsDaoGetAllMethod()
	{
		// Arrange
		ClientService clientService = new ClientServiceImpl();
		ClientDao clientDaoMock = mock(ClientDaoImpl.class);
		clientService.setClientDao(clientDaoMock);
		// Act
		clientService.getAll();
		// Assert
		verify(clientDaoMock).getAll();
	}
	
	@Test
    public void delete_validClientName_CallsDaoDeleteMethod()
	{
		// Arrange
		ClientService clientService = new ClientServiceImpl();
		ClientDao clientDaoMock = mock(ClientDaoImpl.class);
		clientService.setClientDao(clientDaoMock);
		// Act
		clientService.delete("clientToBeDeleted");
		// Assert
		verify(clientDaoMock, never()).delete("clientToBeDeleted");
	}
	
	@Test
    public void update_NonExistentClientName_DoesNotCallDaoUpdateValidityMethod()
	{
		// Arrange
		ClientService clientService = new ClientServiceImpl();
		ClientDao clientDaoMock = mock(ClientDaoImpl.class);
		clientService.setClientDao(clientDaoMock);
		// Act
		clientService.update(Integer.MAX_VALUE, "testClient", ClientTierEnum.Top, true, "tester");
		// Assert
		verify(clientDaoMock, never()).update(Integer.MAX_VALUE, "testClient", ClientTierEnum.Top, true, "tester");
	}
	
	@Test
    public void update_NonExistentClientName_ReturnFalse()
	{
		// Arrange
		ClientService clientService = new ClientServiceImpl();
		ClientDao clientDaoMock = mock(ClientDaoImpl.class);
		clientService.setClientDao(clientDaoMock);
		// Act and Assert
		assertFalse("update should return false if client does not exist.", clientService.update(Integer.MAX_VALUE, "testClient", ClientTierEnum.Top, true, "tester"));
	}
	
	@Test
    public void update_validClientName_CallsDaoUpdateValidityMethod()
	{
		// Arrange
		ClientService clientService = new ClientServiceImpl();
		ClientDao clientDaoMock = mock(ClientDaoImpl.class);
		clientService.setClientDao(clientDaoMock);
		// Act
		clientService.update(Integer.MAX_VALUE, "testClient", ClientTierEnum.Top, true, "tester");
		// Assert
		verify(clientDaoMock, never()).update(Integer.MAX_VALUE, "testClient", ClientTierEnum.Top, true, "tester");
	}
	
	@Test
    public void insert_validParameters_CallsDaoSaveMethod()
	{
		// Arrange
		ClientService clientService = new ClientServiceImpl();
		ClientDao clientDaoMock = mock(ClientDaoImpl.class);
		clientService.setClientDao(clientDaoMock);
		// Act
		clientService.insert("testClient", ClientTierEnum.Top, true, "tester");
		// Assert
		verify(clientDaoMock).insert("testClient", ClientTierEnum.Top, true, "tester");
	}
	
	@Test
    public void insert_NullClientName_ThrowsIllegalArgumentException()
	{
		catchException(this.clientService).insert(null, ClientTierEnum.Top, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringClientName_ThrowsIllegalArgumentException()
	{
		catchException(this.clientService).insert("", ClientTierEnum.Top, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void insert_NullSavedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.clientService).insert("clientName", ClientTierEnum.Top, true, null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void insert_EmptyStringSavedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.clientService).insert("clientName", ClientTierEnum.Top, true, "");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "savedByUser argument is invalid");
	}
	
	@Test
    public void delete_NullParameter_ThrowsIllegalArgumentException()
	{
		catchException(this.clientService).delete(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void delete_EmptyStringParameter_ThrowsInvalidArgumentException()
	{
		catchException(this.clientService).delete("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void update_NullClientName_ThrowsIllegalArgumentException()
	{
		catchException(this.clientService).update(Integer.MAX_VALUE, null, ClientTierEnum.Top, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void update_EmptyStringClientName_ThrowsInvalidArgumentException()
	{
		catchException(this.clientService).update(Integer.MAX_VALUE, "", ClientTierEnum.Top, true, "tester");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void update_NullUpdatedByUser_ThrowsIllegalArgumentException()
	{
		catchException(this.clientService).update(Integer.MAX_VALUE, "clientName", ClientTierEnum.Top, true, null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void update_EmptyStringUpdatedByUser_ThrowsInvalidArgumentException()
	{
		catchException(this.clientService).update(Integer.MAX_VALUE, "clientName", ClientTierEnum.Top, true, "");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "updatedByUser argument is invalid");
	}
	
	@Test
    public void clientExistsWithClientName_EmptyClientName_ThrowsInvalidArgumentException()
	{
		catchException(this.clientService).clientExistsWithClientName("");
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void clientExistsWithClientName_NullClientName_ThrowsIllegalArgumentException()
	{
		catchException(this.clientService).clientExistsWithClientName(null);
		
		assertTrue("Exception should be an instance of IllegalArgumentException", caughtException() instanceof IllegalArgumentException);
		assertEquals("Exception message should match", caughtException().getMessage(), "clientName argument is invalid");
	}
	
	@Test
    public void clientExistsWithClientName_existingClientName__CallsCorrectDaoMethod()
	{
		// Arrange
		ClientService clientService = new ClientServiceImpl();
		ClientDao clientDaoMock = mock(ClientDaoImpl.class);
		clientService.setClientDao(clientDaoMock);
		// Act
		clientService.clientExistsWithClientName("clientName");
		// Assert
		verify(clientDaoMock).clientExistsWithClientName("clientName");
	}
}
