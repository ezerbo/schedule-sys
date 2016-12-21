(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('careGiverDialogController', careGiverDialogController);
	
	careGiverDialogController.$Inject = ['$state','$stateParams', '$mdDialog', '$mdToast', 'careGiversService','careGiversPositionTypeService','PhoneLabelService','PhoneTypeService','CareGiverPhoneService'];
	
	function careGiverDialogController($state,$stateParams, $mdDialog, $mdToast, careGiversService,careGiversPositionTypeService,PhoneLabelService,PhoneTypeService,CareGiverPhoneService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdatecareGiver = createOrUpdatecareGiver;
		
		vm.positions = careGiversPositionTypeService.query();
		vm.phoneNumberTypes = PhoneTypeService.query();
		
		vm.showToast = showToast;
		vm.careGiverphoneType = PhoneTypeService.query();
		vm.careGiverphoneLabel = PhoneLabelService.query();
		vm.getSelectedcareGiver = getSelectedcareGiver;
		
		init();
		
		function PhoneNumber (numberLabel, numberType, number) {
			this.numberLabel =  numberLabel;
			this.numberType = numberType;
			this.number = number;
		}
		
		function init(){
			if(angular.isDefined($stateParams.id)){
				vm.getSelectedcareGiver();
			}else{
				vm.careGiver = {};
				vm.careGiver.phoneNumbers = [];
				vm.primaryPhoneNumber = new PhoneNumber("PRIMARY", null, null);
				vm.secondaryPhoneNumber = new PhoneNumber("SECONDARY", null, null);
				vm.otherPhoneNumber = new PhoneNumber("OTHER", null, null);
			}
		}
		
		if(angular.isDefined($stateParams.id)){
			vm.getSelectedcareGiver();
		}
		
		function getSelectedcareGiver(){
			careGiversService.get({id : $stateParams.id},function(result){
				vm.careGiver = result;
				
				var x = vm.careGiver.dateOfHire.split("-");
				var y = (x[1] + '/' + x[2] + '/' + x[0]);
				vm.careGiver.dateOfHire = new Date(y);

				var a = vm.careGiver.rehireDate.split("-");
				var b = (a[1] + '/' + a[2] + '/' + a[0]);
				vm.careGiver.rehireDate = new Date(b);
				
				var c = vm.careGiver.lastDayOfWork.split("-");
				var d = (c[1] + '/' + c[2] + '/' + c[0]);
				vm.careGiver.lastDayOfWork = new Date(d);
			});
		}
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdatecareGiver(){
			if(angular.isUndefined(vm.careGiver.id)){
				vm.careGiver.phoneNumbers.push(vm.primaryPhoneNumber, vm.secondaryPhoneNumber, vm.otherPhoneNumber);
				console.log('Phone number : ' + angular.toJson(vm.careGiver.phoneNumbers));
				careGiversService.save(vm.careGiver, function(){
					vm.showToast('Care giver ' + vm.careGiver.firstName + ' successfully created', 5000);
					$state.go($state.current, {}, {reload: true});
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}else{
				careGiversService.update({id: $stateParams.id},vm.careGiver, function(){
					vm.showToast('Care-Giver ' + vm.careGiver.firstName + ' successfully updated', 5000);
					vm.cancel();
					$state.go($rootScope.previousState.name, {}, {reload: true});
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}
		}
		
		function showToast(textContent, delay){
			$mdToast.show($mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
		function showConfirmPhone(phone){
			vm.deleteNumber = phone;
			showConfirm();
		}
		
	}
	
})();