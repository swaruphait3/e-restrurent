

function getGreeting(hours) {
  if (hours >= 0 && hours < 12) {
    return 'Good Morning';
  } else if (hours >= 12 && hours < 17) {
    return 'Good Afternoon';
  } else {
    return 'Good Evening';
  }
}

//end getting realtime Date Time

//nav toggle functionality
var navToggler = document.getElementById('navToggler');
var mainWrapper = document.getElementById('mainWrapper');
var navMenu = document.getElementById('navMenu');
var bodyWrapper = document.getElementById('bodyWrapper');
//toggler button click to open and close menu function
// var isSideMenuClose = mainWrapper.classList.contains('menu-close');

// window.onload = function () {
// 	mainWrapper.classList.add('menu-close');
// };
//on mouse enter side menu open
// navContainer.onmouseenter = function () {
// 	mainWrapper.classList.remove('menu-close');
// 	mainWrapper.classList.add('hover-menu-open');
// }
//on mouse leave side menu close
// navContainer.onmouseleave = function () {
// 	mainWrapper.classList.add('menu-close');
// 	mainWrapper.classList.remove('hover-menu-open');
// }

navToggler.onclick = function () {

  mainWrapper.classList.remove('hover-menu-open');

  var isSideMenuClose = mainWrapper.classList.contains('menu-close');
  if (isSideMenuClose) {
    mainWrapper.classList.remove('menu-close');

    navContainer.onmouseenter = function () {
      // mainWrapper.classList.remove('menu-close');
      mainWrapper.classList.remove('hover-menu-open');
    }
    //on mouse leave side menu will remain opened 
    navContainer.onmouseleave = function () {
      mainWrapper.classList.remove('menu-close');
      mainWrapper.classList.remove('hover-menu-open');
    }
  }
  else {
    mainWrapper.classList.add('menu-close');

    //on mouse enter side menu open
    navContainer.onmouseenter = function () {
      mainWrapper.classList.remove('menu-close');
      mainWrapper.classList.add('hover-menu-open');
    }
    //on mouse leave side menu close
    navContainer.onmouseleave = function () {
      mainWrapper.classList.add('menu-close');
      mainWrapper.classList.remove('hover-menu-open');

    }
  }
}


if (window.innerWidth < 576) {
  mainWrapper.classList.add('menu-close');

  bodyWrapper.onclick = function () {
    mainWrapper.classList.add('menu-close');
  }
}

//nav sub menu open close functionality
function toggleSubMenu(element) {
  var parentElement = element.parentNode;
  var isSubMenuActive = parentElement.classList.contains('sub-menu-active');
  // Remove the class from all parent elements
  var elements = document.getElementsByClassName('nav-item');
  for (var i = 0; i < elements.length; i++) {
    elements[i].classList.remove('sub-menu-active');
  }

  // Toggle the class on the parent element of the clicked element
  if (!isSubMenuActive) {
    parentElement.classList.add('sub-menu-active');
  }
}

//custom select
var x, i, j, l, ll, selElmnt, a, b, c;
/*look for any elements with the class "custom-select":*/
x = document.getElementsByClassName("custom-select");
l = x.length;
for (i = 0; i < l; i++) {
  selElmnt = x[i].getElementsByTagName("select")[0];
  ll = selElmnt.length;
  /*for each element, create a new DIV that will act as the selected item:*/
  a = document.createElement("DIV");
  a.setAttribute("class", "select-selected");
  a.innerHTML = selElmnt.options[selElmnt.selectedIndex].innerHTML;
  x[i].appendChild(a);
  /*for each element, create a new DIV that will contain the option list:*/
  b = document.createElement("DIV");
  b.setAttribute("class", "select-items select-hide");
  for (j = 1; j < ll; j++) {
    /*for each option in the original select element,
    create a new DIV that will act as an option item:*/
    c = document.createElement("DIV");
    c.innerHTML = selElmnt.options[j].innerHTML;
    c.addEventListener("click", function (e) {
      /*when an item is clicked, update the original select box,
and the selected item:*/
      var y, i, k, s, h, sl, yl;
      s = this.parentNode.parentNode.getElementsByTagName("select")[0];
      sl = s.length;
      h = this.parentNode.previousSibling;
      for (i = 0; i < sl; i++) {
        if (s.options[i].innerHTML == this.innerHTML) {
          s.selectedIndex = i;
          h.innerHTML = this.innerHTML;
          y = this.parentNode.getElementsByClassName("same-as-selected");
          yl = y.length;
          for (k = 0; k < yl; k++) {
            y[k].removeAttribute("class");
          }
          this.setAttribute("class", "same-as-selected");
          break;
        }
      }
      h.click();
    });
    b.appendChild(c);
  }
  x[i].appendChild(b);
  a.addEventListener("click", function (e) {
    /*when the select box is clicked, close any other select boxes,
    and open/close the current select box:*/
    e.stopPropagation();
    closeAllSelect(this);
    this.nextSibling.classList.toggle("select-hide");
    this.classList.toggle("select-arrow-active");
  });
}
function closeAllSelect(elmnt) {
  /*a function that will close all select boxes in the document,
except the current select box:*/
  var x, y, i, xl, yl, arrNo = [];
  x = document.getElementsByClassName("select-items");
  y = document.getElementsByClassName("select-selected");
  xl = x.length;
  yl = y.length;
  for (i = 0; i < yl; i++) {
    if (elmnt == y[i]) {
      arrNo.push(i)
    } else {
      y[i].classList.remove("select-arrow-active");
    }
  }
  for (i = 0; i < xl; i++) {
    if (arrNo.indexOf(i)) {
      x[i].classList.add("select-hide");
    }
  }
}
/*if the user clicks anywhere outside the select box,
then close all select boxes:*/
document.addEventListener("click", closeAllSelect);


// enhancing bootstrap dropdown
$(document).ready(function () {
  // Prevent dropdown from closing when clicking inside the dropdown menu
  $('.dropdown-menu').on('click', function (e) {
    e.stopPropagation();
  });
});


var showHideLoad = function (hideIndicator) {
  if (typeof hideIndicator === "undefined" || hideIndicator === null) {
    // console.log('data');
    $('#overlay').show();
  } else {
    // console.log('data1');
    $('#overlay').hide();
  }
};

