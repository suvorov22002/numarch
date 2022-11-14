package com.afb.dsi.numarch.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.afb.dsi.ddil.scanToAlfresco.App;
import com.afb.dsi.numarch.AppInitializator;
import com.afb.dsi.numarch.dtos.DocumentsDTO;
import com.afb.dsi.numarch.dtos.NumArchUtils;
import com.afb.dsi.numarch.entities.Documents;
import com.afb.dsi.numarch.entities.Proprietes;
import com.afb.dsi.numarch.entities.ResponseData;
import com.afb.dsi.numarch.entities.Transaction;
import com.afb.dsi.numarch.services.IDocumentsService;
import com.afb.dsi.numarch.services.IParamsService;
import com.afb.dsi.numarch.services.IProprietesService;
import com.afb.dsi.numarch.tools.ExtractImagesUseCase;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;



@CrossOrigin
@RestController
@RequestMapping("/rest/api/numarch/document")
public class DocumentsController {
	
public static final Logger logger = LoggerFactory.getLogger(DocumentsController.class);
	
	@Autowired
	private IDocumentsService documentsService;
	
	@Autowired
	private IProprietesService proprieteservice;
	
	@Autowired
	private static IParamsService paramservice;
	
	private final Path root = Paths.get("uploads");
	
	/**
	 * Ajouter une nouvelle ActiviteJournaliere dans la BD. Si l ActiviteJournaliere existe déjà, on
	retourne un reference indiquant que la création n'a pas abouti.
	 * @param activiteJournaliereDTORequest
	 * @return
	 */
	@PostMapping("/add")
	@ApiOperation(value = "Ajouter un document", response = DocumentsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: le document existe deja"),
			@ApiResponse(code = 201, message = "cree : le document a ete enregistre avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le document n'a pa ete cree") })
	public ResponseEntity<DocumentsDTO> createNew(@RequestBody DocumentsDTO
			documentsDTORequest) {
		//, UriComponentsBuilder uriComponentBuilder
		App a;
		String referenceDocument = "";
		referenceDocument = documentsDTORequest.getReference();
		if(referenceDocument == null || referenceDocument == "") {
			// cas d'un envoi reporté
			referenceDocument = "";
			referenceDocument = referenceDocument + NumArchUtils.now()+"_"+documentsDTORequest.getOlname();
			documentsDTORequest.setReference(referenceDocument);
		 try {
			String uti = documentsDTORequest.getUti();
			logger.info("User: "+uti+" | Filename: "+documentsDTORequest.getOlname());
		//	String folder = AppInitializator.params.get("DEPOT_FOLDER").getValeur()+referenceDocument+".jpg";
			String folder = AppInitializator.params.get("DEPOT_FOLDER").getValeur()+referenceDocument+".pdf";
	//		logger.info("User: "+uti+" | Filename: "+documentsDTORequest.getOlname());
	//		System.out.println("CATEG: "+documentsDTORequest.getCateg());
			String data = documentsDTORequest.getBase64Str();
			String partSeparator = ",";
				if (data.contains(partSeparator)) {
				  String encodedImg = data.split(partSeparator)[1];
				  byte[] _data = Base64.getDecoder().decode(encodedImg);
			         // Save as file, 1.jpg will eventually appear in the root directory of the project
				  OutputStream out = new FileOutputStream(folder);
				  out.write(_data);
				  out.flush();
				  out.close();
				  
				 // documentsDTORequest.setBase64Str(referenceDocument+".jpg");
				  documentsDTORequest.setBase64Str(referenceDocument+".pdf");
				
				//  documentsDTORequest.setTraiter(Boolean.FALSE);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Documents documentsRequest = mapDocumentsDTOToDocuments(documentsDTORequest);
		Documents documentsResponse = documentsService.saveDocuments(documentsDTORequest);
		if (documentsResponse != null) {
			DocumentsDTO documentsDTO = mapDocumentsToDocumentsDTO(documentsResponse);
			return new ResponseEntity<DocumentsDTO>(documentsDTO, HttpStatus.CREATED);
		}
		return new ResponseEntity<DocumentsDTO>(HttpStatus.NOT_MODIFIED);
	
	}
	
	/**
	 * Ajouter une nouvelle ActiviteJournaliere dans la BD. Si l ActiviteJournaliere existe déjà, on
	retourne un reference indiquant que la création n'a pas abouti.
	 * @param activiteJournaliereDTORequest
	 * @return
	 * @throws IOException 
	 */
	@PostMapping("/addtrx")
	@ApiOperation(value = "Ajouter piece comptable", response = DocumentsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: le document existe deja"),
			@ApiResponse(code = 201, message = "cree : le document a ete enregistre avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le document n'a pa ete cree") })
	public ResponseEntity<ResponseData> createNewTrx(@RequestBody DocumentsDTO
			documentsDTORequest) throws IOException {
		//, UriComponentsBuilder uriComponentBuilder
		App a;
		String referenceDocument = "";
		String uti = documentsDTORequest.getUti();
		String dir = AppInitializator.params.get("TMP_FOLDER").getValeur()+uti+"_work_tmp/";
		ResponseData respdata = new ResponseData();
		List ltrx = new ArrayList<Transaction>();
		referenceDocument = documentsDTORequest.getReference();
		if(referenceDocument == null || referenceDocument == "") {
			// cas d'un envoi reporté
			referenceDocument = "";
			referenceDocument = referenceDocument + NumArchUtils.now()+"_"+documentsDTORequest.getOlname();
			documentsDTORequest.setReference(referenceDocument);
		 try {
			
			String folder = AppInitializator.params.get("TMP_FOLDER").getValeur()+referenceDocument+".jpg";
	//		logger.info("User: "+uti+" | Filename: "+documentsDTORequest.getOlname());
			
			Path path = Paths.get(dir);
            Files.createDirectories(path);
            
            path.getFileName().toString();
            folder =  path.toAbsolutePath().toString() + "/" + referenceDocument+".jpg";
        //    logger.info("PATH: "+folder);
			String data = documentsDTORequest.getBase64Str();
			String partSeparator = ",";
				if (data.contains(partSeparator)) {
				  String encodedImg = data.split(partSeparator)[1];
				  byte[] _data = Base64.getDecoder().decode(encodedImg);
			         // Save as file, 1.jpg will eventually appear in the root directory of the project
				  OutputStream out = new FileOutputStream(folder);
				  out.write(_data);
				  out.flush();
				  out.close();
				  
				  documentsDTORequest.setBase64Str(referenceDocument+".jpg");
				  List resultWork = new ArrayList<String>();
				  a = new App();
				  resultWork = a.process(path.toAbsolutePath().toString());
				  
				  if(resultWork == null) {
					  respdata.setMessage("Impossible de traiter");
					  respdata.setCodeResponse("500");
					  respdata.setLtrx(ltrx);
					  return new ResponseEntity<ResponseData>(respdata, HttpStatus.NO_CONTENT);
				  }
				 
				//  resultWork.forEach(System.out::println);
				//	resultWork.forEach(x -> System.out.println(x));
				  ltrx.clear();
				  for(Object str : resultWork) {
					  Transaction trx;
					  String ss = (String)str;
					  ltrx.add(transform(ss));
				  }
				 
				  ltrx.forEach(x -> System.out.println((Transaction)x));
				  respdata.setMessage("Decode succesful");
				  respdata.setCodeResponse("000");
				  respdata.setLtrx(ltrx);
				}
				
				FileUtils.deleteDirectory(new File(dir));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				 e.printStackTrace();
				 respdata.setMessage("Impossible de traiter");
				 respdata.setCodeResponse("500");
				 respdata.setLtrx(ltrx);
				 FileUtils.deleteDirectory(new File(dir));
				 return new ResponseEntity<ResponseData>(respdata, HttpStatus.NO_CONTENT);
			}
		}
		
//		Documents documentsRequest = mapDocumentsDTOToDocuments(documentsDTORequest);
//		Documents documentsResponse = documentsService.saveDocuments(documentsDTORequest);
//		if (documentsResponse != null) {
//			DocumentsDTO documentsDTO = mapDocumentsToDocumentsDTO(documentsResponse);
//			//return new ResponseEntity<DocumentsDTO>(documentsDTO, HttpStatus.CREATED);
//			 return new ResponseEntity<ResponseData>(respdata, HttpStatus.CREATED);
//		}
		 return new ResponseEntity<ResponseData>(respdata, HttpStatus.CREATED);
		
	
	}
	
	@PostMapping("/addtrxs")
	@ApiOperation(value = "Ajouter piece comptable", response = DocumentsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: le document existe deja"),
			@ApiResponse(code = 201, message = "cree : le document a ete enregistre avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le document n'a pa ete cree") })
	public ResponseEntity<ResponseData> createNewTrxs(@RequestBody List<DocumentsDTO>
			documentsDTORequest) throws IOException {
		
		
		//logger.info("TMP_FOLDER: "+AppInitializator.params.get("TMP_FOLDER"));
		App a;
		String uti;
		String dir;
		String referenceDocument;
		Path path;
		ResponseData respdata = new ResponseData();
		List ltrx = new ArrayList<Transaction>();
		logger.info("Taille liste Pieces: "+documentsDTORequest.size());
		if(documentsDTORequest.size() > 0) {
			uti = documentsDTORequest.get(0).getUti();
			dir =  AppInitializator.params.get("TMP_FOLDER").getValeur()+uti+"_work_tmp/";
			path = Paths.get(dir);
            Files.createDirectories(path);
            if( Files.exists(path)) {
            	 logger.info("Repertoire crée avec succes");
            }
		}
		else {
			  respdata.setMessage("Impossible de traiter");
			  respdata.setCodeResponse("500");
			  respdata.setLtrx(ltrx);
			  return new ResponseEntity<ResponseData>(respdata, HttpStatus.NO_CONTENT);
		}
		
		for(DocumentsDTO docDto : documentsDTORequest) {
			
			referenceDocument = "";
			referenceDocument = docDto.getReference();
			
			if(referenceDocument == null || referenceDocument == "") {
				
				referenceDocument = "";
				referenceDocument = referenceDocument + NumArchUtils.now()+"_"+docDto.getOlname();
				docDto.setReference(referenceDocument);
				
				try {
					String folder =  path.toAbsolutePath().toString() + "/" + referenceDocument+".jpg";
					String data = docDto.getBase64Str();
					String partSeparator = ",";
					
					if (data.contains(partSeparator)) {
						  String encodedImg = data.split(partSeparator)[1];
						  byte[] _data = Base64.getDecoder().decode(encodedImg);
					         // Save as file, 1.jpg will eventually appear in the root directory of the project
						  OutputStream out = new FileOutputStream(folder);
						  out.write(_data);
						  out.flush();
						  out.close();
						  
						//  docDto.setBase64Str(referenceDocument+".jpg");
					}
					
				}
				catch (IOException e) {
					 e.printStackTrace();
					 respdata.setMessage("Impossible de traiter");
					 respdata.setCodeResponse("500");
					 respdata.setLtrx(ltrx);
					 FileUtils.deleteDirectory(new File(dir));
					 return new ResponseEntity<ResponseData>(respdata, HttpStatus.NO_CONTENT);
				}
			}
		}
		
		List resultWork = new ArrayList<String>();
		
		  a = new App();
		  resultWork = a.process(path.toAbsolutePath().toString());
		  
		  if(resultWork == null) {
			  respdata.setMessage("Impossible de traiter");
			  respdata.setCodeResponse("500");
			  respdata.setLtrx(ltrx);
			  FileUtils.deleteDirectory(new File(dir));
			  return new ResponseEntity<ResponseData>(respdata, HttpStatus.NO_CONTENT);
		  }
		 
		//  resultWork.forEach(System.out::println);
		//	resultWork.forEach(x -> System.out.println(x));
		  ltrx.clear();
		  for(Object str : resultWork) {
			  Transaction trx;
			  String ss = (String)str;
			  ltrx.add(transform(ss));
		  }
		 
//		  ltrx.forEach(x -> System.out.println((Transaction)x));
		  respdata.setMessage("Decode succesful");
		  respdata.setCodeResponse("000");
		  respdata.setLtrx(ltrx);
		  FileUtils.deleteDirectory(new File(dir));
		
		 return new ResponseEntity<ResponseData>(respdata, HttpStatus.CREATED);
	}
	
	
	@PostMapping("/get-img")
	@ApiOperation(value = "extraire images", response = DocumentsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = ""),
			@ApiResponse(code = 201, message = ""),
			@ApiResponse(code = 304, message = "") })
	public ResponseEntity<ResponseData> extractedImg(@RequestBody String
			data) throws IOException {
		 
		 Path path;
		 ResponseData respdata = new ResponseData();
		 String outputDir;
		 //String outputDir = "D:\\projetsAfb\\scanToAveroes\\Docs\\images\\Output";
		 List<String> listBase64 = null;
		 
		 synchronized (this) {
			 outputDir =  AppInitializator.params.get("OUT_FOLDER").getValeur()+"output_"+ NumArchUtils.now() +"/";
			 path = Paths.get(outputDir);
	         Files.createDirectories(path);
		 }
		 
         if( Files.exists(path)) {
         	 logger.info("Repertoire d'extraction crée avec succes");
         	 
	         listBase64 = new ArrayList<String>();;
	
	         ExtractImagesUseCase useCase = new ExtractImagesUseCase(
	           		 data,
	   	                outputDir
	   	     );
	         listBase64 = useCase.execute();
	   		
	   			    
	   		 respdata.setMessage(listBase64.size() + "fichiers extraits.");
	   		 respdata.setCodeResponse("200");
	   		 respdata.setLtrx(listBase64);
	   		 FileUtils.deleteDirectory(new File(outputDir));
	   		 
	   		 return new ResponseEntity<ResponseData>(respdata, HttpStatus.CREATED);
         }
         
         respdata.setMessage("Impossible de traiter");
   		 respdata.setCodeResponse("500");
   		 respdata.setLtrx(listBase64);
   		 return new ResponseEntity<ResponseData>(respdata, HttpStatus.NO_CONTENT);
         
	}
	
	@PostMapping("/addftrxs")
	@ApiOperation(value = "Ajouter piece comptable", response = DocumentsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: le document existe deja"),
			@ApiResponse(code = 201, message = "cree : le document a ete enregistre avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le document n'a pa ete cree") })
	public ResponseEntity<ResponseData> createNewFluxTrxs(@RequestBody List<DocumentsDTO>
			documentsDTORequest) throws IOException {
		
		
		//logger.info("TMP_FOLDER: "+AppInitializator.params.get("TMP_FOLDER"));
		App a;
		String uti;
		String dir;
		String referenceDocument;
		Path path;
		ResponseData respdata = new ResponseData();
		List ltrx = new ArrayList<List<Transaction>>();
		logger.info("Taille liste Pieces: "+documentsDTORequest.size());
		
	synchronized (this) {
		
		if(documentsDTORequest.size() > 0) {
			uti = documentsDTORequest.get(0).getUti();
			
		//	synchronized (this) {
				
				dir =  AppInitializator.params.get("TMP_FOLDER").getValeur()+"tmp_"+NumArchUtils.now()+"/";
				path = Paths.get(dir);
	            Files.createDirectories(path);
	            if( Files.exists(path)) {
	            	 logger.info("Repertoire crée avec succes");
	            }
				
		//	}
			
		}
		else {
			  respdata.setMessage("Impossible de traiter");
			  respdata.setCodeResponse("500");
			  respdata.setLtrx(ltrx);
			  return new ResponseEntity<ResponseData>(respdata, HttpStatus.NO_CONTENT);
		}
		
		for(DocumentsDTO docDto : documentsDTORequest) {
			
			referenceDocument = "";
			referenceDocument = docDto.getReference();
			String encodedImg;
			
			if(referenceDocument == null || referenceDocument == "") {
				
				referenceDocument = "";
				referenceDocument = referenceDocument + NumArchUtils.now()+"_"+docDto.getOlname();
				docDto.setReference(referenceDocument);
				
				try {
					String folder =  path.toAbsolutePath().toString() + "/" + referenceDocument+".jpg";
					String data = docDto.getBase64Str();
					String partSeparator = ",";
					
					if (data.contains(partSeparator)) {
						  encodedImg = data.split(partSeparator)[1];
						  
						//  docDto.setBase64Str(referenceDocument+".jpg");
					}
					else {
						encodedImg = data;
					}
					
					byte[] _data = Base64.getDecoder().decode(encodedImg);
			         // Save as file, 1.jpg will eventually appear in the root directory of the project
				    OutputStream out = new FileOutputStream(folder);
				    out.write(_data);
				    out.flush();
				    out.close();
					
				}
				catch (IOException e) {
					 e.printStackTrace();
					 respdata.setMessage("Impossible de traiter");
					 respdata.setCodeResponse("500");
					 respdata.setLtrx(ltrx);
					 FileUtils.deleteDirectory(new File(dir));
					 return new ResponseEntity<ResponseData>(respdata, HttpStatus.OK);
				}
			}
		}
		
	}
		
		List resultWork = new ArrayList<String>();
		  a = new App();
		  resultWork = a.process(path.toAbsolutePath().toString());
		  
		  if(resultWork == null) {
			  respdata.setMessage("Impossible de traiter");
			  respdata.setCodeResponse("500");
			  respdata.setLtrx(ltrx);
			 
			  FileUtils.deleteDirectory(new File(dir));
			  return new ResponseEntity<ResponseData>(respdata, HttpStatus.NO_CONTENT);
		  }
		 
		//  resultWork.forEach(System.out::println);
		//	resultWork.forEach(x -> System.out.println(x));
		  ltrx.clear();
		  List lTransc = new ArrayList<Transaction>();
		  for(Object str : resultWork) {
			  Transaction trx;
			  String ss = (String)str;
			  Transaction transact = transform(ss);
			  if(transact.getDecode() && transact.getMon() != 0) {
				  lTransc.add(transact);
			//	  lTransc.forEach(x -> System.out.println((Transaction)x));
				  ltrx.add(lTransc);
				  lTransc = new ArrayList<Transaction>();
			  }
			  else {
				  lTransc.add(transact);
			  }
			 
			 
		  }
		  
		  if (lTransc.size() > 0) {
			  ltrx.add(lTransc);
		  }
		 
		  logger.info("Transact traite: "+ltrx.size());
		//  ltrx.forEach(x -> System.out.println((List)x));
		  respdata.setMessage("Decode succesful");
		  respdata.setCodeResponse("000");
		  respdata.setLtrx(ltrx);
		//  FileUtils.deleteDirectory(new File(dir));
		
		 return new ResponseEntity<ResponseData>(respdata, HttpStatus.CREATED);
	}
	
	@PostMapping("/sendToAcs")
	@ResponseBody
	@ApiOperation(value = "Envoi vers ACS", response = DocumentsDTO.class)
	@ApiResponses(value = { @ApiResponse(code = 409, message = "Conflit: le document existe deja"),
			@ApiResponse(code = 201, message = "cree : le document a ete envoye avec succes"),
			@ApiResponse(code = 304, message = "Erreur : le document n'a pa ete envoyé") })
//	public ResponseEntity<DocumentsDTO> envoiToAcs(@RequestBody ObjectAcs obj) {
	public ResponseEntity<DocumentsDTO> envoiToAcs(@RequestBody DocumentsDTO doc) {
		//, UriComponentsBuilder uriComponentBuilder
		System.out.println("Envoi vers ACS");
		//String data = obj.getFile();
		String data = doc.getBase64Str();
	//	System.out.println(str);
		
//		DocumentsDTO document = obj.getDoc();
		DocumentsDTO document = doc;
		String referenceDocument = "";
		referenceDocument = referenceDocument + NumArchUtils.now();
		document.setReference(referenceDocument);
		
		File file;
		Documents docx = mapDocumentsDTOToDocuments(document);
	
		try {
			String uti;		
			String partSeparator = ",";
			uti = doc.getUti();
			if (data.contains(partSeparator)) {
			  String encodedImg = data.split(partSeparator)[1];
//			  byte[] decodedImg = Base64.getDecoder().decode(encodedImg.getBytes(StandardCharsets.UTF_8));
//			  Path destinationFile = Paths.get("uploads", "myImage.jpg");
//			  Files.write(destinationFile, decodedImg);
//			  System.out.println("File created");
			  
			  byte[] _data = Base64.getDecoder().decode(encodedImg);
		         // Save as file, 1.jpg will eventually appear in the root directory of the project
		    OutputStream out = new FileOutputStream( AppInitializator.params.get("TMP_REP").getValeur()+uti+"/1.jpg");
		
		    out.write(_data);
		    out.flush();
		    out.close();
		    file = new File( AppInitializator.params.get("TMP_REP").getValeur()+uti+"/1.jpg");
		    
		 //   InputStream inputStreams = new FileInputStream(file);
		//	 MultipartFile mfile = new MockMultipartFile(file.getName(), inputStreams);
			 
		    Documents d = documentsService.sendToAcs(docx, file);
		    
		    DocumentsDTO resp_doc = mapDocumentsToDocumentsDTO(d);
			 
			 return new ResponseEntity<DocumentsDTO>(resp_doc, HttpStatus.CREATED);

			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return new ResponseEntity<DocumentsDTO>(HttpStatus.EXPECTATION_FAILED);
		   
	}
	
	/**
	 * Retourne la liste de tous les typeEvidences
	 * @param 
	 * @return
	 */
	@GetMapping("/getall")
	@ApiOperation(value="Obtenir plusieurs types Evidences", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfully listed"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<List<DocumentsDTO>> getAlls() {
		//, UriComponentsBuilder uriComponentBuilder

		List<Documents> documents = documentsService.getAllDocument();
		if (documents != null && !CollectionUtils.isEmpty(documents)) {
			List<DocumentsDTO> documentsDTO = documents.stream().map(document -> {
				return mapDocumentsToDocumentsDTO(document);
			}).collect(Collectors.toList());
			return new ResponseEntity<List<DocumentsDTO>>(documentsDTO, HttpStatus.OK);
		}
		return new ResponseEntity<List<DocumentsDTO>>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * @param name
	 * @return
	 */
	@GetMapping("/findbyid/{id}")
	@ApiOperation(value="Rechercher un document par son id", response = DocumentsDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfull research"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<DocumentsDTO> findById(@PathVariable("id") Long id) {

		//UriComponentsBuilder uriComponentBuilder
		Documents documentsResponse = documentsService.findByIdDocuments(id);

		if (documentsResponse != null) {
			DocumentsDTO documentsDTO = mapDocumentsToDocumentsDTO(documentsResponse);
			return new ResponseEntity<DocumentsDTO>(documentsDTO, HttpStatus.OK);
		}

		return new ResponseEntity<DocumentsDTO>(HttpStatus.NO_CONTENT);
	}
	
	/**
	 * @param name
	 * @return
	 */
	@GetMapping("/findbyref/{ref}")
	@ApiOperation(value="Rechercher un document par reference", response = DocumentsDTO.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Ok: successfull research"),
			@ApiResponse(code = 204, message = "No Content: no result founded"),
	})
	public ResponseEntity<ResponseData> findByRef(@PathVariable("ref") String ref) {
		
		ResponseData respdata = new ResponseData();
		List<DocumentsDTO> lDoc = new ArrayList<DocumentsDTO>();
		//UriComponentsBuilder uriComponentBuilder
		Documents documentsResponse = documentsService.findByReference(ref);
	
		if (documentsResponse != null) {
			
			if (!documentsResponse.getTraiter()) {
				Documents doc = null;
				respdata = process(documentsResponse);
				List ldoc = respdata.getLtrx();
				if (ldoc.size() > 0) {
					doc = (Documents) ldoc.get(0);
				}
				
				
				if (doc != null) {
					DocumentsDTO documentsDTO = mapDocumentsToDocumentsDTO(doc);
					
					respdata.setMessage("ENVOYE");
					respdata.setError("");
					respdata.setCodeResponse("200");
					lDoc.add(documentsDTO);
					
					return new ResponseEntity<ResponseData>(respdata, HttpStatus.OK);
				}
				else {
					respdata.setMessage("NON ENVOYE");
					return new ResponseEntity<ResponseData>(respdata, HttpStatus.OK);
				}
				
			}
			else {
				respdata.setMessage("DEJA TRAITE");
				respdata.setError("");
				respdata.setCodeResponse("500");
				lDoc.add(null);
				
				return new ResponseEntity<ResponseData>(respdata, HttpStatus.OK);
			}
		}
		
		respdata.setMessage("NON ENVOYE");
		respdata.setError("");
		respdata.setCodeResponse("500");
		lDoc.add(null);
		return new ResponseEntity<ResponseData>(respdata, HttpStatus.OK);
	}
	
	@PostMapping(value="/efile",  consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String createFile(@RequestPart(name = "file") MultipartFile file) throws IOException {
		logger.info("Post File: "+file.getName());
		return "test file";
	}
	
	/**
	 * Transforme une entity  en un POJO ActiviteJournaliereDTO
	 *
	 * @param activiteJournaliere
	 * @return
	 */
	private DocumentsDTO mapDocumentsToDocumentsDTO(Documents documents) {
		ModelMapper mapper = new ModelMapper();
		DocumentsDTO documentsDTO = mapper.map(documents, DocumentsDTO.class);
		return documentsDTO;
	}


	/**
	 * Transforme un POJO ActiviteJournaliereDTO en une entity ActiviteJournaliere
	 *
	 * @param activiteJournaliereDTO
	 * @return
	 */
	private Documents mapDocumentsDTOToDocuments(DocumentsDTO documentsDTO) {
		ModelMapper mapper = new ModelMapper();
		Documents doc = mapper.map(documentsDTO, Documents.class);
		return doc;
	}
	
	private Transaction transform(String ss) {
		Transaction trx = new Transaction();
		String[] result = ss.split(";");
		System.out.println("SS: "+ss);
		
		if("ERROR".equals(result[1])) {
			trx.setDecode(Boolean.FALSE);
			trx.setFilename(result[0]);
		}
		else {
			try {
				String v = result[1];
				if(v.length() < 50) {
					trx.setDecode(Boolean.FALSE);
					trx.setFilename(result[0]);
				}
				else {
					trx.setDecode(Boolean.TRUE);
					trx.setFilename(result[0]);
					trx.setEve(v.substring(0, 6));
					trx.setAge(v.substring(6, 11));
					trx.setNcp(v.substring(11, 22));
					trx.setCle(v.substring(22, 24));
					trx.setDco(v.substring(24, 32));
					trx.setUti(v.substring(32, 36));
					trx.setMon(Double.parseDouble(v.substring(36, 50)));
					trx.setType(v.substring(50));
					System.out.println(result[0]);
					//trx.setOlfilename((result[0].split("."))[0].split("_")[1]);
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return trx;
	}
	
	private ResponseData process(Documents d){
	
		File file;
		Documents dx = null;
		ResponseData respDat = new ResponseData();
		List<Documents> resp = new ArrayList<>();
		
		List<Proprietes> docPropriete = new ArrayList<Proprietes>();
		docPropriete = proprieteservice.findByDocument(d.getId());
		d.setDocPropriete(docPropriete);
		
//		List<Params> parameter = paramservice.getAllParams();
//		Map<String, Params> params = new HashMap<String, Params>();
//		
//		for(Params p : parameter) {
//			params.put(p.getCode(), p);
//		}
		
		DocumentsDTO resp_doc =  NumArchUtils.mapDocumentsToDocumentsDTO(d);
		
		try {
			if(resp_doc.getBase64Str() != null || !resp_doc.getBase64Str().isEmpty()) {
				
				
				file = new File(AppInitializator.params.get("DEPOT_FOLDER").getValeur()+resp_doc.getBase64Str());

		        
				resp_doc.setTraiter(Boolean.TRUE);
				
				logger.info("Document ref: "+resp_doc.getReference()+" Name: "+resp_doc.getName());
				Documents docx = NumArchUtils.mapDocumentsDTOToDocuments(resp_doc);
				
				
				if(file.exists()) {
					
					logger.info("file.canWrite: "+file.getParentFile().canWrite());
					
					dx = documentsService.sendToAcs(docx, file);
					
					if(dx != null) {
						
						 logger.info("file.canread(): "+file.canExecute());
						 documentsService.updateDocuments(dx);
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
			String err = e.getMessage();
			respDat.setCodeResponse("500");
			
			if(err.contains("java.net.ConnectException")) {
				respDat.setError("Server connection refused");
			}
			else {
				int lon = err.length();
				String _err = err.substring(2, lon-2);
				String[] __err = _err.split(",");
				for (String c : __err) {
					//System.out.println(c);
					String[] v = c.split(":");
					String x = new String(v[0].replace('\"', ' ').trim());
					System.out.println(x);
					if ("error".equals(x)) {
						String xe = new String(v[1].replace('\"', ' ').trim());
						respDat.setError(xe);
					}
				}
			}
		}
		
		resp.add(dx);
		respDat.setLtrx(resp);
		
		return respDat;
	}
}
