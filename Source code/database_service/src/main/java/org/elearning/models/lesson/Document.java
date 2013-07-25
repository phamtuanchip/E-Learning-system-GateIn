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
 * 
 * @author Trung An - trung.an18@gmail.com
 *
 */
public class Document {
	public static final String PREF = "elearning_document";
	public static final String NT_NAME = "elearning:document";
	public static final String P_LESSON_ID = "elearning:lessonid";
	public static final String P_FILE_PATH = "elearning:filepath";
	
	private String id;
	private String name;
	private String lessonID;
	private String filePath;
	
	public Document(){
		setId(PREF + IdGenerator.generate());
	}
	
	public Document(String lessonID){
		this();
		setLessonID(lessonID);
	}
	
	public Document(String lessonID, String filePath){
		this(lessonID);
		setFilePath(filePath);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
		name = id;
	}
	
	public String getName(){
		return name;
	}
	
	public String getLessonID() {
		return lessonID;
	}
	
	public void setLessonID(String lessonID) {
		this.lessonID = lessonID;
	}
	
	public String getFilePath() {
		return filePath;
	}
	
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
