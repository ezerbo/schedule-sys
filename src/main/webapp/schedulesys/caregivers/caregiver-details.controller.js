(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('careGiverDetailsController', careGiverDetailsController);
	
	careGiverDetailsController.$Inject = ['$scope','$state', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'careGiversService', 'EmployeeTestService'];
	
	function careGiverDetailsController($scope, $state, $stateParams, $mdDialog, $mdToast, careGiversService, EmployeeTestService){
		var vm = this;
		
		vm.enableTestDel = true;
		vm.sliceArray = sliceArray;
		vm.loadAllTests = loadAllTests;
		vm.showTestDelConfirm = showTestDelConfirm;
		vm.getSelectedcareGiver = getSelectedcareGiver;
		
		vm.selectedTest = [];
		vm.testQuery = {limit: 5, page: 1};
		
		$scope.$watchCollection('vm.selectedTest', function(oldValue, newValue) {
			vm.enableTestDel = (vm.selectedTest.length === 0) ? true : false;
		});
		
		loadAllTests();
		getSelectedcareGiver();
		
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
				console.log('Keep this one ...');
			});
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
	}
	
})();