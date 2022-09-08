package com.afb.dsi.numarch.services;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface IManageFileService {
	
	public void uploadFile(MultipartFile file);
	
	public void fileName();
	
	public List<MultipartFile> listFiles();
	
	public void init();
	
	public void deleteAll();

	public Stream<Path> loadAll();
	
	public Resource load(String filename);
}
