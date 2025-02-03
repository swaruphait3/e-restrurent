var app = angular.module("WebEnquiryModule", []);

//Forget Pass Controller
app.controller("WebEnquiryController", function ($scope, $http) {
  $scope.form = {};
  $scope.views = {};
  $scope.views.list = true;

  autoRegularClassesListFetch();
  function autoRegularClassesListFetch() {
    $scope.listSize = 4;
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

  $scope.toggleListSize = function (data) {
    $scope.listSize = $scope.listSize === 4 ? data.length : 4;
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

app.controller("EnquiryController", function ($scope, $http, $sce) {

  $scope.trustAsHtml = function (html) {
    return $sce.trustAsHtml(html);
  };


  $scope.addEditEnquiry = function () {
    console.log($scope.form);
    // alert("hi...............")

    if ($scope.form.id == '' || $scope.form.id == undefined) {
      var method = "POST";
      var url = 'postEnquiry';
    } else {
      var method = "PUT";
      var url = 'updateEnquiry';
    }
    $http({
      method: method,
      url: url,
      data: angular.toJson($scope.form),
      headers: {
        'Content-Type': 'application/json'
      },
      transformResponse: angular.identity
    }).then(
      function successCallback(response) {
        console.log(response.data);
        $scope.postOTP(response.data);
      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  };


  $scope.postOTP = function (id) {
    Swal.fire({
      title: "Enter OTP",
      html: '<input type="text" id="otpInput" placeholder="Enter OTP" class="form-control">',
      showCancelButton: true,
      preConfirm: () => {
        const otpInput = document.getElementById('otpInput').value;
        if (!otpInput) {
          Swal.showValidationMessage('Please enter OTP');
        }
        return otpInput;
      }
    }).then((result) => {
      if (result.value) {
        console.log("OTP entered: " + result.value);
        showHideLoad();
        $http({
          method: 'PUT',
          params: { 'id': id, 'otp': result.value },
          url: 'getEnquiryUpdateByOtp',
          headers: {
            'Content-Type': 'application/json'
          },
          transformResponse: angular.identity
        }).then(function successCallback(response) {
          console.log(response.data);
          
        window.location.href = "enquiry-confirm";
          var reason = $scope.form.reason;
          $scope.form = {};
          $scope.form.reason = reason;
          showHideLoad(true);
        }, function errorCallback(response) {
          showHideLoad(true);
          $scope.postOTP(id);
        });
      }
    });

  }


  function _success(response) {
    console.log(response);
    var reason = $scope.form.reason;
    $scope.form = {};
    $scope.form.reason = reason;
    Swal.fire({
      text: response.data,
      icon: "success",
      buttonsStyling: !1,
      confirmButtonText: "Ok, got it!",
      customClass: {
        confirmButton: "btn btn-primary"
      }
    })
  }

  function _error(response) {
    console.log(response);
    Swal.fire({
      text: response.data,
      icon: "error",
      buttonsStyling: !1,
      confirmButtonText: "Ok, got it!",
      customClass: {
        confirmButton: "btn btn-primary"
      }
    })
  }


  getEventsData();
  function getEventsData() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get("privateEventId");
    $scope.eventId = id;
    $scope.form = {};
    $http({
      method: "GET",
      params: { id: $scope.eventId },
      url: "getPrivateEventsByid",
    }).then(
      function successCallback(response) {
        console.log(response.data);
        $scope.events = response.data;
        $scope.form.reason = $scope.events.name;

      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

  getClassesData();
  function getClassesData() {
    const params = new URLSearchParams(window.location.search);
    const id = params.get("classesId");
    $scope.classId = id;
    $scope.form = {};
    $http({
      method: "GET",
      params: { id: $scope.classId },
      url: "getRegularClassesByid",
    }).then(
      function successCallback(response) {
        console.log(response.data);
        $scope.listClass = response.data;
        $scope.form.reason = $scope.listClass.name;

      },
      function errorCallback(response) {
        console.log(response.statusText);
      }
    );
  }

  $scope.OTPGenerator = function () {
    alert("otp--------");
    $http({
      method: 'GET',
      url: 'OTPGenerator'

    }).then(function successCallback(response) {
      console.log(response.data);
      $scope.otp = response.data;
      alert($scope.otp)
    }, function errorCallback(response) {
      console.log(response.statusText);
    });
  }

  $scope.sendOtp = function (mobile) {
    alert("mobile--------" + mobile);
    $http({
      method: 'GET',
      params: { 'mobile': mobile },
      url: 'sendSMSForOTP'

    }).then(function successCallback(response) {
      console.log(response.data);
      $scope.form = response.data;

    }, function errorCallback(response) {
      console.log(response.statusText);
    });
  }



});
