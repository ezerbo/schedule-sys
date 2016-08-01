(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('usersController', usersController);
	
	usersController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog' ,'usersService'];
	
	function usersController($scope, $state, $mdToast, $mdDialog, usersService){
		var vm = this;
		
		vm.allusers = null;
		vm.usersOnCurrentPage = null;
		
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceusersArray = sliceusersArray;
		vm.showusersDialog = showusersDialog;
		
		vm.query = {
				order: 'name',
				limit: 5,
				page: 1
		};
		
		loadAll();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
			console.log('Selected items : ' + angular.toJson(vm.selected));
			console.log('Edit or delete : ' + vm.editOrDelete);
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a User')
					.textContent('Are you sure you want to delete this User?')
					.ariaLabel('User deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				usersService.remove(
						{id:vm.selected[0].id},
						onDeleteSuccess,
						onDeleteFailure
						);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			usersService.query({}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allusers = data;
			vm.usersOnCurrentPage = vm.sliceusersArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}
		
		function onDeleteSuccess (){
			vm.usersOnCurrentPage.splice(vm.usersOnCurrentPage.indexOf(vm.selected[0]), 1);
			vm.allusers.splice(vm.allusers.indexOf(vm.selected[0]), 1);
			vm.editOrDelete = true;
			vm.showToast('User ' + vm.selected[0].username + ' successfully deleted', 5000);
		}	
		
		function onDeleteFailure (){
			vm.showToast('User ' + vm.selected[0].username 
					+ ' could not be deleted ', 5000);
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay)
					);
		}
		
		function onPaginate(){
			vm.usersOnCurrentPage = vm.sliceusersArray();
		}
		
		function sliceusersArray(){
			var slicedArray = vm.allusers.slice(5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
			console.log('Sliced array : ' + angular.toJson(slicedArray));
			return slicedArray;
		}
		
		function showusersDialog(ev) {
			$mdDialog.show({
				templateUrl: 'schedulesys/users/user-dialog.html',
				parent: angular.element(document.body),
				targetEvent: ev,
				clickOutsideToClose:true
			})
			.then(function() {
				//$scope.status = 'You said the information was "' + answer + '".';
			}, function() {
				//$scope.status = 'You cancelled the dialog.';
			});
		};
	
	}
	
})();