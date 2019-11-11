package org.wdf.wdf.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import javax.servlet.ServletContext;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageService {
@Autowired
ServletContext context;

  Logger log = LoggerFactory.getLogger(this.getClass().getName());
  private final Path rootLocation = Paths.get("upload-dir");
  
  public long store(MultipartFile file,String dossier){
	    try {
	    	File dos=new File("upload-dir",dossier);
	    	dos.mkdirs();
	    	Path rootLoc = Paths.get("upload-dir/"+dossier);
	    	long copie=Files.copy(file.getInputStream(), rootLoc.resolve(file.getOriginalFilename()));
	    	if(copie>0) {
	    		return copie;
	    	}
	    } catch (Exception e) {
	      throw new RuntimeException("erreur de stockage de fichier ");
	    }
		return 0;
	  }
		
  	public Resource loadFile(String chemin) {
	    try {
	      Path file = rootLocation.resolve(chemin);
	      Resource resource = new UrlResource(file.toUri());
	      if (resource.exists() || resource.isReadable()) {
	        return resource;
	      } else {
	        throw new RuntimeException("Erreur de recuperation de la ressource");
	      }
	    } catch (MalformedURLException e) {
	      throw new RuntimeException("echoue!");
	    }
	 }
  	
  	public String CheminFichier(String nom) {
  		File file=new File(rootLocation+"/"+nom);
  		String encodeBase64=null;
  		String chemin=new String();
  		try {
  			String extension = FilenameUtils.getExtension(nom);
  			FileInputStream fileInputStream=new FileInputStream(file);
  			byte[] bytes=new byte[(int) file.length()];
  			fileInputStream.read(bytes);
  			encodeBase64=Base64.getEncoder().encodeToString(bytes);
  			chemin="data:image/"+extension+";base64,"+encodeBase64;
  			fileInputStream.close();
  		}catch(Exception e) {
  			System.out.println("erreur de recuperation du chemin ");
  		}
  		return chemin;
  	}
  	
  	public void DeleteFile(String nom ) {
  		File file=new File("upload-dir/"+nom);
  		file.delete();
  	}
}
