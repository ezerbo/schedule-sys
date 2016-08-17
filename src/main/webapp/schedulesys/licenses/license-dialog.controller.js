(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('LicenseDialogController', LicenseDialogController);
	
	LicenseDialogController.$Inject = ['$state','$stateParams', '$mdDialog', '$mdToast','LicenseService'];
	
	function LicenseDialogController($state,$stateParams, $mdDialog, $mdToast, LicenseService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdateLicense = createOrUpdateLicense;
		vm.showToast = showToast;
		vm.getSelectedLicense = getSelectedLicense;
		
		
		
		
		vm.myModel = {};
		vm.license = {
				id: null,
				nurseId: $stateParams.nurseId,
				expirationDate: null,
				number: null
			
		};
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		if(angular.isDefined($stateParams.id)){
			vm.getSelectedLicense();
		}
		
		
		
		function getSelectedLicense(){
			LicenseService.get({id : $stateParams.id},function(result){
				vm.license = result;
				var x = vm.license.expirationDate.split("-");
				var y = (x[1] + '/' + x[2] + '/' + x[0]);
				vm.license.expirationDate = new Date(y);
				
			});
		}
		function createOrUpdateLicense(){
			console.log('License to be created : ' + angular.toJson(vm.license));
			if(vm.license.id === null){
				LicenseService.save({nurseId: $stateParams.nurseId},vm.license, onCreateSucess, onCreateFailure);
			}else{
				LicenseService.update({id:$stateParams.id,nurseId: $stateParams.nurseId},vm.license, onUpdateSucess, onUpdateFailure);
			}
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('License ' + vm.license.number + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('License ' + vm.license.number + ' successfully updated', 5000);
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