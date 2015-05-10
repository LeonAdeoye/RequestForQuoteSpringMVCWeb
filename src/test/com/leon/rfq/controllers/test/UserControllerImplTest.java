package com.leon.rfq.controllers.test;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
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
	
	@Test
	public void getAll_NoParam_shouldReturnAModelWithAUsersAttribute() throws Exception
	{
		this.mockMvc.perform(get("/users"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/WEB-INF/views/users.jsp"))
			.andExpect(model().attributeExists("users"))
			.andExpect(view().name("users"));
	}
	
	@Test
	public void get_ValidUserId_ShouldReturnUserModel() throws Exception
	{
		// Arrange
		this.userService.insert("testUser", "test@test.com", "firstName",
				"lastName", "testLocation", "testGroup", true, "testUser");
		//Act & Assert
		this.mockMvc.perform(get("/users/user").param("userId", "testUser"))
			.andExpect(status().isOk())
			.andExpect(forwardedUrl("/WEB-INF/views/user.jsp"))
			.andExpect(model().attributeExists("user"))
			.andExpect(view().name("user"))
			.andExpect(model().attribute("user", this.userService.get("testUser")));
	}
	
	@Test
	public void delete_ValidUserId_ShouldDeleteUser() throws Exception
	{
		// Arrange
		this.userService.insert("testUser", "test@test.com", "firstName",
				"lastName", "testLocation", "testGroup", true, "testUser");
		//Act & Assert
		this.mockMvc.perform(post("/users/delete").param("userId", "testUser"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/users"));
	}
	
	@Test
	public void delete_NonExistentUserId_ShouldReturnErrorModel() throws Exception
	{
		//Act & Assert
		this.mockMvc.perform(post("/users/delete").param("userId", "testUser"))
			.andExpect(status().is3xxRedirection())
			.andExpect(model().attributeExists("error"))
			.andExpect(model().attribute("error", "Failed to delete user with userId: testUser"))
			.andExpect(view().name("redirect:/users"));
	}
	
	@Test
	public void getNewUserForm_ValidUser_ShouldReturnNewUserModel() throws Exception
	{
		//Act & Assert
		this.mockMvc.perform(get("/users/add"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("newUser"))
			.andExpect(view().name("addUser"));
	}
	
	@Test
	public void updateValidity_ValidUserId_ShouldUpdateValidity() throws Exception
	{
		// Arrange
		this.userService.insert("testUser", "test@test.com", "firstName",
				"lastName", "testLocation", "testGroup", true, "testUser");
		//Act & Assert
		this.mockMvc.perform(post("/users/updateValidity")
			.param("userId", "testUser").param("isValid", "false").param("updatedByUser", "testUpdater"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/users"));
	}
	
	@Test
	public void updateValidity_NonExistentUserId_ShouldReturnError() throws Exception
	{
		//Act & Assert
		this.mockMvc.perform(post("/users/updateValidity")
			.param("userId", "testUser").param("isValid", "false").param("updatedByUser", "testUpdater"))
			.andExpect(model().attributeExists("error"))
			.andExpect(model().attribute("error", "Failed to update validity user with userId: testUser"))
			.andExpect(status().is3xxRedirection())
			.andExpect(view().name("redirect:/users"));
	}
}
