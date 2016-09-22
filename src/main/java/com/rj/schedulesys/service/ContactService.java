package com.rj.schedulesys.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.rj.schedulesys.dao.ContactDao;
import com.rj.schedulesys.dao.PrivateCareDao;
import com.rj.schedulesys.domain.Contact;
import com.rj.schedulesys.domain.PrivateCare;
import com.rj.schedulesys.util.ObjectValidator;
import com.rj.schedulesys.util.ServiceHelper;
import com.rj.schedulesys.view.model.ContactViewModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContactService {
	
	private ContactDao contactDao;
	private PrivateCareDao privateCareDao;
	
	private DozerBeanMapper dozerMapper;
	private ObjectValidator<ContactViewModel> validator;
	
	@Autowired
	public ContactService(PrivateCareDao privateCareDao, ContactDao contactDao,
			ObjectValidator<ContactViewModel> validator, DozerBeanMapper dozerMapper) {
		this.contactDao = contactDao;
		this.privateCareDao = privateCareDao;
		this.dozerMapper = dozerMapper;
		this.validator = validator;
	}
	
	@Transactional
	public ContactViewModel create(ContactViewModel viewModel){
		log.debug("Creating new contact : {}", viewModel);
		Assert.notNull(viewModel, "No contact provided");
		PrivateCare privateCare = privateCareDao.findOne(viewModel.getPrivateCareId());
		String errorMessage;
		if(privateCare == null){
			errorMessage = new StringBuilder().append("No private care found with id : ")
					.append(viewModel.getPrivateCareId())
					.toString();
			ServiceHelper.logAndThrowException(errorMessage);
		}
		//Staff members titles are always all caps
		Contact contact = contactDao.find(
				viewModel.getFirstName(), viewModel.getLastName()
				, StringUtils.upperCase(viewModel.getTitle()), viewModel.getPrivateCareId());
		if(contact != null){
			errorMessage = new StringBuilder().append("A contact with first name '")
					.append(viewModel.getFirstName()).append("', last name '")
					.append(viewModel.getLastName()).append("' and title '")
					.append(viewModel.getTitle()).append("' already exists")
					.toString();
			ServiceHelper.logAndThrowException(errorMessage);
		}
		viewModel.setId(null);
		viewModel = this.createOrUpdate(viewModel); 
		log.debug("Created contact : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	public ContactViewModel update(ContactViewModel viewModel){
		log.debug("Updating contact : {}", viewModel);
		Assert.notNull(viewModel, "No contact provided");
		Assert.notNull(viewModel.getId(), "No contact id provided");
		Contact contact = contactDao.findOne(viewModel.getId());
		if(contact == null){
			log.error("No contact found with id : {}", viewModel.getId());
			throw new RuntimeException("No contact found with id : " + viewModel.getId());
		}
		String errorMessage;
		//If either of the first name, last name, title has been updated, check if there is no staff
		// member with new first name, last name and title provided
		if(!(StringUtils.equalsIgnoreCase(contact.getFirstName(), viewModel.getFirstName())
				||StringUtils.equalsIgnoreCase(contact.getLastName(), viewModel.getLastName())
				||StringUtils.equalsIgnoreCase(contact.getTitle(), viewModel.getTitle()))){
			log.warn("Contact's first name and/or last name and/or title updated, checking combination(firstName, lastName, title, facility)'s uniqueness");
			if(contactDao.find(viewModel.getFirstName(), viewModel.getLastName()
					, StringUtils.upperCase(viewModel.getTitle()), viewModel.getPrivateCareId()) != null){
				errorMessage = new StringBuilder().append("A contact with first name '")
						.append(viewModel.getFirstName()).append("', last name '")
						.append(viewModel.getLastName()).append("' and title '")
						.append(viewModel.getTitle()).append("' already exists for private care with id : ")
						.append(viewModel.getPrivateCareId())
						.toString();
				ServiceHelper.logAndThrowException(errorMessage);
			}
		}
		//When the facility has been updated, check that the new one exists
		if(contact.getPrivateCare().getId() != viewModel.getPrivateCareId()){
			log.warn("Private care updated, checking : {} existance", viewModel.getPrivateCareId());
			if(privateCareDao.findOne(viewModel.getPrivateCareId()) == null){
				errorMessage = new StringBuilder().append("No private care found with id : ")
						.append(viewModel.getPrivateCareId())
						.toString();
				ServiceHelper.logAndThrowException(errorMessage);
			}
		}
		viewModel = this.createOrUpdate(viewModel);
		log.debug("Updated contact : {}", viewModel);
		return viewModel;
	}
	
	@Transactional
	private ContactViewModel createOrUpdate(ContactViewModel viewModel){
		validator.validate(viewModel);
		PrivateCare privateCare = privateCareDao.findOne(viewModel.getPrivateCareId());
		Contact contact = dozerMapper.map(viewModel, Contact.class);
		contact.setPrivateCare(privateCare);
		contact.setTitle(StringUtils.upperCase(contact.getTitle()));//Title are always all caps
		contact = contactDao.merge(contact);
		return dozerMapper.map(contact, ContactViewModel.class);
	}
	
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public List<ContactViewModel> findAllByPrivateCare(Long id){
		log.debug("Fetching all contacts for private care with id : {}", id);
		if(privateCareDao.findOne(id) == null){
			log.error("No private care found with id : " + id);
			throw new RuntimeException("No private care found with id : " + id);
		}
		List<Contact> contacts = contactDao.findAllByPrivateCare(id);
		List<ContactViewModel> viewModels = new LinkedList<ContactViewModel>();
		contacts.stream()
		.forEach(contact -> {
			ContactViewModel viewModel = dozerMapper.map(contact, ContactViewModel.class);
			viewModel.setPrivateCareId(contact.getPrivateCare().getId());
			viewModels.add(viewModel);
		});
		
		return viewModels;
	}
		
	/**
	 * @param id
	 * @return
	 */
	@Transactional
	public ContactViewModel findOne(Long id){		
		log.info("Finding contact by id : {}", id);
		Contact contact = contactDao.findOne(id);
		ContactViewModel viewModel = null;
		if(contact != null){
			viewModel = dozerMapper.map(contact, ContactViewModel.class);
			viewModel.setPrivateCareId(contact.getPrivateCare().getId());
		}else{
			log.debug("No contact found with id : {}", id);
		}
		return viewModel;
	}
	
	/**
	 * @param id
	 * Deletes staff member with given id
	 */
	@Transactional
	public void delete(Long id){
		log.info("Deleting contact with id : {}", id);
		Contact staffMember = contactDao.findOne(id);
		if(staffMember == null){
			log.error("No contact found with id : {}", id);
			throw new RuntimeException("No contact found with id : " + id);
		}
		contactDao.delete(staffMember);
	}

}