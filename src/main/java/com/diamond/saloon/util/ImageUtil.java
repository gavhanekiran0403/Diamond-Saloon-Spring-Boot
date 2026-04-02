package com.diamond.saloon.util;

import java.util.Base64;

import org.springframework.web.multipart.MultipartFile;

import com.diamond.saloon.exception.BadRequestException;

public class ImageUtil {

	public static String ConvertToBase64(MultipartFile file) {

		try {
			return Base64.getEncoder().encodeToString(file.getBytes());
		} catch (Exception e) {
			throw new BadRequestException("Failed to process image");
		}
	}
}
