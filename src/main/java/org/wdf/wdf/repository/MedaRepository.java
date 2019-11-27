package org.wdf.wdf.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wdf.wdf.entity.Depistage;
import org.wdf.wdf.entity.Formation;
import org.wdf.wdf.entity.Gallerie;
import org.wdf.wdf.entity.Media;
import org.wdf.wdf.entity.Sensibilisation;
import org.wdf.wdf.entity.Soin;
public interface MedaRepository extends JpaRepository<Media,Long>{

	public List<Media> findByFormation(Formation formation);
	
	public List<Media> findBySensibilisation(Sensibilisation sensibilisation);
	
	public List<Media> findBySoin(Soin soin);
	
	public List<Media> findByGallerie(Gallerie gallerie);
	
	
	public List<Media> findByDepistage(Depistage depistage);
}
