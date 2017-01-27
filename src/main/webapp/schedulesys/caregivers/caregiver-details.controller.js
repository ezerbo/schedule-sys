(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('careGiverDetailsController', careGiverDetailsController);
	
	careGiverDetailsController.$Inject = ['$scope', '$rootScope', '$state', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'careGiversService', 'EmployeeTestService', 'CareGiverPhoneService'];
	
	function careGiverDetailsController($scope, $rootScope, $state, $stateParams,
			$mdDialog, $mdToast, careGiversService, EmployeeTestService, CareGiverPhoneService){
		var vm = this;
		
		vm.enableTestDel = true;
		vm.sliceArray = sliceArray;
		vm.loadAllTests = loadAllTests;
		vm.showTestDelConfirm = showTestDelConfirm;
		vm.getSelectedcareGiver = getSelectedcareGiver;
		
		vm.selectedPhoneNumber = [];
		
		vm.selectedTest = [];
		vm.testQuery = {limit: 5, page: 1};
		
		vm.showToast = showToast;
		
		vm.cancel = cancel;
		vm.showPhoneNumberDelConfirm = showPhoneNumberDelConfirm;
		vm.createOrUpdatePhoneNumber = createOrUpdatePhoneNumber;
		vm.getSelectedPhoneNumber = getSelectedPhoneNumber;
		
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
		
		loadAllTests();
		getSelectedcareGiver();
		getSelectedPhoneNumber();
		
		function showPhoneNumberDelConfirm(ev) {
			var confirm = $mdDialog.confirm()
			.title('Delete Phone-Number')
			.textContent('Are you sure you want to delete this Phone-Number ?')
			.ariaLabel('Phone-Number deletion')
			.targetEvent(ev)
			.ok('Delete')
			.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				CareGiverPhoneService.remove({id: vm.careGiver.id, phoneNumberId: vm.selectedPhoneNumber[0].id},
						function(){
							$state.go($state.current, {}, {reload: true});
							vm.showToast('Phone-Number successfully deleted', 5000);
						},
						function(result){
							vm.showToast(result.data, 5000);
						}
				);
			}, function() {
			});
		};
		
		function createOrUpdatePhoneNumber(){
			if(angular.isUndefined(vm.phoneNumber.id)){
				CareGiverPhoneService.save({id: $stateParams.id}, vm.phoneNumber, function(){
					vm.showToast('Phone number successfully added', 5000);
					vm.cancel();
					$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}else{
				CareGiverPhoneService.update({id: $stateParams.id, phoneNumberId: vm.phoneNumber.id}, vm.phoneNumber,
						function(){
					vm.showToast('Phone number ' + vm.phoneNumber.number + 'successfully updated', 5000);
					vm.cancel();
					$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: true});
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}
		}
		
		function getSelectedcareGiver(){
			careGiversService.get({id:$stateParams.id}, function(result) {
				vm.careGiver = result;
			})
		}
		
		function loadAllTests(){
			EmployeeTestService.query({id: $stateParams.id}, function(data){
				vm.allTests = data;
				vm.testsOnCurrentPage = vm.sliceArray(vm.allTests, vm.testQuery);
			});
		}
		
		function sliceArray(list, paginationObj){
			return list.slice(
					5 * (paginationObj - 1), (paginationObj.limit * paginationObj.page));
		}
		
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
			});
		}
		
		function getSelectedPhoneNumber(){
			if(angular.isDefined($stateParams.phoneNumberId)){
				CareGiverPhoneService.get({id:$stateParams.id, phoneNumberId:$stateParams.phoneNumberId}, function(data) {
					vm.phoneNumber = data;
				});
			}
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
		function cancel(){
			$mdDialog.cancel();
			$state.go($rootScope.previousState.name, {id: $rootScope.previousStateParams.id}, {reload: false})
		}
		
	}
	
})();