package com.java.doc.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sections", catalog = "document")
public class Sections implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer groupId;
	private String sectionName;
	
	public Sections(){}
	public Sections(Integer id, Integer groupId, String sectionName){
		this.setId(id);
		this.setGroupId(groupId);
		this.setSectionName(sectionName);
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)  
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	@Column(name = "group_id")
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
	@Column(name = "section_name")
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
}
