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
import com.rj.schedulesys.view.model.TestViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class TestServiceTest {
	
	private @Autowired TestService testService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); 
	
	@Test
	public void test_findAll(){
		assertEquals(10, testService.findAll().size());
	}
	
	@Test
	public void test_findOne_WithNonExistingId(){
		assertNull(testService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingId(){
		TestViewModel viewModel = testService.findOne(1L);
		assertEquals("DRUG TEST", viewModel.getName());
	}
	
	@Test
	public void test_findByName_WithNonExistingName(){
		assertNull(testService.findByName("This test does not exist"));
	}
	
	@Test
	public void test_findByName_WithExistingName(){
		TestViewModel viewModel = testService.findByName("FLU VACCINE");
		assertEquals(Long.valueOf(2), viewModel.getId());
	}
	
	@Test
	public void test_create_WithTestNameLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 3 and 50");
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(null, "TU", false, true, true);
		testService.create(viewModel);
	}
	
	@Test
	public void test_create_WithTestNameLength_GreaterThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 3 and 50");
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				null, "THIS IS A REALLY LONG TEST NAME, IT SHOULD MORE THAN 50 CHARS", false, true, true
				);
		testService.create(viewModel);
	}
	
	@Test
	public void test_create_WithExistingTestName(){

		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A test with name : FLU VACCINE already exists");
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				null, "FLU VACCINE", false, true, true
				);
		
		testService.create(viewModel);
	}
	
	@Test
	public void test_create_WithNonExistingTestName(){
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				null, "A NEW FLU VACCINE", false, true, true
				);
		
		viewModel = testService.create(viewModel);
		 
		assertNotNull(viewModel.getId());
	}
	
	@Test
	public void test_update_WithNonExistingTestId(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test found with id : 0");
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				0L, "A NEW FLU VACCINE", false, true, true
				);
		
		testService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingTestIdButExistingTestName(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A test with name : FLU VACCINE already exists");
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				5L, "FLU VACCINE", false, true, true
				);
		
		testService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingTestIdAndNewTestName(){
		
		TestViewModel viewModel = TestUtil.aNewTestViewModel(
				5L, "UPDATED-TEST-NAME", false, true, true
				);
		
		testService.update(viewModel);
		
		assertEquals("UPDATED-TEST-NAME", testService.findOne(5L).getName());
	}
	
	@Test
	public void test_delete_WithNonExistingTestId(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No test found with id : 0");
		
		testService.delete(0L);
	}
	
	@Test
	public void test_delete_WithExistingTestIdThatHasBeenTakenByNurses(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Test with id : 2 can not be deleted, it has been taken by nurses");
		
		testService.delete(2L);
	}
	
	@Test
	public void test_delete_WithExistingTestThatHasSubCategories(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("Test with id : 4 can not be deleted, delete all its sub categories first");
		
		testService.delete(4L);
	}
	
	@Test
	public void test_delete_WithExistingTestThatHasNoSubCategoriesAndHasNotBeenTakenByNurses(){
		testService.delete(5L);
		
		assertNull(testService.findOne(5L));
	}
	
}