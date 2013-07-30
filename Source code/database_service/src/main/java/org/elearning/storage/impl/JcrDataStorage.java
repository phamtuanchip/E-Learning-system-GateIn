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
package org.elearning.storage.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.elearning.storage.impl.Util;

import javax.jcr.ItemExistsException;
import javax.jcr.ItemNotFoundException;
import javax.jcr.Node;
import javax.jcr.NodeIterator;
import javax.jcr.PathNotFoundException;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import javax.jcr.query.Query;
import javax.jcr.query.QueryManager;

import org.elearning.models.lesson.*;
import org.elearning.storage.DataStorage;
import org.exoplatform.container.ExoContainer;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.ext.hierarchy.NodeHierarchyCreator;
import org.exoplatform.services.jcr.ext.app.SessionProviderService;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

public class JcrDataStorage implements DataStorage{
	private NodeHierarchyCreator nodeHierarchyCreator;
	private RepositoryService repoService;
	private SessionProviderService sessionProviderService;
	
	private static final Log logger = ExoLogger.getLogger("org.elearning.storage.JcrDataStorage");
	
	public JcrDataStorage(NodeHierarchyCreator nodeHierarchyCreator, 
								RepositoryService repoService){
		this.nodeHierarchyCreator = nodeHierarchyCreator;
		ExoContainer container = ExoContainerContext.getCurrentContainer();
		this.repoService = repoService;
		sessionProviderService = (SessionProviderService)
				container.getComponentInstanceOfType(SessionProviderService.class);
	}
	
	public Node getStorageHome() throws RepositoryException, Exception {
		SessionProvider sProvider = createSessionProvider();
		Node pubApp = nodeHierarchyCreator.getPublicApplicationNode(sProvider);
		
		try{
			return pubApp.getNode(Util.E_LEARNING_APP);
		}
		catch(PathNotFoundException e){
			Node eLearningApp = pubApp.addNode(Util.E_LEARNING_APP);
			eLearningApp.getSession().save();
			return eLearningApp;
		}
	}

	public void saveLesson(GenericLesson lesson, boolean isNew)
			throws ItemExistsException, Exception {
		Node lessonStorage = getLessonStorage();
		Node lessonNode;
		if(isNew){
			//TODO: title collision
			if(isExist(GenericLesson.NT_NAME, GenericLesson.P_TITLE, lesson.getTitle()))
				throw new ItemExistsException();
			try{
				lessonNode = setLessonProperty(lesson,
						lessonStorage.addNode(lesson.getId(), GenericLesson.NT_NAME));
				// adding mix:referenable is essential since it
				// will create a jcr:uuid for each node
				lessonNode.addMixin(Util.MIX_REFERENCEABLE);
				lessonNode.getSession().save();
			}
			catch(Exception e){
				e.printStackTrace();
				logger.info(e.getMessage());
				throw e;
			}
		}
		else{
			lessonNode = setLessonProperty(lesson, lessonStorage.getNode(lesson.getId()));
			lessonNode.getSession().save();
		}
	}

	public Collection<GenericLesson> getLessons() throws Exception {
		try{
			Node lessonStorage = getLessonStorage();
			Collection<GenericLesson> coll = new ArrayList<GenericLesson>();
			
			NodeIterator nodeIter = lessonStorage.getNodes();
			while(nodeIter.hasNext()){
				Node n = nodeIter.nextNode();
				coll.add(getLessonProperty(n));
			}
			
			return coll;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.info(e.getMessage());
			return null;
		}
	}

	public GenericLesson getLesson(String id) throws ItemNotFoundException {
		try {
			Node lessonStorage = getLessonStorage();
			return getLessonProperty(lessonStorage.getNode(id));
		} catch (PathNotFoundException e) {
			throw new ItemNotFoundException();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return null;
		}
	}

	public void removeLesson(String id) throws Exception {
		try{
			Node lessonStorage = getLessonStorage();
			lessonStorage.getNode(id).remove();
			lessonStorage.getSession().save();
		}
		catch(Exception e){
			logger.info(e.getMessage());
			throw e;
		}
	}
	
	public void saveDocument(Document doc, boolean isNew) 
			throws ItemExistsException, Exception{
		Node docStorage = getDocumentStorage();
		Node docNode;
		if(isNew){
			//TODO: implement search by ID
			if(isExist(Document.NT_NAME, Document.P_FILE_PATH, doc.getFilePath()))
				throw new ItemExistsException();
			try{
				docNode = setDocumentProperty(doc,
						docStorage.addNode(Util.E_LEARNING_DOC, Util.NT_UNSTRUCTURED));
				docNode.addMixin(Util.MIX_REFERENCEABLE);
				docNode.getSession().save();
			}
			catch(Exception e){
				e.printStackTrace();
				logger.info(e.getMessage());
				throw e;
			}
		}
		else{
			docNode = setDocumentProperty(doc, docStorage.getNode(doc.getId()));
			docNode.getSession().save();
		}
	}
	
	public Collection<Document> getDocuments(GenericLesson lesson) 
			throws Exception{
		Collection<Document> coll = new ArrayList<Document>();
		Node docStorage = getDocumentStorage();
		try{
			for(String id : lesson.getDocuments())
				coll.add(getDocumentProperty(docStorage.getNode(id)));
			return coll;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.info(e.getMessage());
			return null;
		}
	}
	
	public Document getDocument(String id) throws ItemNotFoundException{
		try {
			Node docStorage = getDocumentStorage();
			return getDocumentProperty(docStorage.getNode(id));
		} catch (PathNotFoundException e) {
			throw new ItemNotFoundException();
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(e.getMessage());
			return null;
		}
	}
	
	public void removeDocument(String id) throws Exception{
		try{
			Node docStorage = getDocumentStorage();
			docStorage.getNode(id).remove();
			docStorage.getSession().save();
		}
		catch(Exception e){
			logger.info(e.getMessage());
			throw e;
		}
	}
	
	/******** Private supporting methods ********/
	private SessionProvider createSessionProvider() {
		SessionProvider provider = 
				sessionProviderService.getSessionProvider(null);
		if(provider == null)
			return sessionProviderService.getSystemSessionProvider(null);
		
		return provider;
	}
	
	private Node setLessonProperty(GenericLesson lesson, Node node){
		try{
			node.setProperty(GenericLesson.P_TITLE, lesson.getTitle());
			node.setProperty(GenericLesson.P_SUBJECT, lesson.getSubject());
			node.setProperty(GenericLesson.P_CONTENT, lesson.getContent());
			node.setProperty(GenericLesson.P_DOCS, lesson.getDocuments().toArray(new String[]{}));
		}
		catch(Exception e){
			logger.info(e.getMessage());
			return null;
		}
		return node;
	}
	
	/**
	 * 
	 * @return
	 * @throws RepositoryException
	 * @throws Exception
	 */
	private Node getLessonStorage() throws RepositoryException, Exception{
		Node appStorage = getStorageHome();
		try{
			return appStorage.getNode(Util.E_LEARNING_LESSON);
		}
		catch(PathNotFoundException e){
			Node lessonStorage = appStorage.addNode(Util.E_LEARNING_APP, Util.NT_UNSTRUCTURED);
			appStorage.getSession().save();
			return lessonStorage;
		}
	}
	
	/**
	 * 
	 * @param lessonNode
	 * @return
	 */
	private GenericLesson getLessonProperty(Node lessonNode){
		GenericLesson lesson = new GenericLesson();
		try{
			// the name of a node = the id of the item
			// in this case the id of the lesson
			lesson.setId(lessonNode.getName());
			if(lessonNode.hasProperty(GenericLesson.P_TITLE))
				lesson.setTitle(lessonNode.getProperty(GenericLesson.P_TITLE).getString());
			if(lessonNode.hasProperty(GenericLesson.P_SUBJECT))
				lesson.setSubject(lessonNode.getProperty(GenericLesson.P_SUBJECT).getString());
			if(lessonNode.hasProperty(GenericLesson.P_CONTENT))
				lesson.setContent(lessonNode.getProperty(GenericLesson.P_CONTENT).getString());
			
			if(lessonNode.hasProperty(GenericLesson.P_DOCS)){
				Value[] docValues = lessonNode.getProperty(GenericLesson.P_DOCS).getValues();
				for(Value v : docValues)
					lesson.addDocument(v.toString());
			}
		}
		catch(Exception e){
			logger.info(e.getMessage());
			return null;
		}
		return lesson;
	}
	
	private Node setDocumentProperty(Document doc, Node node){
		try{
			node.setProperty(Document.P_LESSON_ID, doc.getLessonID());
			node.setProperty(Document.P_FILE_PATH, doc.getFilePath());
		}
		catch(Exception e){
			logger.info(e.getMessage());
			return null;
		}
		return node;
	}
	
	private Node getDocumentStorage() throws RepositoryException, Exception{
		Node appStorage = getStorageHome();
		try{
			return appStorage.getNode(Util.E_LEARNING_DOC);
		}
		catch(PathNotFoundException e){
			appStorage.addNode(Util.E_LEARNING_DOC, Util.NT_UNSTRUCTURED);
			appStorage.getSession().save();
			return appStorage.getNode(Util.E_LEARNING_DOC);
		}
	}
	
	private Document getDocumentProperty(Node docNode){
		Document doc = new Document();
		try{
			doc.setId(docNode.getName());
			if(docNode.hasProperty(Document.P_LESSON_ID))
				doc.setLessonID(docNode.getProperty(Document.P_LESSON_ID).toString());
			if(docNode.hasProperty(Document.P_FILE_PATH))
				doc.setFilePath(docNode.getProperty(Document.P_FILE_PATH).toString());
			return doc;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.info(e.getMessage());
			return null;
		}
	}
	
	/**
	 * 
	 * @param nodeTypeName
	 * @param propertyName
	 * @param value
	 * @return
	 */
	private boolean isExist(String nodeTypeName, String propertyName, String value){
		try {
			QueryManager qm = getStorageHome().getSession().getWorkspace().getQueryManager();
			String q = "SELECT " + propertyName + " FROM " + nodeTypeName + 
						" WHERE " + propertyName + " = '" + value + "'";
			Query query = qm.createQuery(q, Query.SQL);
			return query.execute().getRows().getSize() != 0;
		}
		catch(Exception e){
			e.printStackTrace();
			logger.info(e.getMessage());
			return false;
		}
	}
}
