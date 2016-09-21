(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('PrivateCareDialogController', PrivateCareDialogController);
	
	PrivateCareDialogController.$Inject = ['$state','$scope', '$stateParams', '$mdDialog', '$mdToast', 'PrivateCareService'];
	
	function PrivateCareDialogController($state, $scope, $stateParams, $mdDialog, $mdToast, PrivateCareService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdate = createOrUpdate;
		vm.showToast = showToast;
		vm.getSelectedPrivateCare = getSelectedPrivateCare;
		
		vm.privateCare = {
				id: null,
				name: null,
				phoneNumber: null,
				fax: null
		};
		
		getSelectedPrivateCare();
		
		function getSelectedPrivateCare(){
			if(angular.isDefined($stateParams.id)){
				PrivateCareService.get({id : $stateParams.id}, function(result){
					vm.privateCare = result;
				});
			}
		}
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdate(){
			if(vm.privateCare.id === null){
				PrivateCareService.save(vm.privateCare, onCreateSucess, onCreateFailure);
			}else{
				PrivateCareService.update({id : $stateParams.id}, vm.privateCare, onUpdateSucess, onUpdateFailure);
			}
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			vm.showToast('Private care ' + vm.privateCare.name + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			vm.showToast('Private ' + vm.privateCare.name + ' successfully updated', 5000);
		}
		
		function onUpdateFailure(result){
			vm.showToast(result.data, 5000);
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