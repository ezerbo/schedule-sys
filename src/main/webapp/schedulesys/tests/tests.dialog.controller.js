(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('TestDialogController', TestDialogController);
	
	TestDialogController.$Inject = ['$state','$scope', '$stateParams', '$mdDialog', '$mdToast', 'TestsService'];
	
	function TestDialogController($state, $scope, $stateParams, $mdDialog, $mdToast, TestsService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdate = createOrUpdate;
		vm.showToast = showToast;
		vm.getSelectedTest = getSelectedTest;
		vm.test = {
				id: null,
				name: null,
				allowNotApplicable: false,
				hasCompletedDate: false,
				hasExpirationDate: false
		};
		
		getSelectedTest();
		
		function getSelectedTest(){
			if(angular.isDefined($stateParams.id)){
				TestsService.get({id : $stateParams.id}, function(result){
					vm.test = result;
				});
			}
		}
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdate(){
			if(vm.test.id === null){
				TestsService.save(vm.test, function() {
					$mdDialog.cancel();
					vm.showToast('Test ' + vm.test.name + ' successfully created', 5000);
				}, function(result) {
					vm.showToast(result.data, 5000);
				});
			}else{
				TestsService.update({id : $stateParams.id}, vm.test, function() {
					$mdDialog.cancel();
					vm.showToast('Test ' + vm.test.name + ' successfully updated', 5000);
				}, function() {
					vm.showToast(result.data, 5000);
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
	}
	
})();