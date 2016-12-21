(function() {
	
	'use strict';
	
	angular
		.module('scheduleSys')
		.controller('careGiversController', careGiversController);
	
	careGiversController.$Inject = ['$scope', '$state', '$mdToast', '$mdDialog' ,'careGiversService'];
	
	function careGiversController($scope, $state, $mdToast, $mdDialog, careGiversService){
		var vm = this;
		
		vm.allcareGivers = null;
		vm.careGiversOnCurrentPage = null;
		
		vm.loadAll = loadAll;
		vm.showConfirm = showConfirm;
		vm.selected = [];
		vm.deletion = true;
		vm.editOrDetails = false;
		vm.showToast = showToast;
		vm.onPaginate = onPaginate;
		vm.slicecareGiversArray = slicecareGiversArray;
		
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
					.title('Delete a Care-Giver')
					.textContent('Are you sure you want to delete this Care-Giver?')
					.ariaLabel('Care-Giver deletion')
					.targetEvent(ev)
					.ok('Delete')
					.cancel('Cancel');
			$mdDialog.show(confirm).then(function() {
				vm.selected.forEach(function(elt, i, array) {
					careGiversService.remove({id: elt.id}, onDeleteSuccess, onDeleteFailure);
				});
			}, function() {
				console.log('Keep this one ...');
			});
		};
		
		function loadAll(){
			careGiversService.query({}, onLoadAllSuccess, onLoadAllError);
		}
		
		function onLoadAllSuccess(data){
			vm.allcareGivers = data;
			vm.careGiversOnCurrentPage = vm.slicecareGiversArray();
		}
		
		function onLoadAllError(status){
			console.log('Error status : ' + status);
		}
		
		function onDeleteSuccess (){
			vm.showToast('Care giver successfully deleted', 5000);
			$state.go($state.current,{}, {reload:true});
			vm.deletion = true;
		}
		
		function onDeleteFailure (){
			vm.showToast('Care-Giver could not be deleted ', 5000);
		}
		
		function showToast(textContent, delay){
			$mdToast.show(
					$mdToast.simple()
					.textContent(textContent)
					.position('top right')
					.hideDelay(delay));
		}
		
		function onPaginate(){
			vm.careGiversOnCurrentPage = vm.slicecareGiversArray();
		}
		
		function slicecareGiversArray(){
			var slicedArray = vm.allcareGivers.slice(5 * (vm.query.page - 1), (vm.query.limit * vm.query.page));
			return slicedArray;
		}
	}
	
})();