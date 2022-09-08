package com.afb.dsi.numarch.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.afb.dsi.numarch.dtos.TypeDocumentsDTO;
import com.afb.dsi.numarch.entities.TypeDocuments;
import com.afb.dsi.numarch.repository.TypeDocumentsRepository;
import com.afb.dsi.numarch.services.ITypeDocumentsService;

@Service("typedocumentsservice")
@Transactional
public class TypeDocumentsServiceImpl implements ITypeDocumentsService {
	
	@Autowired
	private TypeDocumentsRepository typeDocumentsRepository;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	@Override
	public void deleteTypeDocuments(Long id) {
		// TODO Auto-generated method stub
		typeDocumentsRepository.deleteById(id);
	}
	
	@Override
	public Optional<TypeDocuments> findById(Long id) {
		// TODO Auto-generated method stub
		return typeDocumentsRepository.findById(id);
	}
	
	@Override
	public boolean checkIfIdexists(Long id) {
		return typeDocumentsRepository.existsById(id);
	}
	
	@Override
	public void deleteDocument(Long id) {
		typeDocumentsRepository.deleteById(id);
	}

	@Override
	public TypeDocuments updateTypeDocuments(TypeDocuments typeEvidence) {
		return typeDocumentsRepository.saveAndFlush(typeEvidence);
	}

	@Override
	public TypeDocuments saveTypeDocuments(TypeDocuments documents) {
		return typeDocumentsRepository.save(documents);
	}

	@Override
	public TypeDocuments findByName(String name) {
		return typeDocumentsRepository.findByName(name);
	}

	@Override
	public List<TypeDocuments> findTypeDocuments(TypeDocumentsDTO typeDocumentsDTO) {
		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaQuery<TypeDocuments> cq = cb.createQuery(TypeDocuments.class);

		Root<TypeDocuments> typeDocuments = cq.from(TypeDocuments.class);
		List<Predicate> predicates = new ArrayList<>();

		try {
			
			if (StringUtils.isNotBlank(typeDocumentsDTO.getDescription())) {
				predicates.add(cb.like(typeDocuments.get("description"), "%" + typeDocumentsDTO.getDescription() + "%"));
			}
			if (StringUtils.isNotBlank(typeDocumentsDTO.getName())) {
				predicates.add(cb.like(typeDocuments.get("name"), "%" + typeDocumentsDTO.getName() + "%"));
			}
			if (typeDocumentsDTO.getIsContainer() != null) {
				predicates.add(cb.equal(typeDocuments.get("container"), typeDocumentsDTO.getIsContainer()));
			}
						
			cq.where(predicates.toArray(new Predicate[0]));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return entityManager.createQuery(cq).getResultList();
	
	}

	@Override
	public List<TypeDocuments> getAllTypeDocuments() {
		return typeDocumentsRepository.findAll();
	}

	@Override
	public List<TypeDocuments> listAllTypeAcs() {
		// TODO Auto-generated method stub
		return null;
	}

}
