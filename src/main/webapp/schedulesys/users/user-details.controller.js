(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('userDialogController', userDialogController);
	
	userDialogController.$Inject = ['$state', '$mdDialog', '$mdToast', 'usersService','userRoleService'];
	
	function userDialogController($state, $mdDialog, $mdToast, usersService,userRoleService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdateuser = createOrUpdateuser;
		vm.showToast = showToast;
		vm.userRole = userRoleService.query();
		vm.options6 = vm.userRole;
		
		vm.myModel = {};
		vm.user = {
				id: null,
				username: null,
				userRole: null
			
		};
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		function createOrUpdateuser(){
			console.log('User to be created : ' + angular.toJson(vm.user));
			if(vm.user.id === null){
				usersService.save(vm.user, onCreateOrUpdateSucess, onCreateOrUpdateFailure);
			}else{
				usersService.update(vm.user, onCreateOrUpdateSucess, onCreateOrUpdateFailure);
			}
		}
		
		function onCreateOrUpdateSucess(result){
			$state.go('home.users',{}, {reload: true});
			$mdDialog.cancel();
			vm.showToast('User ' + vm.user.username + ' successfully created', 5000);
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