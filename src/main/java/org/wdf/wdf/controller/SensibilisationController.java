package org.wdf.wdf.controller;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import org.wdf.wdf.entity.Sensibilisations;
import org.wdf.wdf.repository.MedaRepository;
import org.wdf.wdf.repository.SensibilisationRepository;
import org.wdf.wdf.services.StorageService;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/sensibilisation")
public class SensibilisationController {
	@Autowired
	StorageService storageService;
	
	@Autowired
	MedaRepository mediaRepository;
	
	@Autowired
	SensibilisationRepository sensibilisationRepository;
	
	@PostMapping(value = "/save")
	public Sensibilisation SaveSensibilisation(@RequestParam("files") MultipartFile[] files,
			@RequestParam("titre") String titre ,
			@RequestParam("description") String description,
			@RequestParam("datePublication") String datePublication
			) throws ParseException {
		
		MultipartFile[] listeFiles=files;
		List<Media> listMedia=new ArrayList<Media>();
		for(MultipartFile mf:listeFiles) {
			System.out.println(mf.getOriginalFilename());
			Media md=new Media();
			md.setChemin(mf.getOriginalFilename());
			listMedia.add(md);
		}
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Sensibilisation d=new Sensibilisation();
		d.setTitre(titre);
		d.setDescription(description);
		d.setDatePublication(df.parse(datePublication));
		Sensibilisation dep=sensibilisationRepository.save(d);
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
				m.setSensibilisation(dep);
				m.setChemin(dossier+"/"+m.getChemin());
				mediaRepository.save(m);
			}
		}
		return dep;
	}

	
	@GetMapping(value = "/list")
	public ResponseEntity<List<Sensibilisations>> ListeSensibilisation(){
		List<Sensibilisation> listeSensibilisation=sensibilisationRepository.findAll();		
		List<Media> listeMedia=new ArrayList<Media>();
		List<Sensibilisations> listesSensibilisations =new ArrayList<Sensibilisations>();
		DateFormat df =new SimpleDateFormat("yyyy-MM-dd");
		for(Sensibilisation d:listeSensibilisation) {
		listeMedia=mediaRepository.findBySensibilisation(d);
		List<String> listeRessource =new ArrayList<String>();
			for(int i=0;i<listeMedia.size();i++) {
				String file = storageService.CheminFichier(listeMedia.get(i).getChemin());
				listeRessource.add(file);
			}
			Sensibilisations de=new Sensibilisations();
			de.setSensibilisation(d);
			de.setFichier(listeRessource);
			listesSensibilisations.add(de);
		}	
		return ResponseEntity.ok().body(listesSensibilisations);
	}
// suppression de sensibilisation
	@DeleteMapping("/delete/{id}")
    public String deleteSensibilisation(@PathVariable  long id){
    Sensibilisation d=new Sensibilisation();
	 d.setId(id);
	List<Media> listeMedia=mediaRepository.findBySensibilisation(d);
	for(Media m: listeMedia){
		storageService.DeleteFile(m.getChemin());
		mediaRepository.delete(m);
	}
    	sensibilisationRepository.delete(d);
      
        return "bien supprimer";
    }


}
