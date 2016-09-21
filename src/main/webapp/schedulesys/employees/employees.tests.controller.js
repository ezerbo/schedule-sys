(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('EmployeeTestDialogController', EmployeeTestDialogController);
	
	EmployeeTestDialogController.$Inject = ['$state', '$scope', '$stateParams', '$mdDialog', '$mdToast',
	                                  'NursesService','TestsService', 'EmployeeTestService'];
	
	function EmployeeTestDialogController($state, $scope, $stateParams, $mdDialog,
			$mdToast, NursesService, EmployeeTestService, TestsService){
		
		var vm = this;
		vm.getAllTests = getAllTests;
		vm.cancel = cancel;
		vm.onTestSelect = onTestSelect;
		vm.createOrUpdate = createOrUpdate;
		vm.showToast = showToast;
		vm.selectedTest = null;
		vm.notApplicableAllowed = false;
		vm.showSubcategoriesSelect = false;
		vm.completionDateAllowed = false;
		vm.expirationDateAllowed = false;
		vm.onNotApplicableChange = onNotApplicableChange;
		
		
		getAllTests();
		
		function getAllTests(){
			TestsService.query({}, function(result) {
				vm.tests = result;
			})
		}
		
		function onNotApplicableChange(){
			if(vm.notApplicable){
				vm.completedDate = null;
				vm.expirationDate = null;
			}
		}
		
		function onTestSelect(){
			var selectedEmployeeTest = angular.fromJson(vm.selectedTest);
			vm.subcategories = selectedEmployeeTest.subcategories;
			vm.notApplicable = false;
			if(typeof vm.subcategories !== 'undefined' && vm.subcategories.length > 0){
				vm.showSubcategoriesSelect = true;
			}else{
				vm.showSubcategoriesSelect = false;
			}
			vm.completionDateAllowed = selectedEmployeeTest.hasCompletedDate ? true : false;
			vm.expirationDateAllowed = selectedEmployeeTest.hasExpirationDate ? true : false;
			
			if(selectedEmployeeTest.allowNotApplicable){
				vm.notApplicableAllowed = true;
			}else{
				vm.notApplicableAllowed = false;
				vm.notApplicable = false;
			}
		}
		
		function createOrUpdate(){
			if(angular.isUndefined($stateParams.testId)){
				var employeeTest = {
					employeeId:	$stateParams.id,
					testId: angular.fromJson(vm.selectedTest).id,
					testSubCategoryId: angular.isUndefined(vm.selectedCategory) ? null : angular.fromJson(vm.selectedCategory).id,
					completedDate: vm.completedDate,
					expirationDate: vm.expirationDate,
					status: (vm.notApplicable) ? 'NOT APPLICABLE' : 'APPLICABLE'
				};
				EmployeeTestService.save({id:$stateParams.id}, employeeTest, function(result){
					vm.showToast("Test successfully added", 5000);
					vm.cancel();
				},function(result){
					vm.showToast(result.data, 5000);
				});
			}else{
				vm.showToast("Unable to add test", 5000);
			}
		}
		
		function cancel(){
			$mdDialog.cancel();
		}
		
		//TODO move this to common.js
		function showToast(textContent, delay){
			$mdToast.show($mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
	}
}
)();