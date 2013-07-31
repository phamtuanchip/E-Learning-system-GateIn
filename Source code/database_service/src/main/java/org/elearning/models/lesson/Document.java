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

import org.exoplatform.services.jcr.util.IdGenerator;

/**
 * Document (Học liệu) class
 * @author Trung An - trung.an18@gmail.com
 *
 */
public class Document {
	public static final String PREF = "elearning_document";
	public static final String NT_NAME = "elearning:document";
	public static final String P_LESSON_ID = "elearning:lessonid";
	public static final String P_DOC_NAME = "elearning:doc_name";
	public static final String P_FILE_PATH = "elearning:filepath";
	
	private String id;
	private String name;
	private String lessonID;
	private String filePath;
	
	/**
	 * Constructor for Document class
	 */
	public Document(){
		setId(PREF + IdGenerator.generate());
	}
	
	/**
	 * Constructor for Document class
	 * @param lessonID id of the lesson that the document belongs to
	 */
	public Document(String lessonID){
		this(lessonID, "", "");
	}
	
	/**
	 * Constructor for Document class
	 * @param lessonID id of the lesson that the document belongs to
	 * @param name name of the document
	 */
	public Document(String lessonID, String name){
		this(lessonID, name, "");		
	}

	/**
	 * Constructor for Document class
	 * @param lessonID id of the lesson that the document belongs to
	 * @param name name of the document
	 * @param filePath path to the file in database
	 */
	public Document(String lessonID, String name, String filePath){
		this();
		setLessonId(lessonID);
		setName(name);
		setFilePath(filePath);
	}
	
	/**
	 * Get document's id
	 * @return id of the document
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Set document's id
	 * @param id new id for document
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Get document's name
	 * @return document's name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * Set document's name
	 * @param name new name for the document
	 */
	public void setName(String name){
		this.name = name;
	}
	
	/**
	 * Get lesson id
	 * @return lesson's id
	 */
	public String getLessonId() {
		return lessonID;
	}
	
	/**
	 * Set lesson id
	 * @param lessonID lesson's id
	 */
	public void setLessonId(String lessonID) {
		this.lessonID = lessonID;
	}
	
	/**
	 * Get file path
	 * @return file path
	 */
	public String getFilePath() {
		return filePath;
	}
	
	/**
	 * Set file path
	 * @param filePath file path
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
