package br.com.telebrasilia.upload;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import br.com.telebrasilia.dtos.ChamadoDTO;

public interface FilesStorageService {
  
  public String save(MultipartFile[] file, String noProtocolo);

  public Resource load(ChamadoDTO chamadoDTO);

  public void deleteAll();

  public  List<String> loadAll(String noProtocolo);
}