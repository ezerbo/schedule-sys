(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('PrivateCareService', PrivateCareService);
		//.factory('FacilitiesStaffMemberService', FacilitiesStaffMemberService);
	
	PrivateCareService.$Inject = ['$resource'];
	
	function PrivateCareService($resource) {
		var resourceUrl = '/private-cares/:id';
		
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
	//TODO replace this by contact
//	FacilitiesStaffMemberService.$Inject = ['$resource'];
//	
//	function FacilitiesStaffMemberService($resource){
//		var resourceUrl = '/facilities/:id/staff-members/:staffMemberId';
//		return $resource(resourceUrl, {},{
//			'query': { method: 'GET', isArray: true},
//			'save': {
//	                method: 'POST',
//	                transformResponse: function (data) {
//	                    if (data) {
//	                        data = angular.toJson(data);
//	                    }
//	                    return data;
//	                }
//	            },
//	        'update':{
//	                method: 'PUT',
//	                transformResponse: function (data) {
//	                    if (data) {
//	                        data = angular.toJson(data);
//	                    }
//	                    return data;
//	                }
//	            }
//		});
//	}
	
})();