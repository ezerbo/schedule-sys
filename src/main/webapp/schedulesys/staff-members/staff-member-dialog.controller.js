(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('StaffMemberDialogController', StaffMemberDialogController);
	
	StaffMemberDialogController.$Inject = ['$state','$stateParams', '$mdDialog', '$mdToast','StaffMemberService','FacilitiesService'];
	
	function StaffMemberDialogController($state,$stateParams, $mdDialog, $mdToast, StaffMemberService,FacilitiesService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdateStaffMember = createOrUpdateStaffMember;
		vm.showToast = showToast;
		vm.getSelectedStaffMember = getSelectedStaffMember;
		vm.facilityName = FacilitiesService.query();
		vm.options2 = vm.facilityName;
		
		
		
		
		vm.myModel = {};
		vm.staffmember = {
				id: null,
				firstName: null,
				lastName: null,
				title: null,
				phoneNumber: null,
				fax: null,
				facilityName: null
			
		};
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		if(angular.isDefined($stateParams.id)){
			vm.getSelectedStaffMember();
		}
		
		
		
		function getSelectedStaffMember(){
			StaffMemberService.get({id : $stateParams.id},function(result){
				vm.staffmember = result;
				
			});
		}
		function createOrUpdateStaffMember(){
			console.log('Staff-Member to be created : ' + angular.toJson(vm.staffmember));
			if(vm.staffmember.id === null){
				StaffMemberService.save({facId: $stateParams.facId},vm.staffmember, onCreateSucess, onCreateFailure);
			}else{
				StaffMemberService.update({id:$stateParams.id,facId: $stateParams.facId},vm.staffmember, onUpdateSucess, onUpdateFailure);
			}
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			$state.go('home.facilities',{}, {reload: true});
			vm.showToast('Staff-Member ' + vm.staffmember.firstName + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			$state.go('home.facilities',{}, {reload: true});
			vm.showToast('Staff-Member ' + vm.staffmember.firstName + ' successfully updated', 5000);
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