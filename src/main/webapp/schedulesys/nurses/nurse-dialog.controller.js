(function(){
	'use strict';
	angular
	.module('scheduleSys')
	.controller('NurseDialogController', NurseDialogController);

	NurseDialogController.$Inject = ['$state','$scope','$stateParams', '$mdDialog', '$mdToast', 'NursesService','NursesPositionTypeService','PhoneLabelService','PhoneTypeService','NursePhoneService'];

	function NurseDialogController($state,$scope,$stateParams, $mdDialog, $mdToast, NursesService,NursesPositionTypeService,PhoneLabelService,PhoneTypeService,NursePhoneService){
		var vm = this;

		vm.cancel = cancel;
		vm.createOrUpdatenurse = createOrUpdatenurse;
		vm.createOrUpdatePhoneNum = createOrUpdatePhoneNum;
		vm.showToast = showToast;
		vm.nursepositionType = NursesPositionTypeService.query();
		vm.options2 = vm.nursepositionType;
		vm.nursephoneType = PhoneTypeService.query();
		vm.options3 = vm.nursephoneType;
		vm.nursephoneLabel = PhoneLabelService.query();
		vm.options4 = vm.nursephoneLabel;
		vm.getSelectedNurse = getSelectedNurse;
		vm.showPhoneNum = showPhoneNum;
		vm.showOtherPhoneNum = showOtherPhoneNum;
		vm.secondary = "SECONDARY";
		vm.other = "OTHER";
		vm.showConfirmPhone = showConfirmPhone;

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
				phoneNumbers: [{
					number: null ,
					numberLabel: null,
					numberType: null
				}]
		};

		vm.nurse.phoneNumbers[0].numberLabel = "PRIMARY";

		function showPhoneNum(){

			vm.displayPhone = ! vm.displayPhone;
			console.log(vm.displayPhone);
			if(vm.displayPhone){
				vm.q =  { 
						number: null,
						numberLabel: "SECONDARY",
						numberType: null
				};

				vm.nurse['phoneNumbers'].push(vm.q);
				console.log(vm.nurse);
			}else if(!vm.displayPhone){
				vm.nurse['phoneNumbers'].pop();
			}
		}

		function showOtherPhoneNum(){

			vm.displayOtherPhone = ! vm.displayOtherPhone;
			console.log(vm.displayOtherPhone);
			if(vm.displayOtherPhone){

				vm.u =  { 
						number: null,
						numberLabel: "OTHER",
						numberType: null
				};

				vm.nurse['phoneNumbers'].push(vm.u);




				console.log(vm.nurse);





			}else if(!vm.displayOtherPhone){

				vm.nurse['phoneNumbers'].pop();


			}
			console.log(vm.nurse);
		}



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
				console.log(vm.nurse);
			}
		}


		function onCreateSucess(result){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('Nurse ' + vm.nurse.firstName + ' successfully created', 5000);
		}

		function onCreatePhoneSucess(result){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('Nurse ' + vm.nurse.firstName + 'phone number successfully created', 5000);
		}

		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}

		function onUpdateSucess(result){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('Nurse ' + vm.nurse.firstName + ' successfully updated', 5000);
		}

		function onUpdatePhoneSucess(result){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('Nurse ' + vm.nurse.firstName + 'phone number successfully updated', 5000);
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

		function createOrUpdatePhoneNum(phonenumber,label){
			if(label === "SECONDARY"){

				phonenumber.numberLabel="SECONDARY";
			}else if(label === "OTHER"){

				phonenumber.numberLabel="OTHER";

			}
			vm.phoneNumbers = phonenumber;
			console.log(vm.phoneNumbers);
			if(vm.phoneNumbers.id === undefined){
				console.log('Phone-Numbers to be created : ' + angular.toJson(vm.phoneNumbers));
				NursePhoneService.save({nurseID: vm.nurse.id},vm.phoneNumbers, onCreatePhoneSucess, onCreateFailure);
			}else{
				NursePhoneService.update({nurseID: vm.nurse.id, id: vm.phoneNumbers.id},vm.phoneNumbers, onUpdatePhoneSucess, onUpdateFailure);
				console.log(vm.phoneNumbers);
			}
		}

		function showConfirmPhone(phone){

			vm.deleteNumber = phone;

			showConfirm();

		}
		function showConfirm(ev) {

			console.log(ev);


			var confirm = $mdDialog.confirm()
			.title('Delete Phone-Number')
			.textContent('Are you sure you want to delete this Phone-Number ?')
			.ariaLabel('Phone-Number deletion')
			.targetEvent(ev)
			.ok('Delete')
			.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				NursePhoneService.remove(
						{nurseID: vm.nurse.id, id: vm.deleteNumber.id},
						onDeleteSuccess,
						onDeleteFailure
				);
			}, function() {
				console.log('Keep this one ...');
			});
		};

		function onDeleteSuccess (){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('Phone-Number ' + vm.deleteNumber.number + ' successfully deleted', 5000);
		}	

		function onDeleteFailure (){
			$mdDialog.cancel();
			$state.go('home.nurses',{}, {reload: true});
			vm.showToast('Phone-Number ' + vm.deleteNumber.number 
					+ ' could not be deleted ', 5000);
		}
	}

})();