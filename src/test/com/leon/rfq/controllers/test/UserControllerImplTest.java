package com.leon.rfq.controllers.test;
 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.leon.rfq.domains.UserDetailImpl;

@ContextConfiguration(locations = { "classpath: **/applicationContext.xml" })
@WebAppConfiguration
public class UserControllerImplTest extends AbstractJUnit4SpringContextTests
{
	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	
	@Before
	public void setup()
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}
	
	@Test
	public void testGetUsers_WithNoParam_shouldReturnAModelWithAUsersAttribute() throws Exception
	{
		this.mockMvc.perform(get("/users")).andExpect(model().attributeExists("users"));
	}
	
	@Test
	public void testGetUser_WithUserIdParam_shouldReturnAModelWithAUsersAttribute() throws Exception
	{
		// Arrange
		UserDetailImpl user = new UserDetailImpl("testUser", "test@test.com", "firstName",
				"lastName", "testLocation", "testGroup", true, "testUser");
		//Act & Assert
		this.mockMvc.perform(get("/users/user").param("userId", "testUser"))
		.andExpect(model().attributeExists("user")).andExpect(model().attribute("user", user));
	}
}
