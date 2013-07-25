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
 */
public interface DataStorage {
	public Node getStorageHome() throws RepositoryException, Exception;
	
	// get, save and delete lessons
	public void saveLesson(GenericLesson lesson, boolean isNew) 
			throws ItemExistsException, Exception;
	public Collection<GenericLesson> getLessons() throws Exception;
	public GenericLesson getLesson(String id) throws ItemNotFoundException;
	public void removeLesson(String id) throws Exception;
	
	// get, save and delete document
	public void saveDocument(Document doc, boolean isNew) 
			throws ItemExistsException, Exception;
	public Collection<Document> getDocuments(GenericLesson lesson) throws Exception;
	public Document getDocument(String id) throws ItemNotFoundException;
	public void removeDocument(String id) throws Exception;
}
