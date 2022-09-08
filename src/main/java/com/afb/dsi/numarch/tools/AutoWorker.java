/**
 * 
 */
package com.afb.dsi.numarch.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.afb.dsi.numarch.dtos.DocumentsDTO;
import com.afb.dsi.numarch.dtos.NumArchUtils;
import com.afb.dsi.numarch.entities.Documents;
import com.afb.dsi.numarch.entities.Params;
import com.afb.dsi.numarch.entities.Proprietes;
import com.afb.dsi.numarch.services.IDocumentsService;
import com.afb.dsi.numarch.services.IParamsService;
import com.afb.dsi.numarch.services.IProprietesService;

/**
 * @author rodrigue_toukam
 *
 */
public class AutoWorker {
	
	private static TimerTask task;
	private static java.util.Timer timer;
	private static List<Params> parameter;
	private static Map<String, Params> params = new HashMap<String, Params>(); 
	private static int DELAY = 5;
	private final static int DELAY_SEC = 60;
	final static Logger logger = LoggerFactory.getLogger(AutoWorker.class);
	private static List<Documents> ldocx;
	private static DocumentsDTO documentDto;
	private static File file;
			
	@Autowired
	private static IDocumentsService documentservice;
	
	@Autowired
	private static IProprietesService proprieteservice;
	
	@Autowired
	private static IParamsService paramservice;
	
	
	public static void initChecking(){
		
		try{
			
			if(task != null) task.cancel();
			if(timer != null) timer.cancel();
			
			parameter = paramservice.getAllParams();
			for(Params p : parameter) {
				params.put(p.getCode(), p);
			}
				
			task = new TimerTask(){
				@Override
				public void run(){
					
					try {
						if("ON".equals(params.get("ROBOT").getValeur())){						
							logger.info("EXECUTE ROBOT");
							process();
						}
						else{
							logger.info("CANCEL ROBOT");
							this.cancel();
						}

					}catch(Exception e){
						//e.printStackTrace();
					}
				}	
			};
			
			
			if(params.containsKey("ReportSend")){
				
				if (!params.get("ReportSend").getValeur().isEmpty()) {
					DELAY = Integer.parseInt(params.get("ReportSend").getValeur());
				}
			}
			
			timer = new java.util.Timer(true);
			timer.schedule(task, DateUtils.addMinutes(new Date(), 5) , DELAY*DELAY_SEC*1000);	

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void cancelChecking(){
		try{
			
			if(task != null) task.cancel();
			if(timer != null) timer.cancel();
						
			task = null;
			timer = null;
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void process(){
		
		documentDto = new DocumentsDTO();
		documentDto.setTraiter(false);
		ldocx = new ArrayList<Documents>();
		ldocx = documentservice.findAllReportDocs(Boolean.FALSE);
		
		logger.info("Reported list size: "+ldocx.size());
		
		for(Documents d : ldocx) {
			
			List<Proprietes> docPropriete = new ArrayList<Proprietes>();
			docPropriete = proprieteservice.findByDocument(d.getId());
			d.setDocPropriete(docPropriete);
			
			DocumentsDTO resp_doc =  NumArchUtils.mapDocumentsToDocumentsDTO(d);
			
			try {
				if(resp_doc.getBase64Str() != null || !resp_doc.getBase64Str().isEmpty()) {
					
					file = new File(params.get("DEPOT_FOLDER").getValeur()+resp_doc.getBase64Str());

			        
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
			}
			
		}
		
		
	}
}
