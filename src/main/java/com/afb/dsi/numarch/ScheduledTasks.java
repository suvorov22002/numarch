package com.afb.dsi.numarch;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.afb.dsi.numarch.dtos.DocumentsDTO;
import com.afb.dsi.numarch.dtos.NumArchUtils;
import com.afb.dsi.numarch.entities.Documents;
import com.afb.dsi.numarch.entities.Proprietes;
import com.afb.dsi.numarch.services.IDocumentsService;
import com.afb.dsi.numarch.services.IProprietesService;


//@Component
public class ScheduledTasks {
	
	final static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	private List<Documents> ldocx;
	private DocumentsDTO documentDto;
	private File file;
			
	@Autowired
	IDocumentsService documentservice;
	
	@Autowired
	private IProprietesService proprieteservice;
	
	//@Scheduled(fixedDelayString = "${my.property.fixed.delay.seconds:60}000", initialDelay = 10000)
	public void scheduleTaskFixedDelay(){
		logger.info("scheduleTaskFixedDelay executed at {}", LocalDateTime.now());
		
		/** 1- Recherche des documents avec traiter à False. Limiter à 10 */
		documentDto = new DocumentsDTO();
		documentDto.setTraiter(false);
		ldocx = new ArrayList<Documents>();
		ldocx = documentservice.findAllReportDocs(Boolean.FALSE);
		
		logger.info("Reported list size: "+ldocx.size());
		
		for(Documents d : ldocx) {
			//System.out.println("Reported: "+d.getProprietes());
			List<Proprietes> docPropriete = new ArrayList<Proprietes>();
			docPropriete = proprieteservice.findByDocument(d.getId());
			d.setDocPropriete(docPropriete);
			
			DocumentsDTO resp_doc =  NumArchUtils.mapDocumentsToDocumentsDTO(d);
			
			try {
				if(resp_doc.getBase64Str() != null || !resp_doc.getBase64Str().isEmpty()) {
					file = new File(AppInitializator.params.get("DEPOT_FOLDER").getValeur()+resp_doc.getBase64Str());
//					file.setExecutable(Boolean.TRUE);
//					file.setWritable(Boolean.TRUE);
					
//					RandomAccessFile _file = new RandomAccessFile("C://numarch/uploadsR/"+resp_doc.getBase64Str(), "rw");
					
//					
//			        FileChannel fileChannel = _file.getChannel();
//			        fileChannel.truncate(0);
//			        
//			        
//			        FileLock lock = fileChannel.tryLock();
//			        lock.release();
			        
			      
			        
					resp_doc.setTraiter(Boolean.TRUE);
					
					logger.info("Document ref: "+resp_doc.getReference());
					Documents docx = NumArchUtils.mapDocumentsDTOToDocuments(resp_doc);
					
					
					if(file.exists()) {
						
						logger.info("file.canWrite: "+file.getParentFile().canWrite());
						
						Documents dx = documentservice.sendToAcs(docx, file);
						if(dx != null) {
							
							 logger.info("file.canread(): "+file.canExecute());
							documentservice.updateDocuments(dx);
							if(file.delete())
					        {
					            logger.info("File deleted successfully");
					          //  documentservice.updateDocuments(dx);
					        }
					        else
					        {
					            logger.info("Failed to delete the file");
					            File f = new File(file.getAbsolutePath()+"_temp");
						        file.renameTo(f);
					        }
						}
						else {
							 logger.info("File transfer error");
						}
					}
					else {
						logger.info("File not exist");
					}
				}
			}
			catch(Exception e) {
				e.printStackTrace();
				//file.c
			}
			
		}
		
	}
}
