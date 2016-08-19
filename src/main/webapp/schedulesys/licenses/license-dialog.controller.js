(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('LicenseDialogController', LicenseDialogController);
	
	LicenseDialogController.$Inject = ['$state', '$stateParams', '$mdDialog', '$mdToast','NurseLicenseService', 'LicenseService'];
	
	function LicenseDialogController($state, $stateParams, $mdDialog, $mdToast, NurseLicenseService, LicenseService){
		var vm = this;
		
		vm.license = {
				id: null,
				nurseId: $stateParams.nurseId,
				expirationDate: null,
				number: null
				};
		
		vm.cancel = cancel;
		vm.showToast = showToast;
		vm.createOrUpdateLicense = createOrUpdateLicense;
		
		function cancel() {
			$mdDialog.cancel();
		}
		//TODO use moment.js
		if(angular.isDefined($stateParams.licenseId)){
			LicenseService.get({id : $stateParams.licenseId}, function(result){
				vm.license = result;
				var x = vm.license.expirationDate.split("-");
				var y = (x[1] + '/' + x[2] + '/' + x[0]);
				vm.license.expirationDate = new Date(y);
			});
		}
		
		function createOrUpdateLicense(){
			if(vm.license.id === null){
				NurseLicenseService.save({id:$stateParams.id}, vm.license, function(){
					vm.showToast("License successfully created", 5000);
					$mdDialog.cancel();
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}else{
				NurseLicenseService.update({id:$stateParams.id, licenseId: $stateParams.licenseId},
						vm.license, function(result) {
							vm.showToast("License successfully updated", 5000);
							$mdDialog.cancel();
						}, function(result) {
							vm.showToast(result.data, 5000);
						});
			}
		}
		//TODO move this to common.js
		function showToast(textContent, delay){
			$mdToast.show($mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
	}
	
})();