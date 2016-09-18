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
		
		vm.nurses = [];
		vm.shifts = [];
		vm.statuses = [];
		vm.postStatuses = [];
		vm.employees = [];
		vm.facilities = [];
		vm.careGivers = [];
		vm.isEditing = false;
		vm.searchText = null;
		vm.selectedItem = null;
		vm.selectedSchedule = null;
		vm.cancel = cancel;
		vm.showToast = showToast;
		vm.querySearch = querySearch;
		vm.getAllShifts = getAllShifts;
		vm.buildSchedule = buildSchedule;
		vm.getAllFaclities = getAllFaclities;
		vm.getAllEmployees = getAllEmployees;
		vm.createOrUpdateSchedule = createOrUpdateSchedule;
		vm.getAllScheduleStatuses = getAllScheduleStatuses;
		vm.getAllSchedulePostStatuses = getAllSchedulePostStatuses;
		
		vm.schedule = {
			id: null,
			employeeId: null,
			facilityId: null,
			shiftId: null,
			comment: null,
			scheduleStatusId: null,
			schedulePostStatusId: null,
			scheduleDate: null
		};
		
		getAllShifts();
		getAllFaclities();
		getAllEmployees();
		getAllScheduleStatuses();
		getAllSchedulePostStatuses();
		getSelectedSchedule();
		
		function getSelectedSchedule(){
			if(angular.isDefined($stateParams.scheduleId)){
				vm.isEditing = true;
				ScheduleService.get({id: $stateParams.scheduleId}, function(result){
					vm.selectedSchedule = result;
					vm.schedule = vm.buildSchedule(vm.selectedSchedule);
					vm.selectedItem = (vm.selectedSchedule.employee === null) ? null : {
									employeeId: vm.selectedSchedule.employee.id,
									employeeName: vm.selectedSchedule.employee.firstName + ' ' + vm.selectedSchedule.employee.lastName
								}
				});
			}
		}
		
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
		
		function getAllSchedulePostStatuses(){
			SchedulePostStatusService.query({}, function(result){
				vm.postStatuses = result;
			});
		}
		
		function getAllShifts() {
			ShiftService.query({}, function(result) {
				vm.shifts = result;
			});
		}
		
		function createOrUpdateSchedule(){
			vm.schedule.facilityId = $stateParams.id;
			if(vm.schedule.id === null){
				vm.schedule.employeeId = (vm.selectedItem === null) ? null : vm.selectedItem.employeeId;
				ScheduleService.save(vm.schedule, function() {
					vm.showToast("Schedule successfully created", 5000);
					vm.cancel();
				}, function(result) {
					vm.showToast(result.data, 5000);
				});
			}else{
				vm.schedule.employeeId = (vm.selectedItem === null) 
						? null : vm.selectedItem.employeeId;
				ScheduleService.update({id:$stateParams.scheduleId}, vm.schedule,
						function() {
							vm.showToast("Schedule successfully updated", 5000);
							vm.cancel();
						},
						function(result){
							vm.showToast(result.data, 5000);
						});
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
				var lowercaseEmployeeName = angular.lowercase(employee.employeeName);
				return (lowercaseEmployeeName.indexOf(lowercaseQuery) === 0);
				//return (employee.employeeName.indexOf(query) === 0);
			};
		}
		
		function querySearch (query) {
			return query ? vm.employees.filter( createFilterFor(query) ) : vm.employees;
		}
		
		function buildSchedule(selectedSchedule){
			return {
				id:selectedSchedule.id,
				employeeId: (selectedSchedule.employee === null) ? null : selectedSchedule.employee.id,
				facilityId: selectedSchedule.facility.id,
				shiftId: selectedSchedule.shift.id,
				comment: selectedSchedule.scheduleComment,
				scheduleStatusId: selectedSchedule.scheduleStatus.id,
				schedulePostStatusId: selectedSchedule.schedulePostStatus.id,
				scheduleDate: new Date(moment(selectedSchedule.scheduleDate).format('MM/DD/YYYY')),
				timesheetReceived: selectedSchedule.timesheetReceived,
				hours: selectedSchedule.hours,
				overtime: selectedSchedule.overtime
			}
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
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
	}
	
})();