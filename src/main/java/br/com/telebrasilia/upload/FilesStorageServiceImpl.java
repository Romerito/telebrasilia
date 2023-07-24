package br.com.telebrasilia.upload;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.telebrasilia.dtos.ChamadoDTO;

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
  public void deleteAll() {
   // FileSystemUtils.deleteRecursively(root.toFile());
  }

  @Override
  public Set<String> loadAll(String noProtocolo) {
    try {
      String dir =  Paths.get("uploads\\chamados\\" + noProtocolo).toString();
      return    this.listFiles(dir);
        } catch (Exception e) {
      throw new RuntimeException("Could not load the files!");
    }

  }

  public Set<String> listFiles(String dir) throws IOException {
    try (Stream<Path> stream = Files.list(Paths.get(dir))) {
        return stream
          .filter(file -> !Files.isDirectory(file))
          .map(Path::getFileName)
          .map(Path::toString)
          .collect(Collectors.toSet());
    }
}

 @Override
 public Resource load(ChamadoDTO chamadoDTO) {
   try {
        final Path roots = Paths.get("uploads\\chamados\\" + chamadoDTO.getNuProtocolo());
        Path file = roots.resolve(chamadoDTO.getNoArquivo());
        Resource resource = new UrlResource(file.toUri());
        
        if (resource.exists() || resource.isReadable()) {
          

        return resource;
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }

  }
}