package com.Backend.Back.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileUploadUtil {

	public static String saveFile(String fileName, MultipartFile multipartFile) throws IOException {
		try {
			Path uploadDirectory = Paths.get("Files-Upload");
			if (!Files.exists(uploadDirectory)) {
				Files.createDirectories(uploadDirectory);
			}

			String fileCode = RandomStringUtils.randomAlphanumeric(8);

			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadDirectory.resolve(fileCode + "-" + fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			}

			return fileCode;
		} catch (IOException e) {
			// Log the exception and re-throw it for handling in the caller method
			String errorMessage = "Error saving uploaded file: " + fileName;
			// Log the exception for debugging purposes
			e.printStackTrace();
			throw new IOException(errorMessage, e);
		}
	}
	public static String saveVideo(String videoName, MultipartFile videoFile) throws IOException {
		// Define the directory where video files will be uploaded
		Path uploadDirectory = Paths.get("Videos-Upload");

		// Generate a unique file code
		String videoCode = StringUtils.cleanPath(videoName) + "-" + System.currentTimeMillis();

		try (InputStream inputStream = videoFile.getInputStream()) {
			// Resolve the file path
			Path videoFilePath = uploadDirectory.resolve(videoCode + "-" + videoName);
			// Copy the video file to the upload directory
			Files.copy(inputStream, videoFilePath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ioe) {
			throw new IOException("Error saving uploaded video: " + videoName, ioe);
		}

		return videoCode;
	}
}
