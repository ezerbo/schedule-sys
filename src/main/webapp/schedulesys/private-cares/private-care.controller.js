(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('PrivateCareController', PrivateCareController);
	
	PrivateCareController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog' ,'PrivateCareService'];
	
	function PrivateCareController($scope, $state, $mdToast, $mdDialog, PrivateCareService){
		var vm = this;
		
		vm.allPrivateCares = null;
		vm.privateCaresOnCurrentPage = null;
		
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.selected = [];
		vm.deletion = true;
		vm.editOrDetails = false;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.slicePrivateCaresArray = slicePrivateCaresArray;
		//vm.showPrivateCareDialog = showPrivateCareDialog;
		
		vm.query = {
				order: 'name',
				limit: 20,
				page: 1
		};
		
		loadAll();
		
		$scope.$watchCollection('vm.selected', function(oldValue, newValue) {
			if(vm.selected.length === 0){
				vm.deletion = true;
			}else{
				vm.deletion= false;
			}
			vm.editOrDetails = (vm.selected.length === 1) ? false : true;
		});
		
		function showConfirm(ev) {
			var confirm = $mdDialog.confirm()
					.title('Delete ' + vm.selected.length + ' private care(s)')
					.textContent('Are you sure you want to delete the selected private care(s)?')
					.ariaLabel('Private care deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			
			$mdDialog.show(confirm).then(function() {
				vm.selected.forEach(function(elt, i, array) {
					PrivateCareService.remove({id: elt.id},onDeleteSuccess,onDeleteFailure);
				});
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			PrivateCareService.query({}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allPrivateCares = data;
			vm.privateCaresOnCurrentPage = vm.slicePrivateCaresArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}
		
		function onDeleteSuccess (){
			vm.privateCaresOnCurrentPage.splice(vm.privateCaresOnCurrentPage.indexOf(vm.selected[0]), 1);
			vm.allPrivateCares.splice(vm.allPrivateCares.indexOf(vm.selected[0]), 1);
			vm.deletion = true;
			vm.showToast('Private care ' + vm.selected[0].name + ' successfully deleted', 5000);
		}	
		
		function onDeleteFailure (){
			vm.showToast('Private care ' + vm.selected[0].name 
					+ ' could not be deleted, please make sure it has no contacts and past schedules', 5000);
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
		function onPaginate(){
			vm.privateCaresOnCurrentPage = vm.slicePrivateCaresArray();
		}
		
		function slicePrivateCaresArray(){
			var slicedArray = vm.allPrivateCares.slice(10 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
			return slicedArray;
		}
		
	}
	
})();