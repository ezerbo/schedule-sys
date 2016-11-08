(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('NurseDetailsController', NurseDetailsController);
	
	NurseDetailsController.$Inject = ['$state', '$scope', '$stateParams', '$mdDialog', '$mdToast',
	                                  'NursesService','NurseLicenseService','LicenseService',
	                                  'EmployeeTestService', 'LicenseTypeService', 'NursePhoneService', '$rootScope'];
	
	function NurseDetailsController($state, $scope, $stateParams, $mdDialog, $mdToast, NursesService,
			NurseLicenseService, EmployeeTestService, LicenseService, LicenseTypeService, NursePhoneService, $rootScope){
		var vm = this;
		vm.allLicenses = null;
		vm.allTests = [];
		vm.licensesOnCurrentPage = null;
		vm.testsOnCurrentPage = null;
		vm.loadAllLicenses = loadAllLicenses;
		vm.showLicenseDelConfirm = showLicenseDelConfirm;
		vm.showTestDelConfirm = showTestDelConfirm;
		vm.getSelectedNurse = getSelectedNurse;
		vm.createOrUpdatePhoneNumber = createOrUpdatePhoneNumber;
		vm.showPhoneNumberDelConfirm = showPhoneNumberDelConfirm;
		vm.loadAllTests = loadAllTests;
		vm.cancel = cancel;
		
		vm.selectedLicense = [];
		vm.selectedTest = [];
		vm.selectedPhoneNumber = [];
		vm.editOrDelete = true;
		vm.enableTestDel = true;
		vm.showToast = showToast;

		vm.onLicenseTablePaginate = onLicenseTablePaginate;
		vm.sliceArray = sliceArray;
		vm.licenseQuery = {limit: 5, page: 1};
		vm.testQuery = {limit: 5, page: 1};
		
		loadAllLicenses();
		getSelectedNurse();
		getSelectedPhoneNumber();
		loadAllTests();
		
		$scope.$watchCollection('vm.selectedLicense', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selectedLicense.length === 0) ? true : false;
		});
		
		$scope.$watchCollection('vm.selectedTest', function(oldValue, newValue) {
			vm.enableTestDel = (vm.selectedTest.length === 0) ? true : false;
		});
		
		$scope.$watchCollection('vm.selectedPhoneNumber', function(oldValue, newValue) {
			if(vm.selectedPhoneNumber.length === 0 || vm.selectedPhoneNumber[0].numberLabel == "PRIMARY"){
				vm.enablePhoneNumberDel = true;
			}else {
				vm.enablePhoneNumberDel = false;
			}
			vm.enablePhoneNumberEdit = (vm.selectedPhoneNumber.length === 0) ? true : false;
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
		
		function showPhoneNumberDelConfirm(ev) {
			var confirm = $mdDialog.confirm()
			.title('Delete Phone-Number')
			.textContent('Are you sure you want to delete this Phone-Number ?')
			.ariaLabel('Phone-Number deletion')
			.targetEvent(ev)
			.ok('Delete')
			.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				NursePhoneService.remove({id: vm.nurse.id, phoneNumberId: vm.selectedPhoneNumber[0].id},
						function(){
							$state.go($state.current, {}, {reload: true});
							vm.showToast('Phone-Number ' + vm.deleteNumber.number + ' successfully deleted', 5000);
						},
						function(result){
							vm.showToast(result.data, 5000);
						}
				);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function createOrUpdatePhoneNumber(){
			console.log('Something is happening');
			if(angular.isUndefined(vm.phoneNumber.id)){
				NursePhoneService.save({id: $stateParams.id}, vm.phoneNumber, function(){
					vm.showToast('Phone number ' + vm.phoneNumber.number + 'successfully added', 5000);
					vm.cancel();
					$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}else{
				NursePhoneService.update({id: $stateParams.id, phoneNumberId: vm.phoneNumber.id}, vm.phoneNumber,
						function(){
					vm.showToast('Phone number ' + vm.phoneNumber.number + 'successfully updated', 5000);
					vm.cancel();
					$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}
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
		
		function getSelectedPhoneNumber(){
			if(angular.isDefined($stateParams.phoneNumberId)){
				console.log('phoneNumber : ' + $stateParams.phoneNumberId);
				NursePhoneService.get({id:$stateParams.id, phoneNumberId:$stateParams.phoneNumberId}, function(data) {
					vm.phoneNumber = data;
				});
			}
		}
		
		function cancel(){
			$mdDialog.cancel();
			$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: false})
		}
		
	}
})();