(function() {
	'use strict';
	
	angular
		.module('scheduleSys')
		.factory('NursePhoneService', NursePhoneService)
		.factory('CareGiverPhoneService', CareGiverPhoneService);
	
	NursePhoneService.$Inject = ['$resource'];
	CareGiverPhoneService.$Inject = ['$resource'];
	
	function NursePhoneService($resource) {
		var resourceUrl = '/nurses/:id/phone-numbers/:phoneNumberId';
		
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
            'update': { method:'PUT',
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
	
	function CareGiverPhoneService($resource) {
		var resourceUrl = '/care-givers/:id/phone-numbers/:phoneNumberId';
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
            'update': { method:'PUT',
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
	
})();