(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('NurseDialogController', NurseDialogController);
	
	NurseDialogController.$Inject = ['$state','$scope','$stateParams', '$mdDialog', '$mdToast', 'NursesService','NursesPositionTypeService','PhoneLabelService','PhoneTypeService'];
	
	function NurseDialogController($state,$scope,$stateParams, $mdDialog, $mdToast, NursesService,NursesPositionTypeService,PhoneLabelService,PhoneTypeService){
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
		vm.getSelectedNurse = getSelectedNurse;
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
		
		
		
		if(angular.isDefined($stateParams.id)){
			vm.getSelectedNurse();
		}
		
		
		
		function getSelectedNurse(){
			NursesService.get({id : $stateParams.id},function(result){
				vm.nurse = result;
				
				var x = vm.nurse.dateOfHire.split("-");
				var y = (x[1] + '/' + x[2] + '/' + x[0]);
				vm.nurse.dateOfHire = new Date(y);
				
					
					var a = vm.nurse.rehireDate.split("-");
					var b = (a[1] + '/' + a[2] + '/' + a[0]);
					vm.nurse.rehireDate = new Date(b);
					
				
      
					
					var c = vm.nurse.lastDayOfWork.split("-");
					var d = (c[1] + '/' + c[2] + '/' + c[0]);
					vm.nurse.lastDayOfWork = new Date(d);
					
				
				
			});
		}
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdatenurse(){
			console.log('nurse to be created : ' + angular.toJson(vm.nurse));
			if(vm.nurse.id === null){
				NursesService.save(vm.nurse, onCreateSucess, onCreateFailure);
			}else{
				NursesService.update({id:$stateParams.id},vm.nurse, onUpdateSucess, onUpdateFailure);
			}
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('Nurse ' + vm.nurse.firstName + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('Nurse ' + vm.nurse.firstName + ' successfully updated', 5000);
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