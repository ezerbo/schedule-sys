(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('PrivateCareSchedulingDialogController', PrivateCareSchedulingDialogController);
	
	PrivateCareSchedulingDialogController.$Inject = ['$state', '$scope', '$stateParams', '$mdDialog', '$mdToast', 'PrivateCareShiftService',
	                                              'PrivateCareService', 'PrivateCareScheduleService', 'ScheduleStatusService',
	                                               'EmployeesService'];
	
	function PrivateCareSchedulingDialogController($state, $q, $scope, $stateParams, $mdDialog, PrivateCareShiftService, 
			$mdToast, PrivateCareService, PrivateCareScheduleService, ScheduleStatusService
			,EmployeesService){
		var vm = this;
		
		vm.shifts = [];
		vm.statuses = [];
		vm.employees = [];
		vm.privateCares = [];
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
		vm.getAllPrivateCares = getAllPrivateCares;
		vm.getAllEmployees = getAllEmployees;
		vm.createOrUpdateSchedule = createOrUpdateSchedule;
		vm.getAllScheduleStatuses = getAllScheduleStatuses;
		
		vm.schedule = {
			id: null,
			employeeId: null,
			privateCareId: null,
			shiftId: null,
			comment: null,
			scheduleStatusId: null,
			scheduleDate: null
		};
		
		getAllShifts();
		getAllPrivateCares();
		getAllEmployees();
		getAllScheduleStatuses();
		getSelectedSchedule();
		
		function getSelectedSchedule(){
			if(angular.isDefined($stateParams.scheduleId)){
				vm.isEditing = true;
				PrivateCareScheduleService.get({id: $stateParams.scheduleId}, function(result){
					vm.selectedSchedule = result;
					vm.schedule = vm.buildSchedule(vm.selectedSchedule);
					vm.selectedItem = (vm.selectedSchedule.employee === null) ? null : {
									employeeId: vm.selectedSchedule.employee.id,
									employeeName: vm.selectedSchedule.employee.firstName + ' ' + vm.selectedSchedule.employee.lastName
								}
				});
			}
		}
		
		function getAllPrivateCares() {
			PrivateCareService.query({}, function(result) {
				vm.privateCares = result;
			});
		}
		
		function getAllScheduleStatuses() {
			ScheduleStatusService.query({}, function(result) {
				vm.statuses = result;
			});
		}
		
		function getAllShifts() {
			PrivateCareShiftService.query({}, function(result) {
				vm.shifts = result;
			});
		}
		
		function createOrUpdateSchedule(){
			vm.schedule.privateCareId = $stateParams.id;
			if(vm.schedule.id === null){
				vm.schedule.employeeId = (vm.selectedItem === null) ? null : vm.selectedItem.employeeId;
				PrivateCareScheduleService.save(vm.schedule, function() {
					vm.showToast("Schedule successfully created", 5000);
					vm.cancel();
				}, function(result) {
					vm.showToast(result.data, 5000);
				});
			}else{
				vm.schedule.employeeId = (vm.selectedItem === null) 
						? null : vm.selectedItem.employeeId;
				PrivateCareScheduleService.update({id:$stateParams.scheduleId}, vm.schedule,
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
			EmployeesService.query(function(result) {
				if(typeof result !== 'undefined' && result.length > 0){
					var employees = result.map(function(nurse){
						return {
							employeeId: nurse.id,
							employeeName: nurse.firstName + ' ' + nurse.lastName
						}
					});
					vm.employees = vm.employees.concat(employees);
				}
			});
		}
		
		function createFilterFor(query) {
			var lowercaseQuery = angular.lowercase(query);
			return function filterFn(employee) {
				var lowercaseEmployeeName = angular.lowercase(employee.employeeName);
				return (lowercaseEmployeeName.indexOf(lowercaseQuery) === 0);
			};
		}
		
		function querySearch (query) {
			return query ? vm.employees.filter( createFilterFor(query) ) : vm.employees;
		}
		
		function buildSchedule(selectedSchedule){
			return {
				id:selectedSchedule.id,
				employeeId: (selectedSchedule.employee === null) ? null : selectedSchedule.employee.id,
				privateCareId: selectedSchedule.privateCare.id,
				shiftId: selectedSchedule.shift.id,
				comment: selectedSchedule.scheduleComment,
				scheduleStatusId: selectedSchedule.scheduleStatus.id,
				scheduleDate: new Date(moment(selectedSchedule.scheduleDate).format('MM/DD/YYYY')),
				timesheetReceived: selectedSchedule.timesheetReceived,
				paid: selectedSchedule.paid,
				billed: selectedSchedule.billed
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