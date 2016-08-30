package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import javax.transaction.Transactional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.rj.schedulesys.config.TestConfiguration;
import com.rj.schedulesys.util.TestUtil;
import com.rj.schedulesys.view.model.TestSubCategoryViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class TestSubCategoryServiceTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	private @Autowired TestSubCategoryService testSubCategoryService;
	
	@Test
	public void test_findAllByTest_WithNonExistingTest(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test found with id : 0");
		
		testSubCategoryService.findAllByTest(0L);
	}
	
	@Test
	public void test_findAllByTest_WithExistingTest(){
		assertEquals(3, testSubCategoryService.findAllByTest(3L).size());
	}
	
	@Test
	public void test_findOne_WithNonExistingTest(){
		assertNull(testSubCategoryService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingTest(){
		TestSubCategoryViewModel viewModel = testSubCategoryService.findOne(1L);
		assertEquals("ANNUAL PPD", viewModel.getName());
	}
	
	@Test
	public void test_findByName_WithNonExistingTest(){
		assertNull(testSubCategoryService.findByName("This test does not exist"));
	}
	
	@Test
	public void test_findByName_WithExistingTest(){
		TestSubCategoryViewModel viewModel = testSubCategoryService.findByName("QUANTIFERON GOLD");
		assertEquals(Long.valueOf(3L), viewModel.getId());
	}
	
	@Test
	public void test_create_WithSubCategoryNameLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 3 and 50");
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				null, "SU", 0L
				);
		
		testSubCategoryService.create(viewModel);
	}
	
	@Test
	public void test_create_WithSubCategoryNameLength_GreaterThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 3 and 50");
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				null, "Some random test name which lentgth's should be greater than 50 chars", 0L
				);
		
		testSubCategoryService.create(viewModel);
	}
	
	
	@Test
	public void test_create_WithNonExsitingParentTest(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test found with name : 0");
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				null, "sub-test-name", 0L
				);
		
		testSubCategoryService.create(viewModel);
	}
	
	@Test
	public void test_create_WithExistingParentTest_ThatHasAlreadyASubCategoryOfSameName(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A sub category with name : ANNUAL PPD already exists for test with name : TB TEST");
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				null, "ANNUAL PPD", 3L
				);
		
		testSubCategoryService.create(viewModel);
	}
	
	@Test
	public void test_create_WithExistingParentTest_ThatHasNotTheCategoryBeingCreated(){
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				null, "BRAND-NEW-SUBCAT-1", 3L
				);
		
		viewModel = testSubCategoryService.create(viewModel);
		
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingTestSubCategoryId(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test sub category found with id : 0");
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				0L, "BRAND-NEW-SUBCAT-1", 3L
				);
		
		testSubCategoryService.update(viewModel);
		
	}
	
	@Test
	public void test_update_WithExistingTestSubCategory_ThatAlreayHas_ASubCategory_WithTheNewName(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A sub category with name : 2-STEP PPD already exists for test with name : TB TEST");
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				1L, "2-STEP PPD", 3L
				);
		
		testSubCategoryService.update(viewModel);
		
	}
	
	@Test
	public void test_update_WithExistingTestSubCategory_AndNewSubCategoryName(){
		
		TestSubCategoryViewModel viewModel = TestUtil.aNewTestSubCategoryViewModel(
				1L, "BRAND-NEW-NAME", 3L
				);
		
		testSubCategoryService.update(viewModel);
		
		assertEquals("BRAND-NEW-NAME", testSubCategoryService.findOne(1L).getName());
	}
	
	@Test
	public void test_delete_WithNonExistingTestSubCategoryId(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test sub category found with id : 0");
		
		testSubCategoryService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingTestSubCategoryThatHasBeenTakenByNurses(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Test sub category with id : 4 can not be deleted, it has been taken by nurses");
		
		testSubCategoryService.delete(4L);
	}
	
}