package org.wdf.wdf.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RestController;
import org.wdf.wdf.entity.Media;
import org.wdf.wdf.repository.MedaRepository;
import org.wdf.wdf.services.StorageService;


@RestController
public class MediaController {
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	MedaRepository mediaRepository ;
	

	public List<Media> ListMedia(){
		List<String> listeFiles=new ArrayList<String>();
		List<Resource> listeRessource =new ArrayList<Resource>();
		for(int i=0;i<listeFiles.size();i++) {
			Resource file = storageService.loadFile(listeFiles.get(i));
			listeRessource.add(file);
		}
		return null;
	}
}
