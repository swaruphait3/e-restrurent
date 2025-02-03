var app = angular.module("IndexModule", ['ngSanitize']);

app.controller("IndexController", function ($scope, $http, $sce) {

  $scope.trustAsHtml = function (html) {
    return $sce.trustAsHtml(html);
  };


  autoWorkshopFetch();
  function autoWorkshopFetch() {
    $http({
      method: "GET",
      url: "file/getWorkShopForBooking",
    }).then(
      function successCallback(response) {
        console.log(response.data);
        $scope.listWorkshop = response.data;
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

  autoActivityListFetch();
  function autoActivityListFetch() {
    // alert(" Fetch Hi-------------------------")
    $http({
      method: "GET",
      url: "getActiveActivity",
    }).then(
      function successCallback(response) {
        console.log(response);
        $scope.activitys = response.data.filter(function (item) {
          return item.priority !== 0;
        });

        console.log("data++++++"+$scope.activitys);
        console.log($scope.activitys);

      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

  autoRegularClassesListFetch();
  function autoRegularClassesListFetch() {
    //      alert(" Fetch Hi-------------------------")
    $http({
      method: 'GET',
      url: 'getActiveRegularClasses'

    }).then(function successCallback(response) {
      console.log(response);
      $scope.classess = response.data;
    }, function errorCallback(response) {
      console.log(response.statusText);
    });
  }

  autoPrivateEventsListFetch();
  function autoPrivateEventsListFetch() {
    // alert("Hi-------------------------")
    $http({
      method: 'GET',
      url: 'getActivePrivateEvents'

    }).then(function successCallback(response) {
      console.log(response);
      $scope.privateEventss = response.data;
    }, function errorCallback(response) {
      console.log(response.statusText);
    });
  }






});
