package br.com.telebrasilia.upload;

import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  
  public String save(MultipartFile[] file, String noProtocolo);

  public Resource load(String filename);

  public void deleteAll();

  public Set<String> loadAll(String noProtocolo);
}