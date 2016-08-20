(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('careGiverDetailsController', careGiverDetailsController);
	
	careGiverDetailsController.$Inject = ['$state', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'careGiversService'];
	
	function careGiverDetailsController($state, $stateParams, $mdDialog, $mdToast, careGiversService){
		var vm = this;
		
		vm.getSelectedcareGiver = getSelectedcareGiver;
		
		vm.selected = [];
		vm.query = {
				limit: 5,
				page: 1	
		};
		
		getSelectedcareGiver();
		
		function getSelectedcareGiver(){
			careGiversService.get({id:$stateParams.id}, function(result) {
				vm.careGiver = result;
			})
		}
	}
	
})();