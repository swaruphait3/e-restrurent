var app = angular.module("IndexModule", ['ngSanitize']);

app.controller("IndexController", function ($scope, $http) {

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

app.controller("OrderItemController", function ($scope, $http) {

  $scope.itemName= "";
  $scope.images = "";
  $scope.form = {};
  
  autoWorkshopFetch();
  function autoWorkshopFetch() {

    const params = new URLSearchParams(window.location.search);
        const restId = params.get("itemId");
        $scope.activityId = restId;
        $scope.form.itemId = $scope.activityId;
    $http({
      method: "GET",
      params: { id: $scope.activityId },
      url: "food/findById",
    }).then(
      function successCallback(response) {
        $scope.foods = response.data.data;
        $scope.form =  $scope.foods;
        $scope.itemName = $scope.foods.name;
        $scope.images = $scope.foods.images;
        $scope.form.rate = $scope.foods.price;
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }


  $scope.priceCalculate = function (qty) {
    $scope.form.totalAmount = $scope.form.rate * qty;
  }

  $scope.proceedToOrder = function () {
        $http({
            method: "POST",
            url: 'order/purchase',
            data: angular.toJson($scope.form),
            headers: {
                'Content-Type': 'application/json'
            },
            transformResponse: angular.identity
        }).then(function (response) {
      Swal.fire({
          text: response.data.message,
          icon: "success",
          buttonsStyling: !1,
          confirmButtonText: "Ok, got it!",
          customClass: {
              confirmButton: "btn btn-primary"
          }
      })
          console.log(response.data);
      }).catch(function (error) {
          Swal.fire({
              text: response.data.message,
              icon: "error",
              buttonsStyling: !1,
              confirmButtonText: "Ok, got it!",
              customClass: {
                  confirmButton: "btn btn-primary"
              }
          })
          console.error(error);
      });
    };

});
