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
import org.wdf.wdf.entity.Media;
import org.wdf.wdf.entity.Sensibilisation;
import org.wdf.wdf.entity.Soin;
import org.wdf.wdf.entity.Soins;
import org.wdf.wdf.repository.MedaRepository;
import org.wdf.wdf.repository.SoinRepository;
import org.wdf.wdf.services.StorageService;

@RestController
@RequestMapping(value = "/api/soin")
public class SoinController {
	@Autowired
	StorageService storageService;
	
	@Autowired
	MedaRepository mediaRepository;
	
	@Autowired
    SoinRepository soinRepository;
	
	@PostMapping(value = "/save")
	public Soin SaveSoin(@RequestParam("files") MultipartFile[] files,
			@RequestParam("titre") String titre ,
			@RequestParam("description") String description
			) throws ParseException {
		
		MultipartFile[] listeFiles=files;
		List<Media> listMedia=new ArrayList<Media>();
		for(MultipartFile mf:listeFiles) {
			System.out.println(mf.getOriginalFilename());
			Media md=new Media();
			md.setChemin(mf.getOriginalFilename());
			listMedia.add(md);
		}
		Soin d=new Soin();
		d.setTitre(titre);
		d.setDescription(description);
		Soin dep=soinRepository.save(d);
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
				m.setSoin(dep);
				m.setChemin(dossier+"/"+m.getChemin());
				mediaRepository.save(m);
			}
		}
		return dep;
	}

	
	@GetMapping(value = "/list")
	public ResponseEntity<List<Soins>> ListeSoin(){
		List<Soin> listeSoin=soinRepository.findAll();		
		List<Media> listeMedia=new ArrayList<Media>();
		List<Soins> listesSoins =new ArrayList<Soins>();
		for(Soin d:listeSoin) {
		listeMedia=mediaRepository.findBySoin(d);
		List<String> listeRessource =new ArrayList<String>();
			for(int i=0;i<listeMedia.size();i++) {
				String file = storageService.CheminFichier(listeMedia.get(i).getChemin());
				listeRessource.add(file);
			}
			Soins de=new Soins();
			de.setSoin(d);
			de.setFichier(listeRessource);
			listesSoins.add(de);
		}	
		return ResponseEntity.ok().body(listesSoins);
	}
	
	// suppression de soin
	@DeleteMapping("/delete/{id}")
    public String deleteSoin(@PathVariable  long id){
    Soin d=new Soin();
	 d.setId(id);
	List<Media> listeMedia=mediaRepository.findBySoin(d);
	for(Media m: listeMedia){
		storageService.DeleteFile(m.getChemin());
		mediaRepository.delete(m);
	}
    	soinRepository.delete(d);
      
        return "bien supprimer";
    }

}
