const app = angular.module("app", []);

function handleError(err) {
    alert("Error!");
    console.log(err);
}

app.service('sharedService', function() {

});

app.controller('adminCtrl', function ($scope, $http, userFactory) {

    $scope.roles = userFactory.roles;

    $scope.populateCount = 20; // count of inputs to generate

    $scope.populateDb = function () {
        $http.get(`/test/populate?count=${$scope.populateCount}`).then(
            (response) => alert("Populated!"),
            (err) => handleError(err)
        )
    };

    $scope.clearDb = function () {
        $http.get("/test/clear").then(
            (response) => alert("Cleared!"),
            (err) => handleError(err)
        )
    };
});

app.controller('userCtrl', function ($scope, $http, userFactory) {

    $scope.roles = userFactory.roles;

    $scope.latest = [];

    $scope.getLatest = function () {
        $http.get("/api/latest").then(
            (response) => {
                console.log(response.data);
                $scope.latest = response.data
            },
            (err) => handleError(err)
        )
    };

    $scope.getLatest();
});

app.controller('sensorCtrl', function ($scope, $http) {
    $scope.input = {
        value: 0,
        lat: 60,
        lon: 30
    };

    $scope.addInput = function () {
        console.log("Hello!");
        $http.post("/api/add", $scope.input).then(
            (response) => alert("Added"),
            (err) => handleError(err)
        )
    };
});
