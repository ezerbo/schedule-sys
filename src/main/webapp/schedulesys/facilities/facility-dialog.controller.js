(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('FacilityDialogController', FacilityDialogController);
	
	FacilityDialogController.$Inject = ['$state', '$mdDialog', '$mdToast', 'FacilitiesService'];
	
	function FacilityDialogController($state, $mdDialog, $mdToast, FacilitiesService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdatefacility = createOrUpdatefacility;
		vm.showToast = showToast;
		
		if(angular.isUndefined(facility)){
			vm.facility = {
					id: null,
					name: null,
					address: null,
					phoneNumber: null,
					fax: null
			};
		}else{
			vm.facility = facility;
		}
		
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdatefacility(){
			console.log('user to be created : ' + angular.toJson(vm.facility));
			if(vm.facility.id === null){
				FacilitiesService.save(vm.facility, onCreateOrUpdateSucess, onCreateOrUpdateFailure);
			}else{
				FacilitiesService.update(vm.facility, onCreateOrUpdateSucess, onCreateOrUpdateFailure);
			}
		}
		
		function onCreateOrUpdateSucess(result){
			$state.go('home.facilities',{}, {reload: true});
			$mdDialog.cancel();
			vm.showToast('Facility ' + vm.facility.name + ' successfully created', 5000);
		}
		
		function onCreateOrUpdateFailure(result){
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