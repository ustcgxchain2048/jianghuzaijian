var board=new Array();
var score=0;
var hasConflicted=new Array();
var startx=0;
var starty=0;
var endx=0;
var endy=0;

//总的执行步数
var sumPoint = 0;

/*
	总的执行函数
*/
$(document).ready(function(){
	prepareForMobile();
	newgame();
});
/*
	设计页面
*/
function prepareForMobile(){
	if(documnetWidth>500){
		gridContainerWidth=500;
		cellSpace=20;
		cellSideLength=100;
	}

	$('#grid-container').css('width',gridContainerWidth - 2*cellSpace);
	$('#grid-container').css('height',gridContainerWidth - 2*cellSpace);
	$('#grid-container').css('padding',cellSpace);
	$('#grid-container').css('border-radius',0.02*gridContainerWidth);

	$('.grid-ceil').css('width',cellSideLength);
	$('.grid-ceil').css('height',cellSideLength);
	$('.grid-ceil').css('border-radius',cellSideLength*0.02);
}

function newgame(){
	//初始化棋盘格
	init();
	//在随机两个格子生成数字
	generateOneNumber();
	generateOneNumber();
}

function init(){
	for(var i=0;i<4;i++){
		for(var j=0;j<4;j++){
			var gridCell=$('#grid-ceil-'+i+'-'+j);
			gridCell.css('top',getPosTop(i,j));
			gridCell.css('left',getPosLeft(i,j));
		}
	}
	for(var i=0;i<4;i++){
		board[i]=new Array();
		hasConflicted[i]=new Array();
		for(var j=0;j<4;j++){
			board[i][j]=0;
			hasConflicted[i][j] = false;
		}
	}
	updateBoardView();
	score=0;
	updateScore(score);
}

function updateBoardView(){
	$(".number-cell").remove();
	for(var i=0;i<4;i++){
		for(var j=0;j<4;j++){
			$("#grid-container").append('<div class="number-cell" id="number-cell-'+i+'-'+j+'"></div>');
			var theNnumberCell=$('#number-cell-'+i+'-'+j);

			if(board[i][j]==0){
				theNnumberCell.css('width','0px');
				theNnumberCell.css('height','0px');
				theNnumberCell.css('top',getPosTop(i,j)+cellSideLength/2);
				theNnumberCell.css('left',getPosLeft(i,j)+cellSideLength/2);
			}else{
				theNnumberCell.css('width',cellSideLength);
				theNnumberCell.css('height',cellSideLength);
				theNnumberCell.css('top',getPosTop(i,j));
				theNnumberCell.css('left',getPosLeft(i,j));
				theNnumberCell.css('background-color',getNumberBackgroundColor(board[i][j]));
				theNnumberCell.css('color',getNumberColor(board[i][j]));


				theNnumberCell.text(getName(board[i][j]));

			}
			hasConflicted[i][j]=false;
		}
	}
	$('.number-cell').css('line-height',cellSideLength+'px');
	$('.number-cell').css('font-size',0.2*cellSideLength+'px');
}

function generateOneNumber(){
	if(nospace(board)){
		return false;
	}else{

		//随机一个位
		var randx=parseInt(Math.floor(Math.random()*4));
		var randy=parseInt(Math.floor(Math.random()*4));
		var times=0;
		while(times<50){
			if(board[randx][randy]==0){
				break;
			}else{
				var randx=parseInt(Math.floor(Math.random()*4));
				var randy=parseInt(Math.floor(Math.random()*4));
			}
			times++;
		}
		if(times>=50){
			for(var i=0;i<4;i++){
				for(var j=0;j<4;j++){
					if(board[i][j]==0){
						randx=i;
						randy=j;
					}
				}
			}
		}
		//随机一个数字
		var randNumber=Math.random()<0.5? 2 : 4;
		//显示随机的数字
		board[randx][randy]=randNumber;
		showNumberWithAnimation(randx,randy,randNumber);
		return true;
	}
}

$(document).keydown(function( event ){
	event.preventDefault();//阻挡原本效果，在这里可以阻止滚动条的滚动
	sumPoint ++;
	if(setGameOver(10)){
		switch(event.keyCode){
			case 37://左
				if(moveLeft()){
					setTimeout("generateOneNumber()",210);
					setTimeout("isgameover()",300);
				}
				break;
			case 38://上
				if(moveUp()){
					setTimeout("generateOneNumber()",210);
					setTimeout("isgameover()",300);
				}
				break;
			case 39://右
				if(moveRight()){
					setTimeout("generateOneNumber()",210);
					setTimeout("isgameover()",300);
				}
				break;
			case 40://下
				if(moveDown()){
					setTimeout("generateOneNumber()",210);
					setTimeout("isgameover()",300);
				}
				break;
			default:
				break;
		}
	}
});

document.addEventListener('touchstart',function(event){
	startx=event.touches[0].pageX;
	starty=event.touches[0].pageY;
});

document.addEventListener('touchmove',function(event){
	event.preventDefault();//防止安卓系统的一个bug,具体问题具体对待
});

document.addEventListener('touchend',function(event){
	sumPoint ++;
	if(setGameOver(10)){
		endx=event.changedTouches[0].pageX;
		endy=event.changedTouches[0].pageY;

		var deltax=endx - startx;
		var deltay=endy - starty;

		if(Math.abs(deltax)<0.3*documnetWidth && Math.abs(deltay)<0.3*documnetWidth)
			return;

		if(Math.abs(deltax)>=Math.abs(deltay)){
			//	x轴滑动
			if(deltax>0){
				//向右
				if(moveRight()){
					setTimeout("generateOneNumber()",210);
					setTimeout("isgameover()",300);
				}

			}else{
				//	向左
				if(moveLeft()){
					setTimeout("generateOneNumber()",210);
					setTimeout("isgameover()",300);
				}
			}
		}else{
			//y轴滑动
			if(deltay>0){
				//向下
				if(moveDown()){
					setTimeout("generateOneNumber()",210);
					setTimeout("isgameover()",300);
				}
			}else{
				//向上
				if(moveUp()){
					setTimeout("generateOneNumber()",210);
					setTimeout("isgameover()",300);
				}
			}
		}
	}

	
});

function isgameover(){
	if(nospace(board) && nomove(board)){
		gameover();
	}
}

function gameover(){
	alert("游戏结束了");
}

//向左
function moveLeft(){
	if(!canMoveLeft(board)){
		return false;
	}else{
		//向左移
		for(var i=0;i<4;i++){
			for(var j=1;j<4;j++){
				if(board[i][j]!=0){
					for(var k=0;k<j;k++){
						if(board[i][k]==0 && noBlockHorizontal(i,k,j,board)){
							//移动
							showMoveAnimation(i,j,i,k);
							board[i][k] = board[i][j];
							board[i][j] = 0;
							continue;
						}else if(board[i][k] == board[i][j] && noBlockHorizontal(i,k,j,board) && 
							!hasConflicted[i][k]){

							//移动
							//叠加
							showMoveAnimation(i,j,i,k);
							board[i][k]+=board[i][j];
							board[i][j]=0;
							//加分
							score+=board[i][k];
							updateScore(score);

							hasConflicted[i][k]=true;
							continue;

						}

					}
				}
			}
		}
		setTimeout("updateBoardView()",200);
		return true;
	}
}

//向左
function moveRight(){
	if(!canMoveRight(board)){
		return false;
	}else{
		for(var i=0;i<4;i++){
			for(var j=2;j>=0;j--){
				if(board[i][j]!=0){
					for(var k=3;k>j;k--){
						//alert(i+" "+j+" "+k);
						if(board[i][k]==0 && noBlockHorizontal(i,j,k,board)){
							showMoveAnimation(i,j,i,k);
							board[i][k]=board[i][j];
							board[i][j]=0;
							continue;
						}else if(board[i][k]==board[i][j] && noBlockHorizontal(i,j,k,board) && 
							!hasConflicted[i][k]){
							showMoveAnimation(i,j,i,k);
							board[i][k]+=board[i][j];
							board[i][j]=0;
							//加分
							score+=board[i][k];
							updateScore(score);

							hasConflicted[i][k]=true;
							continue;
						}
					}
				}
			}
		}
		setTimeout("updateBoardView()",200);
		return true;
	}
}

//向上
function moveUp(){
	if(!canMoveUp(board)){
		return false;
	}else{
		for(var j=0;j<4;j++){
			for(var i=1;i<4;i++){
				if(board[i][j]!=0){
					for(var k=0;k<i;k++){
						if(board[k][j]==0 && noBlockHorizontalUp(k,i,j,board)){
							showMoveAnimation(i,j,k,j);
							board[k][j]=board[i][j];
							board[i][j]=0;
							continue;
						}else if(board[k][j]==board[i][j] && noBlockHorizontalUp(k,i,j,board)&&
							!hasConflicted[k][j]){
							showMoveAnimation(i,j,k,j);
							board[k][j]*=2;
							board[i][j]=0;
							//加分
							score+=board[k][j];
							updateScore(score);
							hasConflicted[k][j]=true;
							continue;
						}
					}
				}
			}
		}
		setTimeout("updateBoardView()",200);
		return true;
	}
}

//向下
function moveDown(){
	if(!canMoveDown(board)){
		return false;
	}else{
		for(var j=0;j<4;j++){
			for(var i=2;i>=0;i--){
				if(board[i][j]!=0){
					for(var k=3;k>i;k--){
						if(board[k][j]==0 && noBlockHorizontalUp(i,k,j,board)){
							showMoveAnimation(i,j,k,j);
							board[k][j]=board[i][j];
							board[i][j]=0;
							continue;
						}else if(board[k][j]==board[i][j] && noBlockHorizontalUp(i,k,j,board) &&
							!hasConflicted[k][j]){
							showMoveAnimation(i,j,k,j);
							board[k][j]*=2;
							board[i][j]=0;
							//加分
							score+=board[k][j];
							updateScore(score);

							hasConflicted[k][j]=true;
							continue;
						}
					}
				}
			}
		}
		setTimeout("updateBoardView()",200);
		return true;
	}
}

function setGameOver(backdat){
	if(sumPoint>backdat){
		alert("步数已达到，游戏结束了");
		return false;
	}else{
		return true;
	}
}