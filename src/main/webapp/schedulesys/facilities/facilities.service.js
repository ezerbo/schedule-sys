(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('FacilitiesService', FacilitiesService)
		.factory('FacilitiesStaffMemberService', FacilitiesStaffMemberService);
	
	FacilitiesService.$Inject = ['$resource'];
	
	function FacilitiesService($resource) {
		var resourceUrl = '/facilities/:id';
		
		return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {method: 'GET'},
            'update':{
                method: 'PUT',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            },
            'remove':  {
                method: 'DELETE',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            },
            'save': {
                method: 'POST',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.toJson(data);
                    }
                    return data;
                }
            }
        });
	}
	
	FacilitiesStaffMemberService.$Inject = ['$resource'];
	
	function FacilitiesStaffMemberService($resource){
		var resourceUrl = '/facilities/:id/staff-members/:staffMemberId';
		return $resource(resourceUrl, {},{
			'query': { method: 'GET', isArray: true},
			'save': {
	                method: 'POST',
	                transformResponse: function (data) {
	                    if (data) {
	                        data = angular.toJson(data);
	                    }
	                    return data;
	                }
	            },
	        'update':{
	                method: 'PUT',
	                transformResponse: function (data) {
	                    if (data) {
	                        data = angular.toJson(data);
	                    }
	                    return data;
	                }
	            }
		});
	}
	
})();