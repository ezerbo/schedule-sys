(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('FacilityDetailsController', FacilityDetailsController);
	
	FacilityDetailsController.$Inject = ['$state', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'FacilitiesService', 'FacilitiesStaffMemberService'];
	
	function FacilityDetailsController($state, $stateParams, $mdDialog, $mdToast, FacilitiesService, FacilitiesStaffMemberService){
		var vm = this;
		vm.getStaffMembers = getStaffMembers;
		vm.getSelectedFacility = getSelectedFacility;
		vm.allStaffMembers = [];
		vm.selected = [];
		vm.query = {
				limit: 5,
				page: 1	
		};
		
		getStaffMembers();
		getSelectedFacility();
		
		function getSelectedFacility(){
			FacilitiesService.get({id:$stateParams.id}, function(result) {
				vm.facility = result;
			})
		}
		
		function getStaffMembers(){
			FacilitiesStaffMemberService.query({id:$stateParams.id}, function(result) {
				vm.allStaffMembers = result;
			})
		}
	}
})();