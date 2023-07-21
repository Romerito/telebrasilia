package br.com.telebrasilia.upload;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FilesStorageServiceImpl implements FilesStorageService {


  
  @Override
  public String save(MultipartFile[] files, String noProtocolo) {
    try {
      final Path roots = Paths.get("uploads\\chamados\\" + noProtocolo);
      Files.createDirectories(roots);
  
      for (MultipartFile file : files) {
        Files.copy(file.getInputStream(), roots.resolve(file.getOriginalFilename()));
      }
      return roots.toAbsolutePath().toString();
  
    } catch (Exception e) {
      if (e instanceof FileAlreadyExistsException) {
        throw new RuntimeException("A file of that name already exists.");
      }

      throw new RuntimeException(e.getMessage());
    }
  }


  @Override
  public Resource load(String filename) {
    return null;
   // try {
   //   Path file = root.resolve(filename);
   //   Resource resource = new UrlResource(file.toUri());

   //   if (resource.exists() || resource.isReadable()) {
   //     return resource;
  //    } else {
    //    throw new RuntimeException("Could not read the file!");
    //  }
  //  } catch (MalformedURLException e) {
  //    throw new RuntimeException("Error: " + e.getMessage());
   // }
  }

  @Override
  public void deleteAll() {
   // FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Stream<Path> loadAll(String noProtocolo) {
    try {
      final Path roots = Paths.get("uploads\\chamados\\" + noProtocolo);
      return Files.walk(roots, 1).filter(path -> !path.equals(roots)).map(roots::relativize);
    } catch (IOException e) {
      throw new RuntimeException("Could not load the files!");
    }

  }
}