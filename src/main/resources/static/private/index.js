var app = angular.module("IndexModule", ['ngSanitize']);

app.controller("IndexController", function ($scope, $http, $sce) {

  $scope.trustAsHtml = function (html) {
    return $sce.trustAsHtml(html);
  };


  autoWorkshopFetch();
  function autoWorkshopFetch() {
    $http({
      method: "GET",
      url: "food/findAllActiveList",
    }).then(
      function successCallback(response) {
        // console.log(response.data);
        $scope.foods = response.data.data;
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
        // console.log(response);
        $scope.restrurents = response.data.data.filter(function (item) {
          return item.priority !== 0;
        });
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

  // $scope.ItemsListbyRestrurent = function (id) {
  //   $http({
  //     method: 'GET',
  //     params: { 'id': id },
  //     url: 'food/findItemsByResturentId'
  //   }).then(function successCallback(response) {
  //     console.log(response);
      
  //     $scope.$apply(() => { 
  //       $scope.items = response.data.data;
  //     });
  
  //     alert(JSON.stringify(response.data.data)); 
  //   }, function errorCallback(response) {
  //     console.log(response.statusText);
  //   });
  // };
  
});


app.controller("RestrurentItemController", function ($scope, $http) {


  autoWorkshopFetch();
  function autoWorkshopFetch() {

    const params = new URLSearchParams(window.location.search);
        const restId = params.get("restid");
        $scope.activityId = restId;

    $http({
      method: "GET",
      params: { id: $scope.activityId },
      url: "food/findItemsByResturentId",
    }).then(
      function successCallback(response) {
        $scope.foods = response.data.data;
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

});
