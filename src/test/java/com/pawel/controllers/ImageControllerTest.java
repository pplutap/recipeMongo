package com.pawel.controllers;

import com.pawel.commands.RecipeCommand;
import com.pawel.service.ImageService;
import com.pawel.service.RecipeService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

public class ImageControllerTest {

	@Mock
	ImageService imageService;

	@Mock
	RecipeService recipeService;

	ImageController imageController;

	MockMvc mockMvc;

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		imageController = new ImageController(imageService, recipeService);
		mockMvc = MockMvcBuilders
				.standaloneSetup(imageController)
				.setControllerAdvice(new ControllerExceptionHandler())
				.build();
	}

	@Test
	public void getImageForm() throws Exception {
		//given
		RecipeCommand recipeCommand = new RecipeCommand();
		recipeCommand.setId(1L);

		when((recipeService).findCommandById(anyLong())).thenReturn(recipeCommand);
		//when
		mockMvc.perform(get("/recipe/1/image"))
				.andExpect(status().isOk())
				.andExpect(model().attributeExists("recipe"));

		verify(recipeService, times(1)).findCommandById(anyLong());
	}

	@Test
	public void handleImagePost() throws Exception {
		MockMultipartFile multipartFile =
				new MockMultipartFile("imageFile", "testing.txt", "text/plain", "test".getBytes());

		mockMvc.perform(multipart("/recipe/1/image").file(multipartFile))
				.andExpect(status().is3xxRedirection())
				.andExpect(header().string("Location", "/recipe/1/show"));

		verify(imageService, times(1)).saveImageFile(anyLong(), any());
	}

	@Test
	public void renderImageFromDB() throws Exception {
		//given
		RecipeCommand command = new RecipeCommand();
		command.setId(1L);
		String source = "image text";
		Byte[] bytes = new Byte[source.getBytes().length];
		int i = 0;
		for (byte b : source.getBytes()) {
			bytes[i++] = b;
		}
		command.setImage(bytes);

		when(recipeService.findCommandById(anyLong())).thenReturn(command);

		//when
		MockHttpServletResponse response = mockMvc.perform(get("/recipe/1/recipeimage"))
				.andExpect(status().isOk())
				.andReturn().getResponse();

		byte[] responseBytes = response.getContentAsByteArray();

		//then
		assertEquals(source.getBytes().length, responseBytes.length);
	}

	@Test
	public void testGetImageNumberFormatException() throws Exception {
		mockMvc.perform(get("/recipe/asdf/recipeimage"))
				.andExpect(status().isBadRequest())
				.andExpect(view().name("400error"));
	}
}
