(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('FacilitySchedulingDialogController', FacilitySchedulingDialogController);
	
	FacilitySchedulingDialogController.$Inject = ['$state', '$stateParams', '$mdDialog', '$mdToast', 'ShiftService',
	                                              'FacilitiesService', 'ScheduleStatusService', 'SchedulePostStatusService'
	                                              , 'NurseService', 'CareGiverService'];
	
	function FacilitySchedulingDialogController($state, $stateParams, $mdDialog, ShiftService, 
			$mdToast, FacilitiesService, SchedulePostStatusService, ScheduleStatusService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.shifts = [];
		vm.statuses = [];
		vm.facilities = [];
		vm.employees = [];
		vm.getAllShifts = getAllShifts;
		vm.getAllFaclities = getAllFaclities;
		vm.getAllScheduleStatuses = getAllScheduleStatuses;
		vm.createOrUpdateSchedule = createOrUpdateSchedule;
		
		vm.schedule = {
			id: null,
			employeeId: null,
			facilityId: null,
			shiftId: null,
			comment: null,
			scheduleStatusId: null,
			scheduleDate: null
		};

		getAllShifts();
		getAllFaclities();
		getAllScheduleStatuses();
		
		function getAllFaclities() {
			FacilitiesService.query({}, function(result) {
				vm.facilities = result;
			});
		}
		
		function getAllScheduleStatuses() {
			ScheduleStatusService.query({}, function(result) {
				vm.statuses = result;
			});
		}
		
		function getAllShifts() {
			ShiftService.query({}, function(result) {
				vm.shifts = result;
			});
		}
		
		function createOrUpdateSchedule(){
			console.log("schedule : " + angular.toJson(vm.schedule));
			vm.cancel();
		}
		
		function cancel() {
			$mdDialog.cancel();
		}
	}
	
})();