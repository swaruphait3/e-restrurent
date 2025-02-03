var app = angular.module("BookingConfirmModule", []);

app.controller("BookingConfirmController", function ($scope, $http, $sce) {

    $scope.trustAsHtml = function (html) {
        return $sce.trustAsHtml(html);
    };

    getActivityDetails();
    function getActivityDetails() {
        const params = new URLSearchParams(window.location.search);
        const activityID = params.get("activityBookingId");
        const workshopID = params.get("workshopBookingId");
        $scope.viewName = null;
        $scope.viewName = null;
        if (activityID != null) {
            $scope.viewName = "Activity";

            $http({
                method: "GET",
                params: { id: activityID },
                url: "getActivityBookingByid",
            }).then(
                function successCallback(response) {
                    console.log(response.data);
                    $scope.activities = response.data;
                },
                function errorCallback(response) {
                    console.log(response.statusText);
                }
            );

        }

        if (workshopID != null) {
            $scope.viewName = "Weekly Special";

            $http({
                method: "GET",
                params: { id: workshopID },
                url: "getWorkshopBookingByid",
            }).then(
                function successCallback(response) {
                    console.log(response.data);
                    $scope.workshops = response.data;
                },
                function errorCallback(response) {
                    console.log(response.statusText);
                }
            );

        }
    }

});
