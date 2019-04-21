<!doctype html>
<html>
<head>
<meta charset="utf-8">
<script type="text/javascript" src="js/jquery-1.8.3.min.js"></script>
<script type="text/javascript" src="js/operation.js"></script>
<title>镜像导航</title>

<style>
body{
   
   background-repeat: no-repeat;
   background-size:100% 100%;
   background-attachment: fixed;
}
.link{
	margin: auto;
  	text-align: center;
  	padding: 20px;
  	margin: 20px;
}
a{
  text-decoration: none;
  color:white;
}
.butt{
    font-size: 22px;
    border: 20px;
    padding:20px;
    margin-top: 0px;
    margin-bottom: 10px;
    border:2px solid black;
    border-radius: 10px;
    margin:auto;
    background: #6B716F;
    font-color:white;
}
#title{
  font-size: 30px;
  font-family: "微软雅黑";
}
</style>
</head>
<script type="text/javascript">
  function sendRegisfor(){
        $.ajax({
            type:"POST",
            url:"com.servlet/SigleServer",
            data:{
            },
            success:function(data){
                var jdata = eval("("+data+")");
                if(jdata.flag == 1){
                    window.location.href = "index.jsp";
                }else{
                  alert("数据有错误发生");
                }
            }
        });
    }
  function sendTwoRegister(){
    $.ajax({
            type:"POST",
            url:"com.servlet/TwoPeopleServe",
            data:{
            },
            success:function(data){            
                window.location.href = "change.html";
            }
        });
    }
  function sendMoreRegister(){
    $.ajax({
            type:"POST",
            url:"com.servlet/MorePeopleServe",
            data:{
            },
            success:function(data){            
                window.location.href = "morechange.html";
            }
        });
  }
  $(document).ready(function(){
      $("#siglebutton").click(function(){
          sendRegisfor();
	    });
      $("#doublebutton").click(function(){
          sendTwoRegister();
      });
      $("#morebutton").click(function(){
          sendMoreRegister();
      })
      
   });
</script>

<body background = "img/bga.jpg">

<div class="container">
  <div class="link">
    <div class="text" id="title">游戏模式</div>
  </div>
  <div class="link">
    <div class="text">
      <a href="#" id="siglebutton" class="butt">单人模式</a>
    </div>
  </div>
  <div class="link">
    <div class="text">
      <a href="#" id="doublebutton" class="butt">双人模式</a>
    </div>
  </div>
  <div class="link">
    <div class="text">
      <a href="#" id="morebutton" class="butt">多人模式</a>
    </div>
  </div>

  
</div>

<script>
</script>

</body>
</html>
