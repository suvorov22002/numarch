package com.afb.dsi.numarch.dtos;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.modelmapper.ModelMapper;

import com.afb.dsi.numarch.entities.Documents;
import com.afb.dsi.numarch.entities.Params;
import com.afb.dsi.numarch.entities.Parent;
import com.afb.dsi.numarch.entities.Properties;

public class NumArchUtils {
	
	public final static String DATE_HOUR_FORMAT_TT="yyMMddHHmmssSSSS";
	
	/**
	 * Transforme une entity TypeEvidence en un POJO TypeEvidenceDTO
	 *
	 * @param typeEvidence
	 * @return
	 */
	public static PropertiesDTO mapPropertiesToPropertiesDTO(Properties properties) {
		ModelMapper mapper = new ModelMapper();
		PropertiesDTO propertiesDTO = mapper.map(properties, PropertiesDTO.class);
		return propertiesDTO;
	}


	/**
	 * Transforme un POJO TypeEvidenceDTO en une entity TypeEvidence
	 *
	 * @param typeEvidenceDTO
	 * @return
	 */
	public static Properties mapPropertiesDTOToProperties(PropertiesDTO propertiesDTO) {
		ModelMapper mapper = new ModelMapper();
		Properties typeEvidence = mapper.map(propertiesDTO, Properties.class);
		return typeEvidence;
	}
	

	/**
	 * Transforme un POJO RapportJournalierDTO en une entity RapportJournalier
	 *
	 * @param rapportJournalierDTO
	 * @return
	 */
	public static  Documents mapDocumentsDTOToDocuments(DocumentsDTO documentsDTO) {
		ModelMapper mapper = new ModelMapper();
		 Documents documents = mapper.map(documentsDTO,  Documents.class);
		return documents;
	}
	
	
	
	/**
	 * Transforme une entity ActiviteJournaliere en un POJO ActiviteJournaliereDTO
	 *
	 * @param activiteJournaliere
	 * @return
	 */
	public static DocumentsDTO mapDocumentsToDocumentsDTO(Documents documents) {
		ModelMapper mapper = new ModelMapper();
		DocumentsDTO documentsDTO = mapper.map(documents, DocumentsDTO.class);
		return documentsDTO;
	}
		
	public static Long now(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_HOUR_FORMAT_TT);
		String format = simpleDateFormat.format(new Date());
		return Long.valueOf(format) ;
	}
	
	public static ParentDTO mapParentToParentDTO(Parent p) {
		ModelMapper mapper = new ModelMapper();
		ParentDTO parentDTO = mapper.map(p, ParentDTO.class);
		return parentDTO;
	}
	
	public static Parent mapParentDTOToParent(ParentDTO p) {
		ModelMapper mapper = new ModelMapper();
		Parent parent = mapper.map(p,  Parent.class);
		return parent;
	}
	
	public static Params mapParamsDTOToParams(ParamsDTO p) {
		ModelMapper mapper = new ModelMapper();
		Params params = mapper.map(p,  Params.class);
		return params;
	}
	
	public static ParamsDTO mapParamsToParamsDTO(Params p) {
		ModelMapper mapper = new ModelMapper();
		ParamsDTO paramsDTO = mapper.map(p, ParamsDTO.class);
		return paramsDTO;
	}

}
