(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('FacilitySchedulingDialogController', FacilitySchedulingDialogController);
	
	FacilitySchedulingDialogController.$Inject = ['$state', '$scope', '$stateParams', '$mdDialog', '$mdToast', 'ShiftService',
	                                              'FacilitiesService', 'ScheduleService', 'ScheduleStatusService', 'SchedulePostStatusService'
	                                              , 'NursesService', 'careGiversService'];
	
	function FacilitySchedulingDialogController($state, $q, $scope, $stateParams, $mdDialog, ShiftService, 
			$mdToast, FacilitiesService, ScheduleService, SchedulePostStatusService, ScheduleStatusService
			,careGiversService, NursesService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.shifts = [];
		vm.statuses = [];
		vm.facilities = [];
		vm.employees = [];
		vm.careGivers = [];
		vm.nurses = [];
		vm.selectedItem  = null;
		vm.searchText    = null;
		vm.querySearch   = querySearch;
		vm.getAllShifts = getAllShifts;
		vm.getAllFaclities = getAllFaclities;
		vm.getAllEmployees = getAllEmployees;
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
		getAllEmployees();
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
			vm.schedule.employeeId = vm.selectedItem.employeeId;
			if(vm.schedule.id === null){
				console.log("New Schedule");
				ScheduleService.save(vm.schedule, onScheduleSaveSuccess, onScheduleSaveFailure);
				
			}
		}
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function getAllEmployees(){
			
			 NursesService.query(function(result) {
				if(typeof result !== 'undefined' && result.length > 0){
					vm.nurses = result.map(function(nurse){
						return {
							employeeId: nurse.id,
							employeeName: nurse.firstName + ' ' + nurse.lastName
						}
					});
					vm.employees = vm.employees.concat(vm.nurses);
				}
			});
			
			careGiversService.query(function(result) {
				if(typeof result !== 'undefined' && result.length > 0){
					vm.careGivers = result.map(function(careGiver){
						return {
							employeeId: careGiver.id,
							employeeName: careGiver.firstName + ' ' + careGiver.lastName
						}
					});
					vm.employees = vm.employees.concat(vm.careGivers);
				}
			});
		}
		
		function createFilterFor(query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(employee) {
				return (employee.employeeName.indexOf(query) === 0);
			};
		}
		
		function querySearch (query) {
			return query ? vm.employees.filter( createFilterFor(query) ) : vm.employees;
		}
		
		function onScheduleSaveSuccess(){
			
			vm.cancel();
			$state.go('home.facilities-scheduling', {}, {reload:true});
		}
		
		function onScheduleUpdateSuccess(){
			
		}
		
		function onScheduleSaveFailure(){
			console.log('Error while creating new Schedule');
		}
	}
	
})();