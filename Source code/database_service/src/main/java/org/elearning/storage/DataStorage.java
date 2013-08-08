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
package org.elearning.storage;

import java.util.Collection;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.elearning.models.lesson.*;

/**
 * Api for DataStorage
 * @author Trung An - trung.an18@gmail.com
 */
public interface DataStorage {
	/**
	 * Get the root of the application's repository
	 * @return root node of the storage repository
	 * @throws RepositoryException
	 * @throws Exception
	 */
	public Node getStorageHome() throws RepositoryException, Exception;
	
	// get, save and delete lessons
	/**
	 * Save lesson to repository
	 * @param lesson lesson that need to be stored
	 * @param isNew indicate if the lesson is new or already exist
	 * @throws ItemExistsException
	 * @throws Exception
	 */
	public void saveLesson(GenericLesson lesson, boolean isNew) 
			throws ItemExistsException, Exception;
	
	/**
	 * Get all lessons in repository
	 * @return All lessons in repository
	 * @throws Exception
	 */
	public Collection<GenericLesson> getLessons() throws Exception;
	
	/**
	 * Get a specific lesson by id
	 * @param id lesson's id
	 * @return lesson contained the given id
	 * @throws ItemNotFoundException
	 */
	public GenericLesson getLesson(String id) throws ItemNotFoundException;
	
	/**
	 * Remove a specific lesson
	 * @param id lesson's id
	 * @throws Exception
	 */
	public void removeLesson(String id) throws Exception;
	
	// get, save and delete document
	/**
	 * Save document to repository
	 * @param doc document to be stored
	 * @param isNew indicate if creating a new document or modifying an existing one
	 * @throws ItemExistsException
	 * @throws Exception
	 */
	public void saveDocument(Document doc, boolean isNew) 
			throws ItemExistsException, Exception;
	
	/**
	 * Get all documents that belong to a particular lesson
	 * @param lessonID id of the lesson 
	 * @return a collection of documents in the lesson 
	 * @throws Exception
	 */
	public Collection<Document> getDocuments(String lessonID) throws Exception;
	
	/**
	 * Get a document by id
	 * @param id document's id
	 * @return document with existing id
	 * @throws ItemNotFoundException
	 */
	public Document getDocument(String id) throws ItemNotFoundException;
	
	/**
	 * Remove a document by id
	 * @param id document's id
	 * @throws Exception
	 */
	public void removeDocument(String id) throws Exception;
}
