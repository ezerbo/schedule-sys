(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('FacilityDialogController', FacilityDialogController);
	
	FacilityDialogController.$Inject = ['$state','$scope', '$stateParams', '$mdDialog', '$mdToast', 'FacilitiesService'];
	
	function FacilityDialogController($state, $scope, $stateParams, $mdDialog, $mdToast, FacilitiesService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdatefacility = createOrUpdatefacility;
		vm.showToast = showToast;
		vm.getSelectedFacility = getSelectedFacility;
		vm.facility = {
				id: null,
				name: null,
				phoneNumber: null,
				fax: null
		};
		
		getSelectedFacility();
		
		function getSelectedFacility(){
			if(angular.isDefined($stateParams.id)){
				FacilitiesService.get({id : $stateParams.id}, function(result){
					vm.facility = result;
				});
			}
		}
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdatefacility(){
			if(vm.facility.id === null){
				FacilitiesService.save(vm.facility, onCreateSucess, onCreateFailure);
			}else{
				FacilitiesService.update({id : $stateParams.id}, vm.facility, onUpdateSucess, onUpdateFailure);
			}
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			vm.showToast('Facility ' + vm.facility.name + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			$state.go('home.facilities',{}, {reload: true});
			vm.showToast('Facility ' + vm.facility.name + ' successfully updated', 5000);
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