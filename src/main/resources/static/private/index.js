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
    $http({
      method: "GET",
      url: "restrurent/findAllActiveList",
    }).then(
      function successCallback(response) {
        console.log(response);
        $scope.restrurents = response.data.data.filter(function (item) {
          return item.priority !== 0;
        });
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
