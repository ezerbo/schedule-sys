package com.rj.sys.service;

import java.util.LinkedList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rj.sys.dao.TestTypeDao;
import com.rj.sys.dao.UserDao;
import com.rj.sys.dao.UserTestDao;
import com.rj.sys.domain.TestType;
import com.rj.sys.domain.User;
import com.rj.sys.domain.UserTest;
import com.rj.sys.domain.UserTestPK;
import com.rj.sys.view.model.UserTestViewModel;

@Slf4j
@Service
public class UserTestService {

	private @Autowired UserDao userDao;
	private @Autowired UserTestDao userTestDao;
	private @Autowired TestTypeDao testTypeDao;

	private @Autowired DozerBeanMapper dozerMapper;

	@Transactional
	public UserTestViewModel addOrUpdateTestForUser(UserTestViewModel viewModel) {
		log.info("Adding test for user with id : {}", viewModel.getUserId());
		TestType testType = testTypeDao.findByName(viewModel.getTestTypeName());
		User user = userDao.findOne(viewModel.getUserId());

		UserTestPK id = UserTestPK.builder().userId(user.getId().intValue())
				.testTypeId(testType.getId().intValue()).build();

		UserTest userTest = UserTest.builder().id(id).user(user)
				.testType(testType).completedDate(viewModel.getCompletedDate())
				.expirationDate(viewModel.getExpirationDate()).build();

		userTest = userTestDao.merge(userTest);
		viewModel = dozerMapper.map(userTest, UserTestViewModel.class);

		log.info("Created test : {}", viewModel);
		return viewModel;
	}

	@Transactional
	public void removeTest(Long userId, Long testTypeId) {
		log.info(
				"Deleting test type with id : {} from tests list of user with id : {}",
				testTypeId, userId);

		UserTestPK id = UserTestPK.builder().userId(userId.intValue())
				.testTypeId(testTypeId.intValue()).build();

		userTestDao.delete(userTestDao.findById(id));
	}

	@Transactional
	public UserTestViewModel findById(Long userId, Long testTypeId) {
		log.info("Finding UserTest by userid : {} and test type id : {} ",
				userId, testTypeId);
		UserTestPK id = UserTestPK.builder().userId(userId.intValue())
				.testTypeId(testTypeId.intValue()).build();
		UserTestViewModel viewModel = null;
		try {
			viewModel = dozerMapper.map(userTestDao.findById(id),
					UserTestViewModel.class);
		} catch (Exception nre) {
			log.info("User with id : {} has not taken test with id : {}",
					userId, testTypeId);
		}

		log.info("UserTests found : {}", viewModel);
		return viewModel;
	}

	@Transactional
	public List<UserTestViewModel> findAllByUserId(Long userId) {

		log.info("Finding all tests for user with id : {}", userId);

		List<UserTestViewModel> viewModels = new LinkedList<>();
		List<UserTest> userTests = userTestDao.findAllByUserId(userId);
		for (UserTest userTest : userTests) {
			viewModels.add(dozerMapper.map(userTest, UserTestViewModel.class));
		}

		log.info("UserTests found : {}", viewModels);
		return viewModels;
	}
}