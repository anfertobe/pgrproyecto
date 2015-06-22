(function () {
    var app = angular.module('login', ['ngRoute']);

    app.config(function ($routeProvider) {
        $routeProvider
                .when('/home', {
                    templateUrl: 'home.html'

                });
    });
})();





