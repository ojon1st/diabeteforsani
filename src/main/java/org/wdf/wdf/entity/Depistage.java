package org.wdf.wdf.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Depistage implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String titre;
	@Column(length=1000)
	private String description;
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date dateDebut;
	private long resultat;
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date dateFin;
	private boolean statut;
	
}
