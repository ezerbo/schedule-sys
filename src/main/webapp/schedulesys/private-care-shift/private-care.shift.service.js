(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('PrivateCareShiftService', PrivateCareShiftService);
	
	PrivateCareShiftService.$Inject = ['$resource'];
	
	function PrivateCareShiftService($resource) {
		var resourceUrl = '/private-care-shifts/:id';
		
		return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' },
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
	
})();