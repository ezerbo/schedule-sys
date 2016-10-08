(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('UserProfileController', UserProfileController);
	
	UserProfileController.$Inject = ['$state', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'PasswordResetService', 'usersService',
	                                     'AuthenticationProvider', 'Commons', 'UserActivityService'];
	
	function UserProfileController($state, $stateParams, $mdDialog, $mdToast, usersService,
			PasswordResetService, AuthenticationProvider, Commons, UserActivityService){
		var vm = this;
		vm.activityLogsUI = false;
		vm.passwordChangeUI = false;
		vm.changePwdBtnDisabled = true;
		vm.showHideLogsUI = showHideLogsUI;
		vm.changePassword = changePassword;
		vm.onLeftArrowClick = onLeftArrowClick;
		vm.onRightArrowClick = onRightArrowClick;
		vm.onPasswordInput = onPasswordInput;
		vm.showPasswordChangeUI = showPasswordChangeUI;
		vm.hidePasswordChangeUI = hidePasswordChangeUI;
		
		vm.activity = {
				startDate: null,
				endDate: null
		}
		
		getAuthenticatedUser();
		initActivityWeek();
		
		function changePassword(){
			PasswordResetService.save(vm.credentials, function(result) {
				showToast('Password successfully updated');
				$state.go($state.current, {}, {reload:true});
			}, function(result) {
				showToast(result.data);
			})
		}
		
		function getAcitvityLogs(){
			vm.activity = UserActivityService.get({id:vm.currentUser.id, startDate:vm.activity.startDate, endDate:vm.activity.endDate});
		}
		
		function getAuthenticatedUser(){
			usersService.get({id:AuthenticationProvider.getPrincipal()}, function(result) {
				vm.currentUser = result;
			})
		}
		
		function onPasswordInput(){
			if(angular.isDefined(vm.credentials.password) && angular.isDefined(vm.credentials.passwordConf) && vm.credentials.passwordConf !== null){
				vm.changePwdBtnDisabled = ((vm.credentials.password === vm.credentials.passwordConf) 
						&& angular.isDefined(vm.credentials.oldPassword)) ? false : true;
			}else if(vm.credentials.passwordConf == null){
				vm.changePwdBtnDisabled = true;
			}
		}
		
		function showHideLogsUI(){
			vm.activityLogsUI = (vm.activityLogsUI === true) ? false : true;
			if(vm.activityLogsUI === true){
				getAcitvityLogs();
			}
		}
		
		function showPasswordChangeUI(){
			vm.passwordChangeUI = true;
		}
		
		function hidePasswordChangeUI(){
			vm.passwordChangeUI = false;
		}
		
		function showToast(content){
			 $mdToast.show(
				      $mdToast.simple()
				        .textContent(content)
				        .position('top center')
				        .hideDelay(5000));
		}
		
		function onLeftArrowClick(){
			vm.activity.startDate = formatDate(
					moment(vm.activity.startDate, 'YYYY/MM/DD').subtract(7, 'day'));
			vm.activity.endDate = formatDate(
					moment(vm.activity.endDate, 'YYYY/MM/DD').subtract(7, 'day'));
			getAcitvityLogs();
		}
		
		function onRightArrowClick(){
			vm.activity.startDate =  formatDate(
					moment(vm.activity.startDate, 'YYYY/MM/DD').add(7, 'day'));
			vm.activity.endDate = formatDate(
					moment(vm.activity.endDate, 'YYYY/MM/DD').add(7, 'day'));
			getAcitvityLogs();
		}
		
		function initActivityWeek(){
			vm.activity.startDate = formatDate(
					moment(Commons.getCurrentWeekStartDate()));
			vm.activity.endDate =  formatDate(
					moment(Commons.getCurrentWeekEndDate())); 
		}
		
		function formatDate(date){
			return moment(date).format('YYYY/MM/DD');
		}
	}
})();