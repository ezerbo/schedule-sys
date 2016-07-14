(function() {
    'use strict';

    angular
        .module('scheduleSys')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('login', {
            url: '/',
            templateUrl: 'schedulesys/login/login.html',
            controller: 'LoginController'
        });
    }
})();
