$(function(){
	$(".closeBut,.ui-dialog-titlebar-close").click(function(){
		winHide();
	});

});

/**
 * 冻结或解冻用户
 * @param userId 用户ID
 * @param type 1解冻 2冻结
 * @param em
 */
function frozenOrThaw(userId,type,em){
	$.ajax({
		url:ctx+'/studentuser/updateuserstate',
		type:'post',
		dataType:'json',
		data:{'user.userId':userId,'user.isavalible':type},
		success:function(result){
			if(result.success==true){
				var td = $(em).parent('samp').parent('td').parent('tr').children('td')[7];
				if(type==1){
					$(td).text('正常');
					$('#frozenOrThaw'+userId).html('<button  class="btn btn-primary" onclick="frozenOrThaw('+userId+',2,this)">冻结</button>');
				}else if(type==2){
					$(td).text('冻结');
					$('#frozenOrThaw'+userId).html('<button  class="btn btn-primary" onclick="frozenOrThaw('+userId+',1,this)">解冻</button>');
				}
			}else{
				alert(result.message);
			}
		},
		error:function(error){
			
		}
	});
}
///////////////////////////////////////////////////////////////////


///////////////////////////////////////////////////////////////////
/***
 * 初始化修改用户密码窗口
 * @param userId 用户ID
 */
function initUpdatePwd(userId){
	
	$("input[name='user.userId']").val(userId);
}
 



/**
 * 执行修改用户密码
 */
function updateUserPwd(){
	var params = '';
	
	$("#myModal input").each(function(){
		params+=$(this).serialize()+"&";
    });
	$.ajax({
		url:ctx+'/studentuser/updateUserPwd',
		type:'post',
		dataType:'json',
		data:params,
		success:function(result){
			if(result.success ==true ){
				alert("成功更新密码！");
				winHide();
				 window.location.reload();//刷新当前页面.
			}
			if(result.success ==false ){
				alert("密码不一致");				
			}
		},
		error:function(error){
			alert("系统繁忙，请稍后再操作！");
		}
	});
}

function winHide(){
	$("#myModal").hide();
	$("input:password").val('');
	$("#myModal name='[user.userId]'").val(0);
}

/**
 * 用户列表导出
 */
function userExcel(){
	$("#searchForm").prop("action","/studentuser/export");
	$("#searchForm").submit();
	$("#searchForm").prop("action","/studentuser/getuserList");
}