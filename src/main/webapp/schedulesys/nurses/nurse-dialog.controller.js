(function(){
	'use strict';
	angular
	.module('scheduleSys')
	.controller('NurseDialogController', NurseDialogController);

	NurseDialogController.$Inject = ['$state','$scope','$stateParams', '$mdDialog',
	                                 '$mdToast', 'NursesService','NursesPositionTypeService',
	                                 'PhoneTypeService'];

	function NurseDialogController($state, $scope, $stateParams, $mdDialog, $mdToast,
			NursesService, NursesPositionTypeService, PhoneTypeService){
		var vm = this;
		
		vm.createOrUpdatenurse = createOrUpdatenurse;
		vm.init = init;
		vm.showToast = showToast;
		
		vm.getSelectedNurse = getSelectedNurse;
		
		vm.positions = NursesPositionTypeService.query();
		vm.phoneNumberTypes = PhoneTypeService.query();
		
		init();
		
		function PhoneNumber (numberLabel, numberType, number) {
			this.numberLabel =  numberLabel;
			this.numberType = numberType;
			this.number = number;
		}
		
		function init(){
			if(angular.isDefined($stateParams.id)){
				vm.getSelectedNurse();
			}else{
				vm.nurse = {};
				vm.nurse.phoneNumbers = [];
				vm.primaryPhoneNumber = new PhoneNumber("PRIMARY", null, null);
				vm.secondaryPhoneNumber = new PhoneNumber("SECONDARY", null, null);
				vm.otherPhoneNumber = new PhoneNumber("OTHER", null, null);
			}
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

		function createOrUpdatenurse(){
			console.log('nurse to be created : ' + angular.toJson(vm.nurse));
			if(angular.isUndefined(vm.nurse.id)){
				vm.nurse.phoneNumbers.push(vm.primaryPhoneNumber, vm.secondaryPhoneNumber, vm.otherPhoneNumber);
				NursesService.save(vm.nurse, function(){
					vm.showToast('Nurse ' + vm.nurse.firstName + ' successfully created', 5000);
					$state.go($state.current, {}, {reload: true});
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}else{
				NursesService.update({id:$stateParams.id},vm.nurse, function(){
					vm.showToast('Nurse ' + vm.nurse.firstName + ' successfully updated', 5000);
					$state.go($state.current, {}, {reload: true});
				}, function(result){
					vm.showToast(result.data, 5000);
				});
			}
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