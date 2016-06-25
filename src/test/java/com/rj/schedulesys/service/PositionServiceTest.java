package com.rj.schedulesys.service;

import static org.junit.Assert.*;

import java.util.List;

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
import com.rj.schedulesys.view.model.PositionViewModel;

@Transactional
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(TestConfiguration.class)
public class PositionServiceTest {

	private @Autowired PositionService positionService;
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void test_findAllByType_WithNonExistingType(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No position type found with id : 0");
		positionService.findAllByType(0L);
	}
	
	@Test
	public void test_findAllByType(){
		List<PositionViewModel> types = positionService.findAllByType(2L);
		assertEquals(2, types.size());
		assertEquals("CAREGIVER", types.get(0).getPositionTypeName());
	}
	
	@Test
	public void test_findOne_WithNonExistingPosition(){
		assertNull(positionService.findOne(0L));
	}
	
	@Test
	public void test_findOne_WithExistingPosition(){
		PositionViewModel viewModel = positionService.findOne(1L);
		assertEquals("RN", viewModel.getName());
		assertEquals("NURSE", viewModel.getPositionTypeName());
	}
	
	@Test
	public void test_findByName_WithNonExistingPosition(){
		assertNull(positionService.findByName("This position does not exist"));
	}
	
	@Test
	public void test_findByName_WithExistingPosition(){
		assertEquals(Long.valueOf(1), positionService.findByName("RN").getId());
	}
	
	@Test
	public void test_create_WithPositionNameLength_LessThan_03(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 2 and 50");
		
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(null, "N", "NURSE");
		
		positionService.create(viewModel);
	}
	
	@Test
	public void test_create_WithPositionNameLength_LessThan_50(){
		
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("name size must be between 2 and 50");
		
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(null, "Some position name that length should be greater than 50", "CAREGIVER");
		
		positionService.create(viewModel);
	}
	
	@Test
	public void test_create_WithAlreadyExistingPositionType(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A position with name : LIVE-IN already exists for position type with name : CAREGIVER");
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				null, "LIVE-IN", "CAREGIVER");
		positionService.create(viewModel);
	}
	
	@Test
	public void test_create_WithNonExistingPositionType(){
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				null, "BRAND-NEW", "CAREGIVER");
		viewModel = positionService.create(viewModel);
		assertNotNull(viewModel.getId());
	}
	
	
	@Test
	public void test_update_WithNonExistingPositionType(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No position found with id : 0");
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				0L, "BRAND-NEW", "CAREGIVER");
		positionService.update(viewModel);
	}
	
	@Test
	public void test_update_WithExistingPositionForPositionType(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("A position with name : LNP already exists for position type with name : NURSE");
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				1L, "LNP", "NURSE");
		positionService.update(viewModel);
	}
	
	@Test
	public void test_update_WithNonExistingPositionForNonPositionType(){
		expectedException.expect(RuntimeException.class);
		expectedException.expectMessage("No position type found with name : NON-EXISTING");
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				1L, "LNP-NEW", "NON-EXISTING");
		positionService.update(viewModel);
	}
	
	@Test
	public void test_update_WithNonExistingPositionForExistingPositionType(){
		PositionViewModel viewModel = TestUtil.aNewPositionViewModel(
				1L, "LNP-NEW", "NURSE");
		viewModel = positionService.update(viewModel);
		assertEquals("LNP-NEW", positionService.findOne(1L).getName());
	}
	
}