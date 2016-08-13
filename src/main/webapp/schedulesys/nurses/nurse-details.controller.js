(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('NurseDetailsController', NurseDetailsController);
	
	NurseDetailsController.$Inject = ['$state', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'NursesService','NurseLicenseService'];
	
	function NurseDetailsController($state, $stateParams, $mdDialog, $mdToast, NursesService,NurseLicenseService){
		var vm = this;
		
		vm.getSelectedNurse = getSelectedNurse;
		vm.getLicenses = getLicenses;
		vm.allLicenses = [];
		vm.selected = [];
		vm.query = {
				limit: 5,
				page: 1	
		};
		
		
		getSelectedNurse();
		getLicenses();
		
		function getSelectedNurse(){
			NursesService.get({id:$stateParams.id}, function(result) {
				vm.nurse = result;
			})
		}
		
		function getLicenses(){
			NurseLicenseService.query({id:$stateParams.id}, function(result) {
				vm.allLicenses = result;
			})
		}
	}
})();