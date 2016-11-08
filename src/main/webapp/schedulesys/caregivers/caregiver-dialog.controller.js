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
		vm.createOrUpdatePhoneNum = createOrUpdatePhoneNum;
		vm.showToast = showToast;
		vm.careGiverpositionType = careGiversPositionTypeService.query();
		vm.options2 = vm.careGiverpositionType;
		vm.careGiverphoneType = PhoneTypeService.query();
		vm.options3 = vm.careGiverphoneType;
		vm.careGiverphoneLabel = PhoneLabelService.query();
		vm.options4 = vm.careGiverphoneLabel;
		console.log(vm.options2);
		console.log(vm.options3);
		console.log(vm.options4);
		vm.getSelectedcareGiver = getSelectedcareGiver;
		vm.showPhoneNum = showPhoneNum;
		vm.showOtherPhoneNum = showOtherPhoneNum;
		vm.secondary = "SECONDARY";
		vm.other = "OTHER";
		vm.showConfirmPhone = showConfirmPhone;
		
		vm.myModel = {};
		vm.careGiver = {
				id: null,
				firstName: null,
				lastName: null,
				positionName: null,
				ebc: null,
				insvc: null,
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
		
		vm.careGiver.phoneNumbers[0].numberLabel = "PRIMARY";
		

		
        function showPhoneNum(){
			
			vm.displayPhone = ! vm.displayPhone;
			console.log(vm.displayPhone);
			if(vm.displayPhone){
				
				vm.q =  { 
			           number: null,
			           numberLabel: "SECONDARY",
			           numberType: null
			         };
				
			vm.careGiver['phoneNumbers'].push(vm.q);
		
				
		
						
				console.log(vm.careGiver);
				
				
				
				
				
			}else if(!vm.displayPhone){
				
				vm.careGiver['phoneNumbers'].pop();
				
				
			}
			console.log(vm.careGiver);
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
				
			vm.careGiver['phoneNumbers'].push(vm.u);
		
				
		
						
				console.log(vm.careGiver);
				
				
				
				
				
			}else if(!vm.displayOtherPhone){
				
				vm.careGiver['phoneNumbers'].pop();
				
				
			}
			console.log(vm.careGiver);
		}
		
		
		if(angular.isDefined($stateParams.id)){
			vm.getSelectedcareGiver();
		}
		
		console.log(vm.careGiver.dateOfHire);
		
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
			console.log('Care-Giver to be created : ' + angular.toJson(vm.careGiver));
			if(vm.careGiver.id === null){
				careGiversService.save(vm.careGiver, onCreateSucess, onCreateFailure);
				
			}else{
				careGiversService.update({id: $stateParams.id},vm.careGiver, onUpdateSucess, onUpdateFailure);
				console.log("Caregiver-ID : " + $stateParams.id);
			}
			
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			$state.go('home.caregivers',{}, {reload: true});
			vm.showToast('Care-Giver ' + vm.careGiver.firstName + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			$state.go('home.caregivers',{}, {reload: true});
			vm.showToast('Care-Giver ' + vm.careGiver.firstName + ' successfully updated', 5000);
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
						careGiverPhoneService.save({caregivID: vm.careGiver.id},vm.phoneNumbers, onCreatePhoneSucess, onCreateFailure);
					}else{
						careGiverPhoneService.update({caregivID: vm.careGiver.id, id: vm.phoneNumbers.id},vm.phoneNumbers, onUpdatePhoneSucess, onUpdateFailure);
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
				careGiverPhoneService.remove(
						{caregivID: vm.careGiver.id, id: vm.deleteNumber.id},
						onDeleteSuccess,
						onDeleteFailure
						);
			}, function() {
				console.log('Keep this one ...');
			});
		};

		function onDeleteSuccess (){
			$mdDialog.cancel();
			$state.go('home.caregivers',{}, {reload: true});
			vm.showToast('Phone-Number ' + vm.deleteNumber.number + ' successfully deleted', 5000);
		}	

		function onDeleteFailure (){
			$mdDialog.cancel();
			$state.go('home.caregivers',{}, {reload: true});
			vm.showToast('Phone-Number ' + vm.deleteNumber.number 
					+ ' could not be deleted ', 5000);
		}
		
	}
	
})();