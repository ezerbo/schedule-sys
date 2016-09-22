package com.rj.schedulesys.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.stereotype.Repository;

import com.rj.schedulesys.domain.Contact;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ContactDao extends GenericDao<Contact>{
	
	public ContactDao() {
		setClazz(Contact.class);
	}

	public Contact find(String firstName, String lastName, String title, Long privateCareId){
		Contact contact = null;
		try{
			contact = entityManager.createQuery(
					"from Contact c where c.firstName =:firstName "
							+ "and c.lastName =:lastName "
							+ "and c.title =:title "
							+ "and c.privateCare.id =:privateCareId", Contact.class)
			.setParameter("firstName", firstName)
			.setParameter("lastName", lastName)
			.setParameter("title", title)
			.setParameter("privateCareId", privateCareId)
			.getSingleResult();
		}catch(NoResultException e){
			log.warn("No contact found with first name : {}, last name : {} and title : {}"
					, firstName, lastName, title);
		}
		return contact;
	}
	
	/**
	 * @param id
	 * @return
	 */
	public List<Contact> findAllByPrivateCare(Long id){
		List<Contact> contacts = entityManager.createQuery(
				"from Contact sm where sm.privateCare.id =:id "
				, Contact.class)
				.setParameter("id", id)
				.getResultList();
		return contacts;
	}
}
