(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('NurseDetailsController', NurseDetailsController);
	
	NurseDetailsController.$Inject = ['$state', '$scope', '$stateParams', '$mdDialog', '$mdToast',
	                                  'NursesService','NurseLicenseService','LicenseService', 'EmployeeTestService', 'LicenseTypeService'];
	
	function NurseDetailsController($state, $scope, $stateParams, $mdDialog, $mdToast, NursesService,
			NurseLicenseService, EmployeeTestService, LicenseService, LicenseTypeService){
		var vm = this;
		vm.allLicenses = null;
		vm.allTests = [];
		vm.licensesOnCurrentPage = null;
		vm.testsOnCurrentPage = null;
		vm.loadAllLicenses = loadAllLicenses;
		vm.showLicenseDelConfirm = showLicenseDelConfirm;
		vm.showTestDelConfirm = showTestDelConfirm;
		vm.getSelectedNurse = getSelectedNurse;
		
		vm.loadAllTests = loadAllTests;
		
		vm.selectedLicense = [];
		vm.selectedTest = [];
		vm.editOrDelete = true;
		vm.enableTestDel = true;
		vm.showToast = showToast;

		vm.onLicenseTablePaginate = onLicenseTablePaginate;
		vm.sliceArray = sliceArray;
		vm.licenseQuery = {limit: 5, page: 1};
		vm.testQuery = {limit: 5, page: 1};
		
		loadAllLicenses();
		getSelectedNurse();
		
		loadAllTests();
		
		$scope.$watchCollection('vm.selectedLicense', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selectedLicense.length === 0) ? true : false;
		});
		
		$scope.$watchCollection('vm.selectedTest', function(oldValue, newValue) {
			vm.enableTestDel = (vm.selectedTest.length === 0) ? true : false;
		});
		
		function showLicenseDelConfirm(ev) {
			var confirm = $mdDialog.confirm()
			.title('Delete a License')
			.textContent('Are you sure you want to delete this License ?')
			.ariaLabel('License deletion')
			.targetEvent(ev)
			.ok('Delete')
			.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				LicenseService.remove({id:vm.selectedLicense[0].id}, function() {
					vm.licensesOnCurrentPage.splice(vm.licensesOnCurrentPage.indexOf(vm.selectedLicense[0]), 1);
					vm.allLicenses.splice(vm.allLicenses.indexOf(vm.selectedLicense[0]), 1);
					vm.editOrDelete = true;
					vm.showToast('License ' + vm.selectedLicense[0].number + ' successfully deleted', 5000);
				}, function() {
					vm.showToast('License ' + vm.selectedLicense[0].number 
							+ ' could not be deleted ', 5000);
				});
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function showTestDelConfirm(ev){
			var confirm = $mdDialog.confirm()
			.title('Delete a Test')
			.textContent('Are you sure you want to delete this test ?')
			.ariaLabel('Test deletion')
			.targetEvent(ev)
			.ok('Delete')
			.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				EmployeeTestService.remove({id:vm.selectedTest[0].employee.id, testId:vm.selectedTest[0].test.id}, function() {
					vm.testsOnCurrentPage.splice(vm.testsOnCurrentPage.indexOf(vm.selectedTest[0]), 1);
					vm.allTests.splice(vm.allTests.indexOf(vm.selectedTest[0]), 1);
					vm.editOrDelete = true;
					vm.showToast('Test ' + vm.selectedTest[0].test.name + ' successfully deleted', 5000);
				}, function() {
					vm.showToast('Test ' + vm.selectedTest[0].test.name 
							+ ' could not be deleted ', 5000);
				});
			}, function() {
				console.log('Keep this one ...');
			});
		}
		
		function loadAllLicenses(){
			NurseLicenseService.query({id: $stateParams.id}, function(data){
				vm.allLicenses = data;
				vm.licensesOnCurrentPage = vm.sliceArray(vm.allLicenses, vm.licenseQuery);
			}, function() {
				console.log('Error status : ' + status);
			});
		}
		
		function loadAllTests(){
			EmployeeTestService.query({id: $stateParams.id}, function(data){
				vm.allTests = data;
				vm.testsOnCurrentPage = vm.sliceArray(vm.allTests, vm.testQuery);
			});
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
		function onLicenseTablePaginate(){
			vm.licensesOnCurrentPage = vm.sliceArray(vm.allLicenses, vm.licenseQuery);
		}
		
		function sliceArray(list, paginationObj){
			return list.slice(
					5 * (paginationObj - 1), (paginationObj.limit * paginationObj.page));
		}
		
		function getSelectedNurse(){
			NursesService.get({id:$stateParams.id}, function(result) {
				vm.nurse = result;
			})
		}
		
	}
})();