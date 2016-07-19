(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('FacilityDialogController', FacilityDialogController);
	
	FacilityDialogController.$Inject = ['$mdDialog'];
	
	function FacilityDialogController($mdDialog){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdatefacility = createOrUpdatefacility;
		
		vm.facility = {
				name: null,
				address: null,
				phoneNumber: null,
				fax: null
		};
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdatefacility(){
			console.log('user to be created : ' + angular.toJson(vm.facility));
		}
	}
	
})();