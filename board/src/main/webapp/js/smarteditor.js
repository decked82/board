var oEditors = [];

$(document).ready(function() {
 nhn.husky.EZCreator.createInIFrame({
 oAppRef: oEditors,
 elPlaceHolder: "content",
 sSkinURI: "/SmartEditor2Skin.html",
 htParams:{
	bUseToolbar : true,
	bUseVerticalResizer: true,
	bUseModeChanger: true
 },
 fOnAppLoad: function() {
	$("iframe").css("width", "100%").css("height", "300px"); 
 },
fCreator: "createSEditor2"
});
})
  
//저장버튼 클릭시 form 전송
$("#save").click(function(){
	oEditors.getById["content"].exec("UPDATE_CONTENTS_FIELD", []);
    $("#frm").submit();
});      

 