package com.gray.Services.Impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gray.Entity.Post;
import com.gray.Exceptions.EmptyException;
import com.gray.Exceptions.IllegalArgumentException;
import com.gray.Exceptions.ResourcesNotFoundException;
import com.gray.Repositories.PostsRepositorie;

@Service
public class FileUplodeStstemClass {

	@Autowired
	private PostsRepositorie postsRepositorie;

	public void uplodeImage(Integer postId, String path, MultipartFile multipartFile) throws Exception {
		// Image uploading work start
		Post post = this.postsRepositorie.findById(postId)
				.orElseThrow(() -> new ResourcesNotFoundException("Post", "postId", postId));
		if (multipartFile.isEmpty()) {
			throw new EmptyException("Image is required");
		}
		// File Name
		String fileName = multipartFile.getOriginalFilename();

		// FullPath
		String filePath = path + File.separator + fileName;

		// Create a folder if not created
		File file1 = new File(path);
		if (!file1.exists()) {
			file1.mkdir();
		}
		// File copy
		Files.copy(multipartFile.getInputStream(), Paths.get(filePath));
		post.setImageName(fileName);
		this.postsRepositorie.save(post);
	}

	public String uploadImageInPost(String path, MultipartFile[] files) throws Exception {
		if (files == null || files.length == 0) {
			throw new EmptyException("At last one image is required");
		}

		// collect all uplode image name
		List<String> imageName = new ArrayList<>();

		// craete Directry
		File fileDirectry = new File(path);
		if (!fileDirectry.exists()) {
			boolean created = fileDirectry.mkdirs();
			if (!created) {
				throw new IOException("failed to create directry" + path);
			}
		}
		for (MultipartFile file : files) {
			if (file.isEmpty() || file == null) {
				throw new EmptyException("One of the uplode file is empty");
			}

			String name = file.getOriginalFilename();
			String extention=FilenameUtils.getExtension(name);
			if(!extention.equals("jpg")&&!extention.equals("png"))
			{
				throw new IllegalArgumentException("Onlay jpg/png allowed");
			}
			if(file.getSize()>2 * 1024 * 1024)
			{
				throw new IllegalArgumentException("File To large image/2MB");
			}
			
			String newName=UUID.randomUUID().toString()+"."+extention;
			
			String fullPath = path + File.separator + newName;

			Files.copy(file.getInputStream(), Paths.get(fullPath));

			imageName.add(newName);
		}

		return String.join(",", imageName);
	}

	public String fileUplodingForImage(String path, MultipartFile file) throws Exception {
		if (file == null || file.isEmpty()) {
			throw new EmptyException("Plase select a valid :Image:");
		}

		File fileDirectry = new File(path);
		if (!fileDirectry.exists()) {
			boolean created = fileDirectry.mkdirs();
			if (!created) {
				throw new EmptyException("File not creted ");
			}
		}

		String fileName = file.getOriginalFilename();
		String extention=FilenameUtils.getExtension(fileName);
		
		if(!extention.equals("jpg")&&!extention.equals("png"))
		{
			throw new IllegalArgumentException("Onlay jpg/png allowed");
		}
		
		if(file.getSize()>2 *1024 *1024)
		{
			throw new IllegalArgumentException("File to large /1 image= 2MB");
		}

		String newName=UUID.randomUUID().toString()+"."+extention;
		
		String fullPath = path + File.separator + newName;

		Files.copy(file.getInputStream(), Paths.get(fullPath));

		return newName;

	}
}
