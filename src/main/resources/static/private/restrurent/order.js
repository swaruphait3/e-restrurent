var app = angular.module("OrderModule", ['ngSanitize']);

app.controller("OrderController", function ($scope, $http) {

    $scope.itemName= "";
  $scope.images = "";
  $scope.form = {};
  $scope.ordersLength = null;

  
  autoWorkshopFetch();
  function autoWorkshopFetch() {
    $http({
      method: "GET",
      url: "order/viewOrderRestaurant",
    }).then(
      function successCallback(response) {
        $scope.orders = response.data.data;
        $scope.ordersLength = $scope.orders.length; // Get the length here
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }



});
