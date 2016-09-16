package com.java.doc.model;

// Generated Apr 20, 2015 2:42:16 PM by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;

import java.util.List;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * TypeSecret generated by hbm2java
 */
@Entity
@Table(name = "type_secret", catalog = "document")
public class TypeSecret implements java.io.Serializable {

	private static final long serialVersionUID = 4670755709116180075L;
	private Integer id;
	private String typeSecret;
	private String active;
	/*private List<BookReciveOut> bookReciveOuts;
	private List<BookSendOut> bookSendOuts;*/
	
	public TypeSecret() {
	}

	public TypeSecret(String typeSecret, String active) {
		this.typeSecret = typeSecret;
		this.active = active;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "TYPE_SECRET", length = 100)
	public String getTypeSecret() {
		return this.typeSecret;
	}

	public void setTypeSecret(String typeSecret) {
		this.typeSecret = typeSecret;
	}

	@Column(name = "ACTIVE", length = 1)
	public String getActive() {
		return this.active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "typeSecret")
	public List<BookReciveOut> getBookReciveOuts() {
		return bookReciveOuts;
	}

	public void setBookReciveOuts(List<BookReciveOut> bookReciveOuts) {
		this.bookReciveOuts = bookReciveOuts;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "typeSecret")
	public List<BookSendOut> getBookSendOuts() {
		return bookSendOuts;
	}

	public void setBookSendOuts(List<BookSendOut> bookSendOuts) {
		this.bookSendOuts = bookSendOuts;
	}*/
	
}
