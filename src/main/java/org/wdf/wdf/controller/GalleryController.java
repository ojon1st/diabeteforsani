package org.wdf.wdf.controller;

import java.io.File;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.wdf.wdf.entity.Gallerie;
import org.wdf.wdf.entity.Galleries;
import org.wdf.wdf.entity.Media;
import org.wdf.wdf.entity.Sensibilisation;
import org.wdf.wdf.repository.GalleryRepository;
import org.wdf.wdf.repository.MedaRepository;
import org.wdf.wdf.services.StorageService;

@RestController
public class GalleryController {
	@Autowired
	StorageService storageService;
	
	@Autowired
	MedaRepository mediaRepository;
	
	@Autowired
	GalleryRepository galleryRepository;
	
	//@PostMapping(value = "/save")
	@RequestMapping(value = "/api/gallery/save")
	public Gallerie SaveGallerie(@RequestParam("files") MultipartFile[] files,
			@RequestParam("titre") String titre 
			) throws ParseException {
		
		MultipartFile[] listeFiles=files;
		List<Media> listMedia=new ArrayList<Media>();
		for(MultipartFile mf:listeFiles) {
			System.out.println(mf.getOriginalFilename());
			Media md=new Media();
			md.setChemin(mf.getOriginalFilename());
			listMedia.add(md);
		}
		Gallerie d=new Gallerie();
		d.setTitre(titre);
		Gallerie dep=galleryRepository.save(d);
		int compteur=0;
		Instant instant=Instant.now();
		String  dossier=""+instant.getEpochSecond();
		File path=new File("upload-dir/"+dossier);
		//path.mkdirs();
		for(int i=0;i<listeFiles.length;i++){
			System.out.println( " fichier : "+listeFiles[i].getOriginalFilename().toString()+ " dossier : "+dossier);
			storageService.store(listeFiles[i],dossier);
			compteur++;
		}
		if(compteur==listeFiles.length) {
			for(Media m:listMedia) {
				m.setGallerie(dep);
				m.setChemin(dossier+"/"+m.getChemin());
				mediaRepository.save(m);
			}
		}
		return dep;
	}

	
	//@GetMapping(value = "/list")
	@RequestMapping(value = "/api/gallery/list")
	public ResponseEntity<List<Galleries>> ListeGallerie(){
		List<Gallerie> listeGallerie=galleryRepository.findAll();		
		List<Media> listeMedia=new ArrayList<Media>();
		List<Galleries> listesGalleries =new ArrayList<Galleries>();
		for(Gallerie d:listeGallerie) {
		listeMedia=mediaRepository.findByGallerie(d);
		List<String> listeRessource =new ArrayList<String>();
			for(int i=0;i<listeMedia.size();i++) {
				String file = storageService.CheminFichier(listeMedia.get(i).getChemin());
				listeRessource.add(file);
			}
			Galleries de=new Galleries();
			de.setGallerie(d);
			de.setFichier(listeRessource);
			listesGalleries.add(de);
		}	
		return ResponseEntity.ok().body(listesGalleries);
	}
	// supprimer un gallery
	@DeleteMapping("/delete/{id}")
    public String deleteGallery(@PathVariable  long id){
    Gallerie d=new Gallerie();
	 d.setId(id);
	List<Media> listeMedia=mediaRepository.findByGallerie(d);
	for(Media m: listeMedia){
		storageService.DeleteFile(m.getChemin());
		mediaRepository.delete(m);
	}
	      galleryRepository.delete(d);
      
        return "gallery bien supprimer";
    }
}
