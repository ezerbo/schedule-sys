//package com.rj.schedulesys.service;
//
//import java.util.LinkedList;
//import java.util.List;
//
//import org.dozer.DozerBeanMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import com.rj.schedulesys.dao.ScheduleSysUserDao;
//import com.rj.schedulesys.dao.UserTestDao;
//import com.rj.schedulesys.domain.NurseTest;
//import com.rj.schedulesys.domain.NurseTestPK;
//import com.rj.schedulesys.view.model.NurseTestViewModel;
//
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Service
//public class UserTestService {
//
//	private @Autowired ScheduleSysUserDao userDao;
//	private @Autowired UserTestDao userTestDao;
//
//	private @Autowired DozerBeanMapper dozerMapper;
//
//	@Transactional
//	public NurseTestViewModel addOrUpdateTestForUser(NurseTestViewModel viewModel) {
//		log.info("Adding test for user with id : {}", viewModel.getUserId());
////		TestType testType = testTypeDao.findByName(viewModel.getTestTypeName());
////		ScheduleSysUser user = userDao.findOne(viewModel.getUserId());
////
////		NurseTestPK id = NurseTestPK.builder().nurseId(user.getId().intValue())
////				.testTypeId(testType.getId().intValue()).build();
////
////		NurseTest userTest = NurseTest.builder().id(id).user(user)
////				.testType(testType).completedDate(viewModel.getCompletedDate())
////				.expirationDate(viewModel.getExpirationDate()).build();
//
////		userTest = userTestDao.merge(userTest);
////		viewModel = dozerMapper.map(userTest, UserTestViewModel.class);
//
//		log.info("Created test : {}", viewModel);
//		return viewModel;
//	}
//
//	@Transactional
//	public void removeTest(Long userId, Long testTypeId) {
//		log.info(
//				"Deleting test type with id : {} from tests list of user with id : {}",
//				testTypeId, userId);
//
//		NurseTestPK id = NurseTestPK.builder().nurseId(userId.intValue())
//				.testId(testTypeId.intValue()).build();
//
//		userTestDao.delete(userTestDao.findById(id));
//	}
//
//	@Transactional
//	public NurseTestViewModel findById(Long userId, Long testTypeId) {
//		log.info("Finding UserTest by userid : {} and test type id : {} ",
//				userId, testTypeId);
//		NurseTestPK id = NurseTestPK.builder().nurseId(userId.intValue())
//				.testId(testTypeId.intValue()).build();
//		NurseTestViewModel viewModel = null;
//		try {
//			viewModel = dozerMapper.map(userTestDao.findById(id),
//					NurseTestViewModel.class);
//		} catch (Exception nre) {
//			log.info("User with id : {} has not taken test with id : {}",
//					userId, testTypeId);
//		}
//
//		log.info("UserTests found : {}", viewModel);
//		return viewModel;
//	}
//
//	@Transactional
//	public List<NurseTestViewModel> findAllByUserId(Long userId) {
//
//		log.info("Finding all tests for user with id : {}", userId);
//
//		List<NurseTestViewModel> viewModels = new LinkedList<>();
//		List<NurseTest> userTests = userTestDao.findAllByUserId(userId);
//		for (NurseTest userTest : userTests) {
//			viewModels.add(dozerMapper.map(userTest, NurseTestViewModel.class));
//		}
//
//		log.info("UserTests found : {}", viewModels);
//		return viewModels;
//	}
//}