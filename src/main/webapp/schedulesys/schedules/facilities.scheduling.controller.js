(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('FacilitySchedulingController', FacilitySchedulingController);
	
	FacilitySchedulingController.$Inject = ['$state', '$stateParams', '$mdDialog',
	                                        '$mdToast', 'FacilitiesSchedulingService', 'FacilitiesService'];
	
	function FacilitySchedulingController($state, $stateParams, $mdDialog, $mdToast, FacilitiesSchedulingService, FacilitiesService){
		var vm = this;
		vm.allSchedules = null;
		vm.getAllSchedules = getAllSchedules;
		vm.getCurrentfacility = getCurrentfacility;
		vm.schedules = null;
		vm.selected = [];
		vm.query = {
				limit: 10,
				page: 1
		};
		
		getAllSchedules();
		getCurrentfacility();
		
		function getAllSchedules(){
			FacilitiesSchedulingService.query({id:$stateParams.id}, function(result) {
				vm.allSchedules = result;
				console.log('schedules : ' + angular.toJson(result));
			})
		}
		
		function getCurrentfacility(){
			FacilitiesService.get({id:$stateParams.id}, function(result) {
				vm.facility = result;
			})
		}
		
	}
	
})();