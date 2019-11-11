package org.wdf.wdf.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.core.io.Resource;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility =Visibility.ANY)
public class Formations implements Serializable{
	private Formation formation;
	private List<String> fichier;
}
