(function () {
    var app = angular.module('modone', ['ngRoute']);
   
    app.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider
                    // route for the about page
                    .when('/index1', {
                        templateUrl: 'index1.html'

                    })
                    .otherwise({
                        redirectTo: '/index'
                    });
        }]);
})();
