/*登录检查*/
function sendLoginfor(activePrivateKey,accountIdOrName){
		$.ajax({
			type:"POST",
			url:"com.servlet/LoginSuccess",
			data:{
				activePrivateKey:activePrivateKey,
				accountIdOrName:accountIdOrName
			},
			success:function(data){
				alert("您已经获取相关激励");
			},
			complete:function(){
				
			}
		});
	}