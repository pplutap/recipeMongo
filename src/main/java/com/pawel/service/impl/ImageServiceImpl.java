package com.pawel.service.impl;

import com.pawel.domain.Recipe;
import com.pawel.repositories.RecipeRepository;
import com.pawel.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

	private final RecipeRepository recipeRepository;

	public ImageServiceImpl(RecipeRepository recipeRepository) {
		this.recipeRepository = recipeRepository;
	}

	@Override
	public void saveImageFile(Long recipeId, MultipartFile file) {
		log.debug("File " + file.getName() + " received");
		try {
			Recipe recipe = recipeRepository.findById(recipeId).get();
			Byte[] bytes = new Byte[file.getBytes().length];
			int i = 0;
			for (byte b : file.getBytes()) {
				bytes[i++] = b;
			}
			recipe.setImage(bytes);
			recipeRepository.save(recipe);
			log.debug("File " + file.getName() + " saved");
		} catch (IOException e) {
			log.error("Error during saving file");
			e.printStackTrace();
		}

	}
}
