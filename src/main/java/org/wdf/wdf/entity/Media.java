package org.wdf.wdf.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Media implements Serializable{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idMedia;
	private String chemin;
	@ManyToOne()
	private Formation formation;
	@ManyToOne()
	private Gallerie gallerie;
	@ManyToOne()
	private Depistage depistage;
	@ManyToOne()
	private Sensibilisation sensibilisation;
	@ManyToOne()
	private Soin soin;
	
}
