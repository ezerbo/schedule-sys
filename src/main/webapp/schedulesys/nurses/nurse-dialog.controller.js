(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('NurseDialogController', NurseDialogController);
	
	NurseDialogController.$Inject = ['$state', '$mdDialog', '$mdToast', 'NursesService','NursesPositionTypeService','PhoneLabelService','PhoneTypeService'];
	
	function NurseDialogController($state, $mdDialog, $mdToast, NursesService,NursesPositionTypeService,PhoneLabelService,PhoneTypeService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdatenurse = createOrUpdatenurse;
		vm.showToast = showToast;
		vm.nursepositionType = NursesPositionTypeService.query();
		vm.options2 = vm.nursepositionType;
		vm.nursephoneType = PhoneTypeService.query();
		vm.options3 = vm.nursephoneType;
		vm.nursephoneLabel = PhoneLabelService.query();
		vm.options4 = vm.nursephoneLabel;
		console.log(vm.options2);
		console.log(vm.options3);
		console.log(vm.options4);
		vm.showPhone = function(){
			
			vm.displayPhone = ! vm.displayPhone;
		}
		vm.myModel = {};
		vm.nurse = {
				id: null,
				firstName: null,
				lastName: null,
				positionName: null,
				ebc: null,
				cpr: null,
				dateOfHire: null,
				rehireDate: null,
				lastDayOfWork: null,
				comment: null,
				phoneNumbers: [
			      {
			        
			        number: null ,
			        numberLabel: null,
			        numberType: null
			      }
			    ]
			
		};
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdatenurse(){
			console.log('nurse to be created : ' + angular.toJson(vm.nurse));
			if(vm.nurse.id === null){
				NursesService.save(vm.nurse, onCreateOrUpdateSucess, onCreateOrUpdateFailure);
			}else{
				NursesService.update(vm.nurse, onCreateOrUpdateSucess, onCreateOrUpdateFailure);
			}
		}
		
		function onCreateOrUpdateSucess(result){
			$state.go('home.nurses',{}, {reload: true});
			$mdDialog.cancel();
			vm.showToast('Nurse ' + vm.nurse.firstName + ' successfully created', 5000);
		}
		
		function onCreateOrUpdateFailure(result){
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