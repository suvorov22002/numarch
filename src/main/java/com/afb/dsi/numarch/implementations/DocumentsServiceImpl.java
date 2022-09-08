package com.afb.dsi.numarch.implementations;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.afb.dsi.numarch.AppInitializator;
import com.afb.dsi.numarch.ScheduledTasks;
import com.afb.dsi.numarch.dtos.DocumentsDTO;
import com.afb.dsi.numarch.dtos.NumArchUtils;
import com.afb.dsi.numarch.entities.Documents;
import com.afb.dsi.numarch.entities.Proprietes;
import com.afb.dsi.numarch.repository.DocumentsRepository;
import com.afb.dsi.numarch.repository.PropertiesRepository;
import com.afb.dsi.numarch.repository.ProprietesRepository;
import com.afb.dsi.numarch.services.IDocumentsService;
import com.afb.dsi.numarch.services.ITypeDocumentsService;

@Service("documentsservice")
@Transactional
public class DocumentsServiceImpl implements IDocumentsService {
	
	@Autowired
	private DocumentsRepository documentsRepository;
	
	@Autowired
	private ProprietesRepository proprieteRepository;
	
	@Autowired
	private PropertiesRepository propertiesRepository;
	
	@Autowired
	private ITypeDocumentsService typedocservice;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	final static Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
	
	
	@Override
	public Documents saveDocuments(DocumentsDTO documentsDTO) {
		
		String referenceDocument = "";
//		referenceDocument = referenceDocument + NumArchUtils.now();
		referenceDocument = documentsDTO.getReference();
//		System.out.println("referenceDocument "+referenceDocument);
		Documents document = NumArchUtils.mapDocumentsDTOToDocuments(documentsDTO);
		
		Documents _document = documentsRepository.findByReference(referenceDocument);
		if(_document == null) {

			String pro = document.getProprietes();
			document = documentsRepository.save(document);
			
			if(StringUtils.isNotBlank(pro)) {
				String[] p = pro.split("/");
				for(String propriete : p) {
					logger.info("propriete: ", propriete);
					String[] pr = propriete.split(";");
					try {
						if(pr.length > 1) {
							Proprietes props = new Proprietes(pr[0], pr[1], document);
							proprieteRepository.save(props);
						}
						
					}
					catch(Exception e) {
						logger.error(e.toString());
					}
					
				}
			}
		}
		
		
		return document;
	}

	@Override
	public List<Documents> saveListDocuments(List<Documents> documents) {
		return documentsRepository.saveAll(documents);
	}

	@Override
	public Documents updateDocuments(Documents documents) {
		return documentsRepository.saveAndFlush(documents);
	}

	@Override
	public void deleteDocuments(Long id) {
		documentsRepository.deleteById(id);	
	}

	@Override
	public Optional<Documents> findById(Long id) {
		return documentsRepository.findById(id);
	}

	@Override
	public List<Documents> getAllDocuments() {
		return documentsRepository.findAll();
	}
	
	@Override
	public List<Documents> getAllDocument() {
		return documentsRepository.findAlls();
	}

	@Override
	public boolean checkIfIdexists(Long id) {
		return documentsRepository.existsById(id);
	}

	@Override
	public List<Documents> findByDocuments(Long documentsId) {
		return documentsRepository.findByDocuments(documentsId);
	}
	
	@Override
	public Documents findByName(String name) {
		return documentsRepository.findByName(name);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Documents> findDocuments(DocumentsDTO documentsDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Documents> cq = cb.createQuery(Documents.class);

		Root<Documents> documents = cq.from(Documents.class);
		List<Predicate> predicates = new ArrayList<>();

		try {
			if (documentsDTO.getTypeDocument() != null) {
				predicates.add(cb.equal(documents.get("id"), documentsDTO.getTypeDocument().getId()));
			}
			
			List<Order> orderList = new ArrayList();
			
			cq.where(predicates.toArray(new Predicate[0]));
			
			orderList.add(cb.desc(documents.get("dateEnvoi")));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entityManager.createQuery(cq).getResultList();
	
	}

	@Override
	public Documents sendToAcs(Documents docx, File file) {
		
		JSONObject data = new JSONObject();
		JSONObject metadata = new JSONObject();
		String serverUrl;
		String serverCrUrl; // Url creation doddier
		RestTemplate restTemplate;
		String year = "",month = "";
	
		//serverUrl  = "http://192.168.11.36:8889/alfresco-contents/save-aps-content";
		serverUrl = AppInitializator.params.get("urlAcs").getValeur();
		serverCrUrl = AppInitializator.params.get("urlCrAcs").getValeur();
		
		String default_aspect = "cm:titled;afbm:internalDoc;afbm:transaction;afbm:unit;afbm:nameComponent";
		Map<String, String> propriete = new HashMap<String, String>();
		try {                                                                                                                                                                                              
			// Recuperation de toutes les proprites et valeur du document
			String proprietes = docx.getProprietes();
			String[] _proprie = proprietes.split("/");
			logger.info("_proprie: "+proprietes);
		 if (_proprie.length > 6) {
			for(String p : _proprie) {
				propriete.put(p.split(";")[0], p.split(";")[1]);  //propriete + valeur
				
				if (p.split(";")[0].equals("afbm:trxDate") || p.split(";")[0].equals("afbm:prodDate")) {
					
					try {
						String dd = p.split(";")[1];
						logger.info("convDate: "+dd);
						Date date;
						if (dd.length() == 8) {
							String convDate = dd.substring(0,2) + '/' + dd.substring(2,4) + '/' + dd.substring(4,8);
							//logger.info("convDate: "+convDate);
							year = dd.substring(4,8);
							month = dd.substring(2,4);
							DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
							date = format.parse(convDate);
						}
						else {
							date = new Date(dd);
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(date);
							year = ""+calendar.get(Calendar.YEAR);
							month = ""+calendar.get(Calendar.MONTH);
							logger.info("convDateTest: "+year+" --- "+month);
						}
						
						metadata.put(p.split(";")[0], date);
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}
				else if (p.split(";")[0].equals("afbm:trxAmount")) {
					try {
						metadata.put(p.split(";")[0], Double.parseDouble(p.split(";")[1]));
					}
					catch( Exception e) {
						e.printStackTrace();
						metadata.put(p.split(";")[0], 0);
					}
					
				}
				else if (p.split(";")[0].equals("cm:name")) {
					metadata.put("afbm:nameElt1", p.split(";")[1]);
				}
				else if (p.split(";")[0].equals("cm:content")) {
					continue;
				}
				else {
					metadata.put(p.split(";")[0],  p.split(";")[1]);
				}
				
				
			}
			
		}
		else {
			return null;
		}
			
			
			
			// Recherche des proprietes des aspects.
//			TypeDocuments typdoc = typedocservice.findByName(docx.getName());
//			String asp = typdoc.getDefaultAspects();
//			String[] _asp = asp.split("/"); 
//			for(String n : _asp) {
//				if(n.trim().startsWith("afbm")) {
//					default_aspect = default_aspect + ";" + n;
//
//					TypeDocuments typdocs = new TypeDocuments();
//					typdocs = typedocservice.findByName(n);
//					String asprop = typdocs.getPropertie(); //recuperation des proprietes de l'aspect
//					logger.info("\"----------------aspect asprop:", asprop);
//					String[] _asprop = asprop.split("/");
//					
//					for(String asn : _asprop) { // recherche de la valeur des proprietes
//						logger.info("----------------aspect:", propriete.get(asn));
//						
//						if(propriete.containsKey(asn)) {
//							metadata.put(asn, propriete.get(asn));
//						}
//					}
//				}
//				
//			}
			
			String[] mois = {"JANVIER", "FEVRIER","MARS","AVRIL","MAI","JUIN","JUILLET",
			                 "AOUT","SEPTEMBRE","OCTOBRE","NOVEMBRE","DECEMBRE"};
			String reper = "" ;
			JSONObject varia = new JSONObject();
			String destfolder = "";
			
			if ("QRCODE".equals(docx.getCateg())) {
				
				if (metadata.has("afbm:trxUser")) {
					reper = (String) metadata.get("afbm:trxUser");
				}
				else {
					reper = "AUTO";
				}
				
				
				varia = new JSONObject();
				varia.put("folderType", "afbm:transferFolder");
				varia.put("rootPath", "/Sites/afriland/documentLibrary");
				varia.put("folderName", reper);
				varia.put("aspects", "afbm:transfer");
				varia.put("destPath", "/afb_numarch/journeeComptables/"+year+'/'+mois[Integer.parseInt(month)-1]);
				
				destfolder = destfolder + "/afb_numarch/journeeComptables/"+year+'/'+mois[Integer.parseInt(month)-1];
				
				data.put("dateKeys", "afbm:trxDate");
			}
			else if("CLASSIC".equals(docx.getCateg())) {
				varia = new JSONObject();
				reper = docx.getUti();
				
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
				String dat = simpleDateFormat.format(new Date());
				year = dat.substring(0,4);
				month = dat.substring(4,6);
				
				varia = new JSONObject();
				varia.put("folderType", "afbm:transferFolder");
				varia.put("rootPath", "/Sites/afriland/documentLibrary");
				varia.put("folderName", reper);
				varia.put("aspects", "afbm:transfer");
				varia.put("destPath", "/afb_numarch/documentClassic/"+year+'/'+mois[Integer.parseInt(month)-1]);

				
				destfolder = destfolder + "/afb_numarch/documentClassic/"+year+'/'+mois[Integer.parseInt(month)-1];
				
				data.put("dateKeys", "");
			}
			
			
			
			//MultiValueMap<String, String> varia = new LinkedMultiValueMap<>();
			
			
			
			HttpHeaders head = new HttpHeaders();
			head.setContentType(MediaType.APPLICATION_JSON);
			restTemplate = new RestTemplate();
			HttpEntity<String> reqEntity = new HttpEntity<String>(varia.toString(), head);
			JSONObject folderID = restTemplate.postForObject(serverCrUrl, reqEntity, JSONObject.class);
			
			
			
			data.put("fileName", "numarch_"+docx.getReference());
			data.put("rootPath", "/Sites/afriland/documentLibrary");
			data.put("destPath", destfolder+'/'+reper);
			data.put("aspects", default_aspect);
			//data.put("folderId", "afb_numarch");
		
			//logger.info("----------------type:", docx.getName());
			data.put("type", docx.getName());
			
			//cm:titled
			metadata.put("cm:title", docx.getName().toUpperCase());
			metadata.put("cm:description", "DESCRIPTION - "+docx.getName().toUpperCase());
			
			data.put("metadata", metadata);
		
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			
//			String username = "willie";
//			String password = ":p@ssword";
//			headers.setBasicAuth(username, password);
			
	//		xhr.setRequestHeader('Authorization', 'Basic ' + btoa(unescape(encodeURIComponent(YOUR_USERNAME + ':' + YOUR_PASSWORD))))

			MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
			body.set("data", data.toString());
			InputStream is = null;
			ByteArrayResource contentsAsResource;
			try {

				is = new FileInputStream(file);//Convertir document en inputstream
				 
		
			    contentsAsResource = new ByteArrayResource(IOUtils.toByteArray(is)) {
			        @Override
			        public String getFilename() {
			            return file.getName(); // Filename has to be returned in order to be able to post.
			        }
			    };
			    is.close();
			} catch (IOException e) {
			    // TODO Auto-generated catch block
			    e.printStackTrace();
			   
			    return null;
			}
			
			body.set("file", contentsAsResource);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);


			restTemplate = new RestTemplate();
			String lienDoc = restTemplate.postForEntity(serverUrl, requestEntity, String.class).getBody();
			
		//	System.out.println("lienDoc: "+lienDoc);
			docx.setUrl(lienDoc);
			docx.setBase64Str("");
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		    e.printStackTrace();
			return null;
		}
		
		
		return docx;
		
	}

	@Override
	public List<Documents> findAllReportDocs(Boolean traiter) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<Documents> cq = cb.createQuery(Documents.class);

		Root<Documents> documents = cq.from(Documents.class);
		List<Predicate> predicates = new ArrayList<>();
		
		

		try {
			Predicate activ = cb.equal(documents.get("traiter"),  traiter);
		     
	        cq.where(activ);
		//	predicates.add(cb.equal(documents.get("traiter"), traiter));
		//	cq.where(predicates.toArray(new Predicate[0]));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entityManager.createQuery(cq)
				.setMaxResults(50)
				.getResultList();
	}

	@Override
	public Documents findByIdDocuments(Long id) {
		return documentsRepository.findByIdDocuments(id);
	}

	@Override
	public Documents findByReference(String ref) {
		return documentsRepository.findByReference(ref);
	}
	
}
