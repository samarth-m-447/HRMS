<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en" xml:lang="en">
<head>
<title><spring:message code="login.label.hrms"/></title>
<style>
body {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
  background: #000;
}
.middle {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  position: absolute;
  z-index:1;
}
.bar {
  width: 6px;
  height: 50px;
  background: lightskyblue;
  display: inline-block;
  transform-origin: bottom center;
  border-top-right-radius: 20px;
  border-top-left-radius: 20px;
  /*   box-shadow:5px 10px 20px inset rgba(255,23,25.2); */
  animation: loader 1.2s linear infinite;
}
.bar1 {
  animation-delay: 0.01s;
}
.bar2 {
  animation-delay: 0.02s;
}
.bar3 {
  animation-delay: 0.03s;
}
.bar4 {
  animation-delay: 0.04s;
}
.bar5 {
  animation-delay: 0.05s;
}
.bar6 {
  animation-delay: 0.06s;
}
.bar7 {
  animation-delay: 0.07s;
}
.bar8 {
  animation-delay: 0.08s;
}

@keyframes loader {
  0% {
    transform: scaleY(0.1);
    background: ;
  }
  50% {
    transform: scaleY(1);
    background: deepskyblue;
  }
  100% {
    transform: scaleY(0.1);
    background: transparent;
  }
}
.loaderDiv{
position: relative;
    width: 100%;
    height: 100%;
    background: rgba(0,0,0,0.5);
    z-index: 1;
    }
    
    
 .spinner {
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  position: absolute;
  z-index:1;
  
  border: 5px solid #f3f3f3;
  border-radius: 50%;
  border-top: 5px solid #3498db;
  width: 40px;
  height: 40px;
  -webkit-animation: spin 2s linear infinite; 
  animation: spin 2s linear infinite;
}
@-webkit-keyframes spin {
  0% { -webkit-transform: rotate(0deg); }
  100% { -webkit-transform: rotate(360deg); }
}
@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
} 

</style>
</head>
<body>
<div class="loaderDiv">
	<!-- <div class="middle" id="middle">
	  <div class="bar bar1"></div>
	  <div class="bar bar2"></div>
	  <div class="bar bar3"></div>
	  <div class="bar bar4"></div>
	  <div class="bar bar5"></div>
	  <div class="bar bar6"></div>
	  <div class="bar bar7"></div>
	  <div class="bar bar8"></div>
	</div> -->
	 <div class="spinner"></div>
</div>

</body>
<script>
$(document).ready(function() {
		$('.loaderDiv').fadeOut(300); //when dom is loaded
});
window.addEventListener("load", (event) => {
	$('.loaderDiv').fadeOut(300); //when the page is reloaded
});
</script>
</html>