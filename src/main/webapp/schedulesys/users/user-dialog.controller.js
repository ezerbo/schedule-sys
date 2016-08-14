(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('userDialogController', userDialogController);
	
	userDialogController.$Inject = ['$state','$stateParams', '$mdDialog', '$mdToast', 'usersService','userRoleService'];
	
	function userDialogController($state,$stateParams, $mdDialog, $mdToast, usersService,userRoleService){
		var vm = this;
		
		vm.cancel = cancel;
		vm.createOrUpdateuser = createOrUpdateuser;
		vm.showToast = showToast;
		vm.userRole = userRoleService.query();
		vm.options6 = vm.userRole;
		vm.getSelectedUser = getSelectedUser;
		vm.showPassword = showPassword;
		
		function showPassword(){
			
			console.log($state.current.name);
			if($state.current.name === "home.users.edit"){
			return false;
			}
			
			return true;
		}
		
		vm.myModel = {};
		vm.user = {
				id: null,
				username: null,
				userRole: null
			
		};
		
		function cancel() {
			$mdDialog.cancel();
		}
		
		if(angular.isDefined($stateParams.id)){
			vm.getSelectedUser();
		}
		
		
		
		function getSelectedUser(){
			usersService.get({id : $stateParams.id},function(result){
				vm.user = result;
				
			});
		}
		function createOrUpdateuser(){
			console.log('User to be created : ' + angular.toJson(vm.user));
			if(vm.user.id === null){
				usersService.save(vm.user, onCreateSucess, onCreateFailure);
			}else{
				usersService.update({id:$stateParams.id},vm.user, onUpdateSucess, onUpdateFailure);
			}
		}
		
		function onCreateSucess(result){
			$mdDialog.cancel();
			$state.go('home.users',{}, {reload: true});
			vm.showToast('User ' + vm.user.username + ' successfully created', 5000);
		}
		
		function onCreateFailure(result){
			vm.showToast(result.data, 5000);
		}
		
		function onUpdateSucess(result){
			$mdDialog.cancel();
			$state.go('home.users',{}, {reload: true});
			vm.showToast('User ' + vm.user.username + ' successfully updated', 5000);
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