package org.elearning.tests;

import javax.jcr.ItemExistsException;
import javax.jcr.RepositoryException;

import org.elearning.models.lesson.Document;
import org.elearning.models.lesson.GenericLesson;
import org.elearning.storage.DataStorage;
import org.elearning.storage.impl.JcrDataStorage;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.config.RepositoryConfigurationException;

public class LessonStorageTest extends GenericServiceTest{
	private RepositoryService repoService;
	private DataStorage storage;
	
	public void setUp() throws Exception{
		repoService = getService(RepositoryService.class);		
		storage = getService(JcrDataStorage.class);
	}
	
	public void testInitService() throws RepositoryException, RepositoryConfigurationException{
		assertNotNull(repoService);
		assertEquals(repoService.getDefaultRepository().getConfiguration().getName(), 
					"repository");
	    assertEquals(repoService.getDefaultRepository().getConfiguration().getDefaultWorkspaceName(), 
	    			"portal-test");
		assertNotNull(storage);
	}
	
	public void testStorageHome() throws RepositoryException, Exception{
		assertNotNull(storage.getStorageHome());
		assertEquals(org.elearning.Util.E_LEARNING_APP, storage.getStorageHome().getName());
	}
	
	public void testLessonStorage() throws Exception{
		GenericLesson lesson = new GenericLesson();
		lesson.setTitle("test lesson");
		lesson.setSubject("Tin hoc");
		lesson.setContent("Khong co gi trong content ca");
		
		// test saving new lesson
		storage.saveLesson(lesson, true);
		assertNotNull(storage.getLessons());
		assertEquals(1, storage.getLessons().size());
		
		//test saving existing lesson
		lesson.setContent("lan nay thi co 1 it chu trong content");
		storage.saveLesson(lesson, false);
		assertEquals("lan nay thi co 1 it chu trong content", 
				storage.getLesson(lesson.getId()).getContent());
		
		lesson.setTitle("Title moi");
		try{
			storage.saveLesson(lesson, true);
		}
		catch(ItemExistsException e){
			logger.info("Lesson " + lesson.getId() + " already exists");
			assertTrue(true);
		}
		
		// test removing lesson
		storage.removeLesson(lesson.getId());
		assertEquals(0, storage.getLessons().size());
	}
	
	public void testDocumentStorage() throws Exception{
		// Setting up pseudo lesson
		GenericLesson lesson = new GenericLesson();
		lesson.setTitle("test lesson");
		lesson.setSubject("Tin hoc");
		lesson.setContent("Khong co gi trong content ca");
		storage.saveLesson(lesson, true);
		
		Document doc = new Document();
		doc.setName("abc.pdf");
		doc.setLessonId(lesson.getId());
		doc.setFilePath("/files/lessonid/abc.pdf");
		
		// test saving document
		storage.saveDocument(doc, true);
		assertNotNull(storage.getDocuments(lesson.getId()));
		assertEquals(1, storage.getDocuments(lesson.getId()).size());
		
		//test saving existing document
		doc.setName("new_name.pdf");
		storage.saveDocument(doc, false);
		assertEquals("new_name.pdf", 
				storage.getDocument(doc.getId()).getName());
		
		doc.setName("another_new_name.pdf");
		try{
			storage.saveDocument(doc, true);
		}
		catch(ItemExistsException e){
			logger.info("Document " + doc.getId() + " already exists");
			assertTrue(true);
		}
		
		// testing removeLesson
		storage.removeDocument(doc.getId());
		assertEquals(0, storage.getDocuments(lesson.getId()).size());
	}
}
