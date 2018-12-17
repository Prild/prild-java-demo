/* 问答start */
//时间控件

/* 删除问答 */
function delQuestions(questionsId){
	if(confirm("是否删除此问答?")){
		$.ajax({
			url:ctx+"/questions/del/"+questionsId,
			data:{
			},
			dataType:"json",
			type:"post",
			async:true,
			success:function(result){
				if(result.success==true){
					window.location.reload();
				}else{
					dialog('提示信息',result.message,1);
				}
			}
		})
	}
}
/* 问答end */


