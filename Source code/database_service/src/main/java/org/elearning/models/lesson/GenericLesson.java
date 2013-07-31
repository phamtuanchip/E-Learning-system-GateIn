/****************************************************************************** 
 * E-Learning GateIn System is e-learning system based on GateIn Portal.
 * Copyright (C) 2013 Nguyen Khanh Thinh, Nguyen Dinh Nien, Nguyen Manh Cuong, 
 * To Ngoc Linh, Tran Hung Quan and contributors.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.

 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 51 Franklin St, Fifth Floor,
 * Boston, MA  02110-1301, USA.
 * 
**************************************************************************************/
package org.elearning.models.lesson;

import java.util.ArrayList;
import java.util.Collection;

import org.exoplatform.services.jcr.util.IdGenerator;
/**
 * Generic Lesson class
 * @author Trung An - trung.an18@gmail.com
 *
 */
public class GenericLesson {
	public static final String PREF = "elearning_lesson";
	// nt_name = node type name
	public static final String NT_NAME = "elearning:lesson";
	public static final String P_TITLE = "elearning:title";
	public static final String P_SUBJECT = "elearning:subject";
	public static final String P_CONTENT = "elearning:content";
	public static final String P_DOCS = "elearning:documentsID";
	
	private String id;
	private String title; 
	private String subject;
	private String content;
	// list of all attached documents that belongs to the lesson
	private ArrayList<String> documents;
	
	/**
	 * Constructor for GenericLesson. It uses 
	 * GateIn to regenerate an id for the lesson
	 */
	public GenericLesson(){
		// set lesson id by ExoPlatform's api
		setId(PREF + IdGenerator.generate());
		documents = new ArrayList<String>();
	}
	
	/**
	 * Get the lesson's id
	 * @return id of the current lesson
	 */
	public String getId(){
		return id;
	}
	
	/**
	 * Set the lesson's id
	 * @param id Lesson ID
	 */
	public void setId(String id){
		this.id = id;
	}
	/**
	 * Get lesson's title
	 * @return title of the current lesson
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Set new title for the lesson
	 * @param title new title of the lesson
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Get lesson's subject
	 * @return lesson's subject
	 */
	public String getSubject() {
		return subject;
	}

	/**
	 * Assign the lesson to a subject 
	 * @param subject the subject
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * Get lesson's content
	 * @return lesson's content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Set new content for the lesson
	 * @param content new lesson's content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public boolean addDocuments(Collection<String> docs){
		if(docs == null)
			return false;
		return documents.addAll(docs);
	}
	/**
	 * Attach new document to the lesson
	 * @param doc attached document
	 * @return true if the document is added successfully; otherwise, 
	 * return false if document is null.
	 */
	public boolean addDocument(String docID){
		if(docID == null || docID == "")
			return false;
		return documents.add(docID);
	}
	
	/**
	 * Unattached a document from the lesson
	 * @param doc the document that needed to be removed
	 * @return true if the document is removed successfully; false 
	 * if the parameter is null or it is not in the list
	 */
	public boolean removeDocument(String docID){
		if(docID == null || docID == "")
			return false;
		return documents.remove(docID);
	}
	
	/**
	 * Get all documents attached with the lesson
	 * @return list of attached documents
	 */
	public ArrayList<String> getDocuments(){
		return documents;
	}
}
