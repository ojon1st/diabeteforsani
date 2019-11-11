package org.wdf.wdf.controller;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.wdf.wdf.entity.Formation;
import org.wdf.wdf.entity.Formations;
import org.wdf.wdf.entity.Media;
import org.wdf.wdf.repository.FormationRepository;
import org.wdf.wdf.repository.MedaRepository;
import org.wdf.wdf.services.StorageService;

@RestController
public class FormationController {
	
	@Autowired
	private FormationRepository formationRepository;
	
	@Autowired
	StorageService storageService;
	
	@Autowired
	MedaRepository mediaRepository ;
	
	@RequestMapping(value = "/api/formation/save")
	public Formation SaveFormation(@RequestParam("files") MultipartFile[] files,
			@RequestParam("titre") String titre ,@RequestParam("description") String description,
			@RequestParam("dateDebut") String dateDebut,
			@RequestParam("dateFin") String dateFin,
			HttpServletRequest httpServletRequest) throws ParseException {
		System.out.println("\n\n bonjour le monde je suis appeler \n\n");
		MultipartFile[] listeFiles=files;
		List<Media> listMedia=new ArrayList<Media>();
		for(MultipartFile mf:listeFiles){
			Media md=new Media();
			md.setChemin(mf.getOriginalFilename());
			listMedia.add(md);
		}
		
		DateFormat df=new SimpleDateFormat("yyyy-MM-dd");
		Formation fmt=new Formation();
		fmt.setTitre(titre);
		fmt.setDateDebut(df.parse(dateDebut));
		fmt.setDateFin(df.parse(dateFin)); 
		fmt.setDescription(description);
		Formation format=formationRepository.save(fmt);
		int compteur=0;
		Instant instant=Instant.now();
		String  dossier=""+instant.getEpochSecond();
		System.out.println("");
		for(int i=0;i<listeFiles.length;i++){
			System.out.println( " fichier : "+listeFiles[i].getOriginalFilename().toString()+ " dossier : "+dossier);
			storageService.store(listeFiles[i],dossier);
			compteur++;
		} 
		if(compteur==listeFiles.length) {
			for(Media m:listMedia) {
				m.setFormation(format);
				m.setChemin(dossier+"/"+m.getChemin());
				mediaRepository.save(m);
			}
		}
		return format;
	}
	

	@RequestMapping(value = "/api/formation/list")
	public ResponseEntity<List<Formations>> ListeFormation(){
		List<Formation> listeFormation=formationRepository.findAll();		
		List<Media> listeMedia=new ArrayList<Media>();
		List<Formations> listesFormations=new ArrayList<Formations>();
		for(Formation f:listeFormation) {
		listeMedia=mediaRepository.findByFormation(f);
		List<String> listeRessource =new ArrayList<String>();
			for(int i=0;i<listeMedia.size();i++) {
				String file = storageService.CheminFichier(listeMedia.get(i).getChemin());
				listeRessource.add(file);
			}
			Formations fo=new Formations();
			fo.setFormation(f);
			fo.setFichier(listeRessource);	
			listesFormations.add(fo);
		}
		return ResponseEntity.ok().body(listesFormations);
	}
	
	@RequestMapping(value = "/api/formation/delete/{id}",method = RequestMethod.DELETE)
	public void DeleteFormation(@PathVariable long id){
		Formation f=new Formation();
		f.setId(id);
		List<Media> listeMedia=mediaRepository.findByFormation(f);
		for(Media m: listeMedia){
			storageService.DeleteFile(m.getChemin());
			mediaRepository.delete(m);
		}
		this.formationRepository.deleteById(id);
	}
}
