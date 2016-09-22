(function(){
	'use strict';
	angular
		.module('scheduleSys')
		.controller('PrivateCareDetailsController', PrivateCareDetailsController);
	
	PrivateCareDetailsController.$Inject = ['$state','$scope', '$stateParams', '$mdDialog',
	                                     '$mdToast', 'PrivateCareService', 'PrivateCareContactService','ContactService'];
	
	function PrivateCareDetailsController($state,$scope, $stateParams, $mdDialog, $mdToast,
			PrivateCareService, PrivateCareContactService, ContactService){
		
		var vm = this;
		vm.allContacts = [];
		vm.contactsOnCurrentPage = [];
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.getContacts = getContacts;
		vm.getSelectedPrivateCare = getSelectedPrivateCare;
		
		vm.selected = [];
		vm.editOrDelete = true;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.sliceContactsArray = sliceContactsArray;
		vm.query = {limit: 5, page: 1};
		
		loadAll();
		getContacts();
		getSelectedPrivateCare();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			vm.editOrDelete = (vm.selected.length === 0) ? true : false;
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete a Contact')
					.textContent('Are you sure you want to delete this Contact ?')
					.ariaLabel('Contact deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				ContactService.remove(
						{id:vm.selected[0].id},
						onDeleteSuccess,
						onDeleteFailure
						);
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			PrivateCareContactService.query({id: $stateParams.id},
					onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allContacts = data;
			vm.contactsOnCurrentPage = vm.sliceContactsArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}

		function onDeleteSuccess (){
			$state.go($state.$current, {}, {reload:true});
			vm.showToast('Contact ' + vm.selected[0].firstName + ' successfully deleted', 5000);
		}	
		
		function onDeleteFailure (){
			vm.showToast('Contact ' + vm.selected[0].firstName 
					+ ' could not be deleted ', 5000);
		}
		
		function showToast(textContent, delay){
			$mdToast.show($mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
		function onPaginate(){
			vm.contactsOnCurrentPage = vm.sliceContactsArray();
		}
		
		function sliceContactsArray(){
			return vm.allContacts.slice(
					5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
		}
		
		function getSelectedPrivateCare(){
			PrivateCareService.get({id:$stateParams.id}, function(result) {
				vm.privateCare = result;
			});
		}
		
		function getContacts(){
			PrivateCareContactService.query({id:$stateParams.id}, function(result) {
				vm.allContacts = result;
			});
		}
	}
})();