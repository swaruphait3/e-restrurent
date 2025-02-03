var app = angular.module("WebActivityModule", []);

// angular.module('yourApp', [])
// .controller('YourController', function ($scope) {
//     $scope.showAllItems = false;
//     $scope.showInitialView = false;
// });

app.controller("WebActivityController", function ($scope, $http) {

    $scope.search = {};

    autoActivityListFetch();
    function autoActivityListFetch() {
        // alert(" Fetch Hi-------------------------")
        $http({
            method: "GET",
            url: "getActiveActivity",
        }).then(
            function successCallback(response) {
                console.log(response);
                $scope.activitys = response.data;
            },
            function errorCallback(response) {
                //console.log(response.statusText);
            }
        );
    }

    autoCategoryDetailListFetch();
    function autoCategoryDetailListFetch() {
        //alert("hi----------------------")

        $http({
            method: 'GET',
            url: 'getActivityDetailsForPage'

        }).then(function successCallback(response) {
            console.log(response);
            $scope.data = response.data;
            $scope.categorys = response.data;

            // $scope.changeView('list');

        }, function errorCallback(response) {
            //console.log(response.statusText);
        });
    }


    $scope.toggleDefSize = function (data) {
        data.defSize = data.defSize === 4 ? data.size : 4;
    }



    $scope.openModal = function () {
        // $scope.search = {};
        $("#exampleModal").modal("show");

    }

    $scope.closeModal = function () {
        $scope.search = {};
        $scope.categorys = JSON.parse(JSON.stringify($scope.data));
        $("#exampleModal").modal("hide");
    }

    $scope.searchData = function () {
        // Create a copy of the original data
        let filteredData = JSON.parse(JSON.stringify($scope.data));

        // Filter by category if search.cat is provided
        if ($scope.search.cat && $scope.search.cat !== 'all') {
            filteredData = filteredData.filter(item => item.name === $scope.search.cat);
        }

        // Filter by activity duration if search.time is provided
        if ($scope.search.time) {
            filteredData.forEach(category => {
                category.activitys = category.activitys.filter(activity => activity.duration === parseInt($scope.search.time));
            });
        }


        // Filter out categories with no activities
        filteredData = filteredData.filter(category => category.activitys.length > 0);

        $scope.categorys = filteredData;
        if ($scope.search.priceType) {
            if ($scope.search.priceType === 'lowToHigh') {
                $scope.search.price = true;
            }
            else {
                $scope.search.price = false;
            }
        }
        $("#exampleModal").modal("hide");
    };



});


app.controller("activityBookingController", function ($scope, $http, $sce) {

    showHideLoad(true);

    $scope.trustAsHtml = function (html) {
        return $sce.trustAsHtml(html);
    };

    getActivityDetails();
    function getActivityDetails() {
        const params = new URLSearchParams(window.location.search);
        const id = params.get("id");
        $scope.activityId = id;
        $scope.form = {};
        $http({
            method: "GET",
            params: { id: $scope.activityId },
            url: "getActivityByid",
        }).then(
            function successCallback(response) {
                //console.log(response.data);
                $scope.activities = response.data;
                $scope.form.name = $scope.activities.name;
                $scope.form.activityId = $scope.activities.id;
                $scope.form.duration = $scope.activities.duration;
                $scope.form.availableSeats = $scope.activities.availableSeats;
                $scope.form.price = $scope.activities.price;
                $scope.form.holdSeats = 1;
                $scope.form.paymentStatus = "FP";
                $scope.form.dueAmount = 0.0;
            },
            function errorCallback(response) {
                //console.log(response.statusText);
            }
        );
    }

    $http.get("getTime")
        .then(function (response) {
            //console.log(response);
            $scope.shop = response.data
            initializeTimepickers();
        })
        .catch(function (error) {
            console.error('Error fetching shop start time:', error);
        });


    function initializeTimepickers() {
        // Initialize start time picker
        $("#startTime").flatpickr({
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
            allowInput: true,
            disableMobile: true,
            minuteIncrement: 30, // Set to 30 to allow only 30-minute intervals
            time_24hr: true, // Use 24-hour time format
            minTime: $scope.shop.start, // Set minimum time to shop start time
            maxTime: $scope.shop.end, // Set minimum time to shop start time
            defaultDate: [$scope.shop.start],
        });

        // Initialize end time picker
        $("#endTime").flatpickr({
            enableTime: true,
            noCalendar: true,
            dateFormat: "H:i",
            allowInput: true,
            disableMobile: true,
            minuteIncrement: 30, // Set to 30 to allow only 30-minute intervals
            time_24hr: true, // Use 24-hour time format
            minTime: $scope.shop.start, // Set minimum time to shop start time
            maxTime: $scope.shop.end // Set minimum time to shop start time
        });
    }

    $scope.checkDate = function (date) {
        if(date === undefined || date === null){
            alert("Please Select Date!!!!!!!!");
            $scope.invalidDate = true;  // Set invalidDate to true when date is invalid
            return;
        }
        $scope.invalidDate = false;  // Set invalidDate to false when date is valid
    };

    $scope.getCapacityForActivity = function (activityId, date, startTime, endTime) {
        $http({
            method: "GET",
            params: { 'activityId': activityId, 'date': date, 'startTime': startTime, 'endTime': endTime },
            url: "getCapacityForActivity",
        }).then(
            function successCallback(response) {
                //console.log(response.data);
                $scope.form.availableSeats = response.data;
            },
            function errorCallback(response) {
                //console.log(response.statusText);
            }
        );
    };


    $scope.calculateEndTime = function (id, date, inputTime, duration) {
        // Parse the input time into a Date object
        var time = new Date("2000-01-01 " + inputTime);
        // Convert duration to a number
        var durationInHours = parseInt(duration, 10);
        // Increase the time by the specified duration (in hours)
        time.setHours(time.getHours() + durationInHours);
        // Format the increased time as desired (e.g., 24-hour format without AM/PM)
        var outputTime = time.toLocaleTimeString("en-US", {
            hour: "2-digit",
            minute: "2-digit",
            hour12: false,
        });
        // Output the increased time
        //console.log(outputTime);
        // Assign the increased time to the endTime property of the form
        $scope.form.endTime = outputTime;
        $scope.getCapacityForActivity(id, date, inputTime, outputTime);
    };

   
    $scope.proceedToPayment = function () {
        if ($scope.form.paymentStatus === "FP" && $scope.form.holdAmount !== $scope.form.price * $scope.form.holdSeats) {
            showAlert("Price Mismatch", "error");
            return;
        }
        
        if ($scope.form.paymentStatus === "PP" && $scope.form.price * $scope.form.holdSeats !== $scope.form.holdAmount + $scope.form.dueAmount) {
            showAlert("Price Mismatch", "error");
            return;
        }
        
        if (!$scope.form.startTime || !$scope.form.endTime) {
            showAlert("Enter Start Time and End Time", "error");
            return;
        }
    
        $scope.paymentGateway($scope.form.holdAmount, $scope.form.activityId);
    };
    
    // Payment Gateway initialization function
    $scope.paymentGateway = function (amount, activityId) {
        showHideLoad();
    
        $http({
            method: "POST",
            url: "createOrder",
            data: { amount: amount.toString(), id: activityId, type: 'A' },
            headers: { "Content-Type": "application/json" }
        }).then(
            function (response) {
                handlePaymentResponse(response.data);
            },
            function (error) {
                showHideLoad(true);
                console.error("Error creating payment order:", error);
                showAlert("Error creating payment order", "error");
            }
        );
    };
    
    function handlePaymentResponse(data) {
        showHideLoad(true);
    
        if (data.status === "created") {
            const options = {
                key: 'rzp_live_03ZqIGhx4BVNIM',
                // key: 'rzp_test_QpuyoJh4EuusxU',
                amount: data.amount,
                currency: "INR",
                name: "Art Rickshaw",
                description: "Payable Amount",
                image: "assets/images/brad-logo-circle.png",
                order_id: data.id,
                handler: function (response) {
                    $scope.addEditActivityBookings(response.razorpay_payment_id, response.razorpay_order_id, "paid");
                    // $scope.createOrUpdateBooking();
                   // $scope.activityEmailSender($scope.id);
                    swal("Good Job !!", "Payment Successfull !!", "Success");
                },
                prefill: { name: "", email: "", contact: "" },
                notes: { address: "Art Rickshaw" },
                theme: { color: "#0C86F6" },
            };
    
            const rzp = new Razorpay(options);
    
            rzp.on("payment.failed", function () {
                showHideLoad(true);
                swal("Failed !!", "Payment failed.", "error");
            });
    
            rzp.on("modal.close", function () {
                showHideLoad(true);
                swal("Payment Failed", "You closed the payment popup. Please try again.", "error");
            });
    
            rzp.open();
        } else {
            showAlert("Failed to create payment order", "error");
        }
    }
    
   
    
    // Create or update the booking after payment success
    $scope.addEditActivityBookings = function (paymentId, orderId, status) {
        const method = $scope.form.id ? "PUT" : "POST";
        const url = $scope.form.id ? "updateActivityBooking" : "postActivityBooking";
    
         // Set payment details in form data
            $scope.form.paymentId = paymentId;
            $scope.form.orderId = orderId;
            $scope.form.paymentStatus = status;
    
        $http({
            method: method,
            url: url,
            data: angular.toJson($scope.form),
            headers: { "Content-Type": "application/json" }
        }).then(
            function (response) {
                $scope.id = response.data;
                $scope.updatePaymentOnServer($scope.id, paymentId, orderId, "paid").then(() => {
                    $scope.activityEmailSender($scope.id);
                    window.location.href = "booking-confirm?activityBookingId=" + $scope.id;
                }).catch(error => {
                    console.error("Error updating payment on server:", error);
                });
            },
            function (error) {
                console.error("Error creating booking:", error);
                showAlert("Error creating booking: " + error.statusText, "error");
            }
        );
    };
    
    // Update payment details on the server
    $scope.updatePaymentOnServer = function (id, razorpay_payment_id, razorpay_order_id, status) {
        showHideLoad();
        return $http({
            method: "PUT",
            url: "updateOrder",
            params: {
                id: id,
                razorpay_payment_id: razorpay_payment_id,
                razorpay_order_id: razorpay_order_id,
                status: status,
                type: 'A'
            }
        }).then(
            function (response) {
                showHideLoad(true);
                Swal.fire({
                    text: "Successfully Saved",
                    icon: "success",
                    confirmButtonText: "Ok, got it!",
                    customClass: { confirmButton: "btn btn-primary" }
                });
            },
            function (error) {
                showHideLoad(true);
                showAlert("Failed to update payment details on server", "error");
                console.log("Error updating payment:", error.statusText);
            }
        );
    };
    
    // Send confirmation email for activity booking
    $scope.activityEmailSender = function (id) {
        showHideLoad();
        $http({
            method: "GET",
            url: "activityConfirmMail",
            params: { id: id },
            headers: { "Content-Type": "application/json" },
            transformResponse: angular.identity
        }).then(
            function (response) {
                showHideLoad(true);
                console.log("Email sent successfully:", response);
            },
            function (error) {
                showHideLoad(true);
                console.log("Failed to send confirmation email:", error);
            }
        );
    };
    
    
    
    
    
    
    // Helper function to show alerts
    function showAlert(message, type) {
        Swal.fire({
            title: type === "error" ? 'Error' : 'Success',
            text: message,
            icon: type,
            confirmButtonText: 'OK'
        });
    }
    
  
    
    
    $scope.editActivityBookings = function (id) {
        $scope.form = $scope.listActivity.find(function (item) {
            return item.id === id;
        });
        $scope.getActiveActivity();
        $("#activity_modal").modal("show");
    };
  
    fetchHoliday();
    function fetchHoliday() {
        $http({
            method: 'GET',
            url: 'getHoliday'
        }).then(function successCallback(response) {
            // console.log(response.data);
            $scope.holidays = response.data;

            // Prepare holiday data in the form { date: "YYYY-MM-DD", occasion: "Holiday Name" }
            let holidayMap = {};
            $scope.holidays.forEach(holiday => {
                holidayMap[holiday.date] = holiday.occasion;
            });

            initializeDatePicker(holidayMap);  // Pass the holiday map to the date picker initialization
            
        }, function errorCallback(response) {
            console.log(response.statusText);
        });
    }

    function initializeDatePicker(holidayMap) {
        var now = new Date();

        let holidayDates = Object.keys(holidayMap);  // Extract the holiday dates from the map

        // Check if the current time is after 10:30:00
        if (now.getHours() > 10 || (now.getHours() === 10 && now.getMinutes() >= 30)) {
            // If after 10:30:00, set the minimum date to tomorrow
            var tomorrow = new Date(now);
            tomorrow.setDate(now.getDate() + 1); // Add one day
            $("#date").flatpickr({
                dateFormat: "Y-m-d",
                allowInput: true,
                disableMobile: true,
                minDate: tomorrow,
                disable: holidayDates,  // Disable holiday dates directly as strings
                onDayCreate: function(dObj, dStr, fp, dayElem) {
                    let formattedDate = fp.formatDate(dayElem.dateObj, "Y-m-d");  // Format date without timezone
                    if (holidayMap[formattedDate]) {
                        dayElem.setAttribute("title", holidayMap[formattedDate]);  // Set the occasion as tooltip
                        dayElem.style.backgroundColor = "#f8d7da";  // Optional: Highlight holiday dates
                        dayElem.style.color = "#721c24";  // Optional: Text color for holidays
                    }
                }
            });
        } else {
            // If before 10:30:00, set the minimum date to today
            $("#date").flatpickr({
                dateFormat: "Y-m-d",
                allowInput: true,
                disableMobile: true,
                minDate: "today",
                disable: holidayDates,  // Disable holiday dates directly as strings
                onDayCreate: function(dObj, dStr, fp, dayElem) {
                    let formattedDate = fp.formatDate(dayElem.dateObj, "Y-m-d");  // Format date without timezone
                    if (holidayMap[formattedDate]) {
                        dayElem.setAttribute("title", holidayMap[formattedDate]);  // Set the occasion as tooltip
                        dayElem.style.backgroundColor = "#f8d7da";  // Optional: Highlight holiday dates
                        dayElem.style.color = "#721c24";  // Optional: Text color for holidays
                    }
                }
            });
        }
    }



    // <!-- =============================================================== -->
    // <!-- %%%%%%%%%%%%%%%% Activity Booking END %%%%%%%%%%%%%%%% -->
    // <!-- =============================================================== -->


    $scope.$watch('form.holdSeats', function(newVal, oldVal) {
        if (newVal !== oldVal) {
            $scope.getNetAmount(newVal);
        }
    });
    $scope.getNetAmount = function (seat) {
        $scope.form.holdAmount = $scope.form.price * seat;
    };
    $scope.calculateDueAmount = function () {
        $scope.form.dueAmount =
            $scope.form.price * $scope.form.holdSeats - $scope.form.holdAmount;
    };
    $scope.paymentStatusChange = function (status) {
        if (status === "FP") {
            $scope.form.holdAmount = $scope.form.price * $scope.form.holdSeats;
            $scope.form.dueAmount = 0.0;
        }
    };

// $scope.paymentGateway = function (amount, id) {
//     showHideLoad();
//     var redirectUrl = "booking-confirm?activityBookingId=" + id;
//     if (!amount) {
//         alert("Amount is required");
//         return;
//     }

//     if (id !== "Updated") {
//         $http({
//             method: "POST",
//             url: "createOrder",
//             params: { amount: amount, id: id, type: 'A' },
//         }).then(
//             function (response) {
//                 showHideLoad(true);

//                 if (response.data.status == "created") {
//                     var options = {
//                         // key: "rzp_test_QpuyoJh4EuusxU",
//                         // Payment id of Art-Rickshaw (8017178277)
//                         // key: 'rzp_test_JOC0wRKpLH1cVW',
//                         // Payment id of Art-Rickshaw Live (8017178277)
//                         key: 'rzp_live_03ZqIGhx4BVNIM',
//                         amount: response.data.amount,
//                         currency: "INR",
//                         name: "Art Rickshaw",
//                         description: "Payable Amount",
//                         image: (src = "assets/images/brad-logo-circle.png"),
//                         order_id: response.data.id,
//                         handler: function (response) {
//                             window.location.href = redirectUrl;
//                             $scope.updatePaymentOnServer($scope.id,
//                                 response.razorpay_payment_id,
//                                 response.razorpay_order_id,
//                                 "paid"
//                             );
//                             $scope.activityEmailSender($scope.id);
//                             swal("Good Job !!", "Payment Successfull !!", "Success");
//                         },
//                         prefill: {
//                             name: "",
//                             email: "",
//                             contact: "",
//                         },
//                         notes: {
//                             address: "Art Rickshaw",
//                         },
//                         theme: {
//                             color: "#0C86F6",
//                         },
//                     };

//                     var rzp = new Razorpay(options);

//                     // Handle payment failed
//                     rzp.on("payment.failed", function (response) {
//                         showHideLoad(true);
//                         swal("Failed !!", "Oops payment failed !!", "error");
//                     });

//                     // Handle when the payment modal is closed without completing payment
//                     rzp.on("modal.close", function () {
//                         showHideLoad(true);
//                         swal("Payment Failed", "You closed the payment popup. Please try again.", "error");
//                     });

//                     rzp.open();
//                 }
//             },
//             function (error) {
//                 showHideLoad(true);
//                 alert("Something went wrong !!");
//             }
//         );
//     } else {
//         showHideLoad(true);
//         autoBookingsListFetch();
//         Swal.fire({
//             text: "Successfully Saved",
//             icon: "success",
//             buttonsStyling: !1,
//             confirmButtonText: "Ok, got it!",
//             customClass: {
//                 confirmButton: "btn btn-primary",
//             },
//         });
//     }
// };


    // $scope.updatePaymentOnServer = function (

    //     id,
    //     razorpay_payment_id,
    //     razorpay_order_id,
    //     status
    // ) {
    //     showHideLoad();
    //     //      alert("Hi");
    //     $http({
    //         method: "PUT",
    //         url: "updateOrder",
    //         params: {
    //             id: id,
    //             razorpay_payment_id: razorpay_payment_id,
    //             razorpay_order_id: razorpay_order_id,
    //             status: status,
    //             type: 'A',
    //         },
    //     }).then(
    //         function successCallback(response) {
    //             showHideLoad(true);
    //             //      alert(response.data);
    //             console(response.data);
    //             autoBookingsListFetch();
    //             Swal.fire({
    //                 text: "Sucessfully Saved",
    //                 icon: "success",
    //                 buttonsStyling: !1,
    //                 confirmButtonText: "Ok, got it!",
    //                 customClass: {
    //                     confirmButton: "btn btn-primary",
    //                 },
    //             });
    //         },
    //         function errorCallback(response) {
    //             showHideLoad(true);
    //             //console.log(response.statusText);
    //         }
    //     );
    // };
    

    // $scope.activityEmailSender = function (id) {
    //     showHideLoad();
    //     var method = "GET";
    //     var url = "activityConfirmMail";
    //     $http({
    //         method: method,
    //         params: { id: id },
    //         url: url,
    //         headers: {
    //             "Content-Type": "application/json",
    //         },
    //         transformResponse: angular.identity,
    //     }).then(
    //         function successCallback(response) {
    //             showHideLoad(true);
    //             console.log(response);
    //         },
    //         function errorCallback(response) {
    //             showHideLoad(true);
    //             console.log(response);
    //         }
    //     );
    // };
});
