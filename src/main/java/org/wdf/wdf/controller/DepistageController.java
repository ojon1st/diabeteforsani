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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.wdf.wdf.entity.Depistage;
import org.wdf.wdf.entity.Depistages;
import org.wdf.wdf.entity.Media;
import org.wdf.wdf.repository.DepistageRepository;
import org.wdf.wdf.repository.MedaRepository;
import org.wdf.wdf.services.StorageService;
@RestController
public class DepistageController {
	

	@Autowired
	StorageService storageService;
	
	@Autowired
	MedaRepository mediaRepository;
	
	@Autowired
	DepistageRepository depistageRepository;
	
	@RequestMapping(value = "/api/depistage/save",method = RequestMethod.POST)
	public Depistage SaveDepistage(@RequestParam("files") MultipartFile[] files,
			@RequestParam("titre") String titre ,@RequestParam("description") String description,
			@RequestParam("dateDebut") String dateDebut,
			@RequestParam("dateFin") String dateFin) throws ParseException {
		
		MultipartFile[] listeFiles=files;
		List<Media> listMedia=new ArrayList<Media>();
		for(MultipartFile mf:listeFiles) {
			System.out.println(mf.getOriginalFilename());
			Media md=new Media();
			md.setChemin(mf.getOriginalFilename());
			listMedia.add(md);
		}
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Depistage d=new Depistage();
		d.setTitre(titre);
		d.setDateDebut(df.parse(dateDebut));
		d.setDateFin(df.parse(dateFin));
		d.setDescription(description);
		Depistage dep=depistageRepository.save(d);
		int compteur=0;
		Instant instant=Instant.now();
		String  dossier=""+instant.getEpochSecond();
		File path=new File("upload-dir/"+dossier);
		path.mkdirs();
		for(int i=0;i<listeFiles.length;i++){
			storageService.store(listeFiles[i],dossier);
			compteur++;
		} 
		if(compteur==listeFiles.length) {
			for(Media m:listMedia) {
				m.setDepistage(dep);
				mediaRepository.save(m);
			}
		}
		return dep;
	}

	
	@RequestMapping(value = "/api/depistage/list",method = RequestMethod.GET)
	public ResponseEntity<List<Depistages>> ListeFormation(){
		List<Depistage> listeDepistage=depistageRepository.findAll();		
		List<Media> listeMedia=new ArrayList<Media>();
		List<Depistages> listesDepistages =new ArrayList<Depistages>();
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		for(Depistage d:listeDepistage) {
		listeMedia=mediaRepository.findByDepistage(d);
		List<String> listeRessource =new ArrayList<String>();
			for(int i=0;i<listeMedia.size();i++) {
				String file = storageService.CheminFichier(listeMedia.get(i).getChemin());
				listeRessource.add(file);
			}
			Depistages de=new Depistages();
			de.setDepistage(d);
			de.setFichier(listeRessource);
			listesDepistages.add(de);
		}	
		return ResponseEntity.ok().body(listesDepistages);
	}
	
	@RequestMapping(value = "/api/depistage/delete/{id}",method = RequestMethod.DELETE)
	public String DeleteDepistage(@PathVariable long id) {
		Depistage d=new Depistage();
		d.setId(id);
		List<Media> listeMedia=mediaRepository.findByDepistage(d);
		for(Media m: listeMedia){
			storageService.DeleteFile(m.getChemin());
			mediaRepository.delete(m);
		}
		System.out.println(" bonjour le monde je suis la methode supprimer ");
		depistageRepository.delete(d);
		return "success";
	}
}
