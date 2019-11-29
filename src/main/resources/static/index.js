const app = angular.module("app", []);

function showMessage(str) {
    alert(str);
}

function handleError(err) {
    let message = `${err.data == null ? "Error!" : err.data.message}`;
    if (err.data.violated != null) {
        for (let violation of err.data.violated) {
            message += '\n' + violation;
        }
    }
    console.log(err);
    showMessage(message);
}

app.service('sharedService', function () {

});

app.controller('adminCtrl', function ($scope, $http, userFactory) {

    $scope.roles = userFactory.roles;

    $scope.populateCount = 20; // count of inputs to generate

    $scope.populateDb = function () {
        $http.get(`/test/populate?count=${$scope.populateCount}`).then(
            (response) => showMessage("Populated."),
            (err) => handleError(err)
        )
    };

    $scope.clearDb = function () {
        $http.get("/test/clear").then(
            (response) => showMessage(response.data.message),
            (err) => handleError(err)
        )
    };
});

app.controller('userCtrl', function ($scope, $http, userFactory) {

    $scope.roles = userFactory.roles;

    $scope.latest = [];

    $scope.locationFilter = "";
    $scope.latestCount = 10;

    $scope.getLatest = function (filter = $scope.locationFilter, count = $scope.latestCount) {
        $http.get(`/api/latest?filter=${filter.trim()}&count=${count}`)
            .then(
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
        $http.post("/api/add", $scope.input).then(
            (response) => showMessage("Added."),
            (err) => handleError(err)
        )
    };
});
