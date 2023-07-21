package br.com.telebrasilia.upload;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesStorageService {
  
  public String save(MultipartFile[] file, String noProtocolo);

  public Resource load(String filename);

  public void deleteAll();

  public Stream<Path> loadAll(String noProtocolo);
}