 let totalData; //총 데이터 수
let dataPerPage; //한 페이지에 나타낼 글 수
let pageCount; //페이징에 나타낼 페이지 수
let dataList; //표시하려하는 데이터 리스트
let searchTypeKeyword;
let pagination;
let formData;
let argsMap = new Map();
  
let curPage = 1;
let searchType = "title";
let keyword = "";

 displayMainContent(curPage);
 
function setCookie(cName, cValue) {
	document.cookie = cName + "=" + encodeURIComponent(cValue) + ";path=/";
}


function searchByKeyword(page, searchType, keyword) {
  	setCookie("page", page);
  	setCookie("searchType", searchType);
  	setCookie("keyword", keyword);
  	
    $.ajax({        
        url : "/api/board/list",
        method: "GET",
        dataType : "json",
        success : function(data){
  
 			
        	//totalData(총 데이터 수) 구하기        	
	   		totalData = data.page.count;
           	//데이터 대입
            dataList = data.boardList;
	  		
            dataPerPage = data.page.postNum;
			pageCount = data.page.pageNumCnt;
			
			pagination = data.page; 
			
			argsMap.set('searchType', pagination.searchType);
			argsMap.set('keyword', pagination.keyword);
						     
			//const url = "?page="+page+pagination.searchTypeKeyword;
	        //history.pushState(null, null, url);
		
			$("#searchType").val(pagination.searchType);
			$("#keyword").val(pagination.keyword);
			

			//글 목록 표시 호출 (테이블 생성)
			displayData();
			 
			//페이징 표시 호출
			paging();
			console.log(data)
			const regBtn =	'<button id="regBtn" class="btn btn-outline-secondary" onclick="displayAddBoardForm('+page+' ,'+data.userId+')" type="button">등록</button>';
			const loginBtn =	'<button id="login" class="btn btn-outline-secondary" type="button">로그인</button>';
			const logoutBtn =	'<button id="logout" class="btn btn-outline-secondary" type="button">로그아웃</button>';
			
			if (data.isLogged) {
				$("#frm").html(regBtn);
				$("#frm").append(logoutBtn);
			} else {
				$("#frm").html(loginBtn);
			}

			$("#login").click(function() {
				login(argsMap);
			})
			
			$("#logout").click(function() {
				logout(argsMap);
				$("#frm").html(loginBtn);
				searchByKeyword(page, searchType, keyword)
			})
        }
    });
}

function displayMainContent(curPage) {
	argsMap.set('status', 'main');
  	argsMap.set('curPage', curPage);
	
	const mainContent = '<nav class="navbar bg-light">'
		+ '<form id="searchFrm" class="d-flex " role="search" name="searchFrm">'
		+	'<label class="visually-hidden" for="searchType">Preference</label>'
		+	'<div class="row navmarg">'
		+		'<div class="col-4">'
		+			'<select class="form-select" name="searchType" id="searchType">'
		+	      		'<option value="title" <c:if test="${page.searchType eq \'title\'}">제목</option>'
		+	        	'<option value="content" <c:if test="${page.searchType eq \'content\'}">내용</option>'
		+	      		'<option value="title_content" <c:if test="${page.searchType eq \'title_content\'}">제목+내용</option>'
		+	      		'<option value="writer" <c:if test="${page.searchType eq \'writer\'}">작성자</option>'
		+	  		'</select>'
		+	  	'</div>'
		+	  	'<div class="col-5">'	
		+			'<input class="form-control" type="text" placeholder="Search" aria-label="Search" name="keyword" id="keyword"/>'
		+  	 	'</div>'	
		+		'<div class="col-3">'	
		+  	 		'<button id="searchBtn" class="btn btn-outline-secondary">검색</button>'
		+  	 	'</div>'
	  	+ 	'</div>'
		+'</form>'
	+'</nav>'		
	
	
	+'<table class="table table-bordered table-hover">'
	+	'<thead>'
	+		'<tr class="text-center">'
	+			'<th>번호</th>'
	+			'<th>제목</th>'
	+			'<th>작성일</th>'
	+			'<th>작성자</th>'
	+			'<th>조회수</th>'
	+			'<th>첨부 파일</th>'
	+		'</tr>'
	+	'</thead>' 
	+	'<tbody>'
	
	+	'</tbody>'	
	+'</table>'
	
	+'<nav>'
	+	'<ul class="pagination">'
	
	+	'</ul>'	
	+'</nav>'

	+'<form id="frm">'		
	+'</form>';

	$("#bodyContent").html(mainContent);

	searchByKeyword(curPage, searchType, keyword);
	
	$("#searchBtn").on("click", function(e) {
		e.preventDefault();
		searchByKeyword(1, $("#searchType").val(), $("#keyword").val());
	});
	
	
//	$("#keyword").on("keypress", function() {
//		searchByKeyword(1, $("#searchTarype").val(), $("#keyword").val());
//	});

	
}  
    

//현재 페이지(currentPage)와 페이지당 글 개수(dataPerPage) 반영
function displayData() {
	
  let show = "";
  let postNum = pagination.postNum;
  if(postNum>dataList.length){
	  postNum=dataList.length
  }

  for (let i = 0; i < postNum; i++) {

	  if (dataList[i].user === null) {
		  dataList[i].writer = "";
	  } else {
		  dataList[i].writer = dataList[i].user.username;
	  }
	  
	  if (dataList[i].files.length > 0) {
	      show += "<tr><td class=\"column-width\">" + dataList[i].bno +
	      			"</td><td class=\"clip\"><button type=\"button\" onclick=\"displayViewBoardForm("+dataList[i].bno+", "+pagination.num+")\" class=\"btn btn-link\">"+dataList[i].title+
				    "</button></td><td class=\"column-width\">" +dataList[i].regDate+
	      			"</td><td class=\"column-width\">" 		+dataList[i].writer+
	      			"</td><td class=\"column-width\">" 		+dataList[i].viewCnt+
	      			"</td><td class=\"column-width\"><img src=\"image/attach.png\" class=\"file-img\"/>" + 
	      			"</td></tr>";
	  } else {
		  show += "<tr><td class=\"column-width\">" + dataList[i].bno +
	      			"</td><td class=\"clip\"><button type=\"button\" onclick=\"displayViewBoardForm("+dataList[i].bno+", "+pagination.num+")\" class=\"btn btn-link\">"+dataList[i].title+
				    "</button></td><td class=\"column-width\">" +dataList[i].regDate+
	      			"</td><td class=\"column-width\">" 		+dataList[i].writer+
	      			"</td><td class=\"column-width\">" 		+dataList[i].viewCnt+
	      			"</td><td class=\"column-width\">" + 
	      			"</td></tr>";

	  }
      			
  } //dataList는 임의의 데이터임.. 각 소스에 맞게 변수를 넣어주면 됨...
  $("tbody").html(show);
}

function paging() {
  let page = pagination.num;
  let totalPage = Math.ceil(pagination.count / pagination.postNum); //총 페이지 수

  if(page>totalPage){
    page=totalPage;
  }
  
  let pageGroup = Math.ceil(page / pagination.pageNumCnt); // 페이지 그룹
  let last = pageGroup * pagination.pageNumCnt; //화면에 보여질 마지막 페이지 번호
 
  if (last > totalPage) {
    last = totalPage;
  }
 
  let first = last - (pagination.pageNumCnt - 1); //화면에 보여질 첫번째 페이지 번호
  let next = last + 1;
  let prev = first -1;

  let pageHtml = "";

  if (prev > 0) {
    pageHtml += "<li class=\"page-item\"><a class=\"page-link\" href=\"?page=" + prev + pagination.searchTypeKeyword +"\" onclick=\"searchByKeyword("+prev+", '"+pagination.searchType+"', '"+pagination.keyword+"')\" id=\"prev\"><span aria-hidden=\"true\">&laquo;</span></a></li>";
  }
	
 //페이징 번호 표시 
  for (let num = pagination.startPageNum; num <= last; num++) {
    if (page == num) {
      pageHtml += "<li class=\"page-item active\"><a href=\"?page="+num+pagination.searchTypeKeyword+"\" class=\"page-link\" onclick=\"searchByKeyword("+num+", '"+pagination.searchType+"', '"+pagination.keyword+"')\" id=\"num\">"+num+"</a></li>";
	} else {
      pageHtml += "<li class=\"page-item\"><a href=\"?page="+num+pagination.searchTypeKeyword+"\" class=\"page-link\" onclick=\"searchByKeyword("+num+", '"+pagination.searchType+"', '"+pagination.keyword+"')\" id=\"num\">"+num+"</a></li>";
    }
  }//	setCookie("username", username);
//	setCookie("password", password);
 
  if (last < totalPage) {
    pageHtml += "<li class=\"page-item\"><a class=\"page-link\" href=\"?page=" + next + pagination.searchTypeKeyword +"\" onclick=\"searchByKeyword("+next+", '"+pagination.searchType+"', '"+pagination.keyword+"')\" id=\"next\"><span aria-hidden=\"true\">&raquo;</span></a></li>"
  }

  $(".pagination").html(pageHtml);
 
  //페이징 번호 클릭 이벤트 
  $(".pagination li a").click(function (event) {
    let $id = $(this).attr("id");
    let selectedPage = $(this).text();

	event.preventDefault();

	//history.pushState(null, null, $(this).attr("href"));
	
    if ($id == "next") selectedPage = next;
    if ($id == "prev") selectedPage = prev;


  });
}
    

 
function displayAddBoardForm(page, userId) {
	
	formData = new FormData();
	
	const addBoardFrm = '<div class="insert">'
	+'<form id="frm">'
	+	'<p>'		
	+		'<label>제목</label>'
	+		'<input type="text" class="form-control" name="title" id="title"/>'
	+	'</p>'
	+	'<p>'
	+		'<label>작성자</label>'
	+		'<input type="text" class="form-control" name="writer" id="writer"/>'
	+		'<input type="number" class="form-control" name="user" id="user" readonly="readonly" hidden/>'
	+	'</p>'
	+	'<div class="fileblock">'
	+	'<p>'
	+		'<label for="files">파일 첨부</label>' 
	+		'<input id="files" type="file" name="files" multiple/>'
	+	'</p>'
	+	'</div>'
	+	'<p>'
	+		'<div class="file-list"></div>'
	+	'</p>'	
	+	'<p>'
	+		'<label>내용</label>'
	+		'<textarea name="content" id="content" class="form-control" cols="100" rows="10"></textarea>'
	+	'</p>'
	+	'<button id="save" class="btn btn-outline-dark" type="button">작성</button>'

	+'</form>'
+'</div>'

	$("#bodyContent").html(addBoardFrm);
 
 	
  
	$('#save').on('click',function(){
		addBoard(page, userId);
	});
	
	backBtn = "<button id=\"boardListBtn\" onclick=\"displayMainContent("+page+"); searchByKeyword("+page+", '"+pagination.searchType+"', '"+pagination.keyword+"')\" type=\"button\" class=\"btn btn-outline-dark\">취소</button>";
	$("#frm").append(backBtn)
  
  	$('#files').on('change',function(){
		addFile(this, formData);
	});

  
}

function addBoard(curPage, userId) {
	let objArr = [];	
	objArr.push({	 
		title: $('#title').val(), 
		writer: $('#writer').val(),
		user: {id: userId},
		content: $('#content').val(),
		});		
	
	if (isEmpty($("#title").val())) {
		alert("제목은 필수 입력 항목입니다.");
		return false;
	}
		
		const blob = new Blob([JSON.stringify(objArr[0])], {type: "application/json"});
		formData.append("board", blob);
	
	if (confirm("저장 하시겠습니까?")) {
		$.ajax({
		  url 	  : "/api/board",
		  method  : "POST",
		  data: formData,
	      processData: false,
	      "mimeType": "multipart/form-data",
	      contentType: false,
	      timeout: 0,
		  success: function(response) {
	        console.log('Success:', response);
	 
	        $("#addFrm").hide();
	        displayMainContent(curPage);
	        searchByKeyword(curPage, pagination.searchType, pagination.keyword);
	      },
	    	error: function(error) {
	        console.log('Error:', error);
	      }
		});
	}	
}

let makeLink = false;
let board;
function displayViewBoardForm(bno, curPage) {
	argsMap.set('status', 'view');
  	argsMap.set('bno', bno);
  	argsMap.set('curPage', curPage);
  	
	const viewFrm = '<div class="insert">'
	+'<div id="viewTitle"></div>'	
	+'<div id="viewWriter"></div>'		
	+'<div id="viewContent"></div>'	
	+'<div class="fileblock">'	
	+'</div>'	
	+'<div class="file-list"></div>'	
	+'<div id="groupBtn"></div>'
	+'<br>'	
 //댓글 시작
	+'<div id="replies" class="reply-width"></div>'
	+'<p>'
	+'<div class="col-8" id="replyList"></div>'
	+'<div id="comment"></div>'
	+'</p>'

	+'<div id="rc_box">'
	+'</div>'
+'</div>'
//댓글 끝

	setCookie("bno", bno);
	$.ajax({
		 url : 	'/api/board',
		 method: 'GET',
		 dataType: 'json',
		 success: function(data) {
			//console.log(data);
			$("#bodyContent").html(viewFrm);
			board = data.board;
			
			writer = board.writer = board.user === null ? "" : board.user.username;
			files = board.files;
						
			titleHtml = "<h2>" + board.title + "</h2><hr />";
		  	writerHtml = "<span>작성자: </span>" + board.writer + "<hr />";
		  	contentHtml = board.content + "<hr />";
		  	$("#viewTitle").html(titleHtml);
		  	$("#viewWriter").html(writerHtml);
		  	$("#viewContent").html(contentHtml);
			
			
			showFilesAfterDeletion(board);
			
			editBtn = "<button id=\"editBtn\" onclick=\"displayUpdateBoardForm("+bno+" ,"+curPage+")\" type=\"button\" class=\"btn btn-outline-secondary\">게시물 수정</button>";
			delBtn = "<button id=\"delBtn\" onclick=\"displayMainContent("+curPage+"); deleteBoard("+board.bno+", "+curPage+")\" type=\"button\" class=\"btn btn-outline-secondary\">게시물 삭제</button>";
			boardListBtn = "<button id=\"boardListBtn\" onclick=\"displayMainContent("+curPage+"); searchByKeyword("+curPage+", '"+pagination.searchType+"', '"+pagination.keyword+"')\" type=\"button\" class=\"btn btn-outline-secondary\">게시글 목록</button>";
			if (data.isLogged) {
				$("#groupBtn").append(editBtn);
				$("#groupBtn").append(delBtn);
			}	
			$("#groupBtn").append(boardListBtn);

			showReply(bno, curPage);

			tbox = '<img src=\"image/avatar.png\" class=\"file-img\" /><span>'+ writer +'</span>'
			+ '<textarea id="replyContent" class="form-control" rows="6" cols="95" name="content"></textarea>'
			+'<p>'
				+'<button id="addReplyBtn" class="btn btn-outline-dark" type="button">댓글 작성</button>'
			+'</p>';
			rcBoxLabel = '<label for="replyContent" class="rc_box_label">댓글을 작성하려면 로그인 해주세요</label>';

			if (data.userId != board.user.id) {
				$("#rc_box").append(rcBoxLabel);
			} else {
				$("#rc_box").append(tbox);
			}	 

			$(".rc_box_label").click(function() {
				if (confirm("로그인을 하신 후 이요해 주시기 바랍니다.")) {
					 login(argsMap);
				 } else {
					 return false;
				 }
			})
		
			$("#addReplyBtn").click(function() {
				addReply(bno, curPage, writer);
			});
							
		 },
         error: function(e){
                console.log(e);
         }
	});
	  	
}


function displayUpdateBoardForm(bno, curPage) {
	formData = new FormData;

	const updateBoardFrm = '<div class="insert">'
	+'<form id="frm">'
	+	'<p>'		
	+		'<label>제목</label>'
	+		'<input type="text" class="form-control" name="title" id="editTitle" required/>'
	+	'</p>'
	+	'<p>'
	+		'<label>작성자</label>'
	+		'<input type="text" class="form-control" name="writer" id="editWriter"/>'
	+	'</p>'
	+	'<div class="fileblock">'
	+	'<p>'
	+		'<label for="files">파일 첨부</label>' 
	+		'<input id="files" type="file" name="files[]" multiple/>'
	+	'</p>'
	+	'</div>'
	+	'<p>'
	+		'<div class="file-list"></div>'
	+	'</p>'	
	+	'<p>'
	+		'<label>내용</label>'
	+		'<textarea name="content" id="content" class="form-control" cols="100" rows="10"></textarea>'
	+	'</p>'
	+	'<button id="save" class="btn btn-outline-dark" type="button">수정</button>'

	+'</form>'
+'</div>'

	$("#bodyContent").html(updateBoardFrm);

	board.writer = board.user === null ? "" : board.user.username;
  	$("#editTitle").val(board.title);
  	$("#editWriter").val(board.writer);
  	$("#content").val(board.content);

	backBtn = "<button id=\"boardListBtn\" onclick=\"displayViewBoardForm("+bno+" ,"+curPage+")\" type=\"button\" class=\"btn btn-outline-dark\">취소</button>";
	$("#frm").append(backBtn);

	showFiles(board);

	$('#files').on('change',function(){
		addFile(this, formData);
	});
	
	$('#save').click(function() {
		updateBoard(board, curPage, formData);
	});
	
}

function updateBoard(board, curPage, formData) {

	let boardJson = [];
		boardJson.push({
				 bno: board.bno,
				 uuid: board.uuid,
				 title: $('#editTitle').val(),
				 writer: $('#editWriter').val(),
				 content: $('#content').val()
		});	
				
		const blob = new Blob([JSON.stringify(boardJson[0])], {type: 'application/json'});
		
	if (isEmpty($("#editTitle").val())) {
		alert("제목은 필수 입력 항목입니다.");
		return false;
	}
		
		formData.append('board', blob);
		
	if (!confirm("수정 하시겠습니까?")) {
		displayViewBoardForm(board.bno, page);
	} else {	
		
		$.ajax({
			 url : 	'/api/board/edit?uuid=' + board.uuid,
			 method: 'POST',
			 data: formData,
			 processData: false,
			 'mineType': 'multipart/form-data',
			 contentType: false,
			 timeout: 0,		 
			 success: function(data) {
					console.log('success: ' + data);
				 	displayViewBoardForm(board.bno, curPage);
			 },
	         error: function(e){
	                console.log('error: ' + e);
	         }
		});	
	}	
		 
}	 


function deleteBoard(bno, page) {
	setCookie("bno", bno);
	
	if (!confirm("삭제 하시겠습니까?")) {
		displayViewBoardForm(bno, page);
	} else {
	
		$.ajax({
			 url : 	'/api/board',
			 method: 'POST',
			 processData: false,
			 contentType: false,
			 success: function(data) {
				 console.log('success: ' + data);
	 	 		 searchByKeyword(page, pagination.searchType, pagination.keyword);
			 }
		});
		 $("#viewFrm").hide();
	}

	 
}


let fileNo = 0;
let filesArr = new Array();
let obj;
/* 첨부파일 추가 */
function addFile(obj, formData){
    let maxFileCnt = 5;   // 첨부파일 최대 개수
    let attFileCnt = document.querySelectorAll('.filebox').length;    // 기존 추가된 첨부파일 개수
    let remainFileCnt = maxFileCnt - attFileCnt;    // 추가로 첨부가능한 개수
    let curFileCnt = obj.files.length;  // 현재 선택된 첨부파일 개수

    // 첨부파일 개수 확인
    if (curFileCnt > remainFileCnt) {
        alert("첨부파일은 최대 " + maxFileCnt + "개 까지 첨부 가능합니다.");
    }
	
    for (let i = 0; i < Math.min(curFileCnt, remainFileCnt); i++) {

        file = obj.files[i];
 		
        // 첨부파일 검증
        if (validation(file)) {
            // 파일 배열에 담기
            let reader = new FileReader();
            reader.onload = function () {
                filesArr.push(file);
             };
            reader.readAsDataURL(file)
                      
            // 목록 추가
            let htmlData = '';
            htmlData += '<div id="file' + fileNo + '" class="filebox">';
            htmlData += '   <p class="name">' + file.name + '</p>';
            htmlData += '   <a class="delete" onclick="removeFile(' + fileNo + ')"><i class="far fa-minus-square"></i></a>';
            htmlData += '</div>';
            $('.file-list').append(htmlData);
            fileNo++;
        } else {
            continue;
        }
 		formData.append('files', file);
  		
    }

	
    // 초기화
    document.querySelector("input[type=file]").value = "";
}

/* 첨부파일 검증 */
function validation(obj){
    const fileTypes = ['application/pdf', 'image/gif', 'image/jpeg', 'image/png', 'image/bmp', 'image/tif', 'application/haansofthwp', 'application/x-hwp'];
    if (obj.name.length > 100) {
        alert("파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    } else if (obj.size > (100 * 1024 * 1024)) {
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    } else if (obj.name.lastIndexOf('.') == -1) {
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    } else if (!fileTypes.includes(obj.type)) {
        alert("첨부가 불가능한 파일은 제외되었습니다.");
        return false;
    } else {
        return true;
    }
}


function removeFile(num) {
    document.querySelector("#file" + num).remove();
    filesArr[num].is_delete = true;
   	formData.delete("files");
    //filesArr.splice(num, 1);

}

/* 첨부파일 삭제 */
function deleteFile(num, fno) {
    document.querySelector("#file" + num).remove();
    filesArr[num].is_delete = true;
 	
    setCookie("fno", fno);
    $.ajax({
		url : "/api/storage",
		type : "DELETE",
		dataType: "json",
		success : function(data) {
			console.log('success: ' + data);

		},
		error: function(e) {
			console.log('error: ' + e);
		}
		
	});
}

function showFiles(board) {
	makeLink = false;
	for (let i = 0; i < board.files.length; i++) {
	
		const file = board.files[i];
		filesArr.push(file);
		           // 목록 추가
        let htmlData = '';
        htmlData += '<div id="file' + fileNo + '" class="filebox">';
        if (makeLink) {
			 htmlData += ' <a class="name" download="'+file.name+'" href="http://localhost:8080/download/'+file.name+'">' + file.name + '</a>';
		} else {
			htmlData += ' <span class="name">' + file.name + '</span>';
	        htmlData += '   <a class="delete" onclick="deleteFile(' + fileNo + ', ' + file.fno +');"><i class="far fa-minus-square"></i></a>';
		}
        htmlData += '</div>';
        $('.file-list').append(htmlData);
        fileNo++;

	}

}


function showFilesAfterDeletion(board) {
	$.ajax({
	 url : 	'/api/storage/files?uuid=' + board.uuid,
	 method: 'GET',
	 dataType: 'json',
	 success: function(data) {
	
		makeLink = true;
		$.each(data.listFiles, function(key, value) {
			const file = value;
			filesArr.push(file);
			           // 목록 추가
	        let htmlData = '';
	        htmlData += '<div id="file' + fileNo + '" class="filebox">';
	        if (makeLink) {
				 htmlData += ' <a class="name" download="'+file.name+'" href="http://localhost:8080/download/'+file.name+'">' + file.name + '</a>';
			} else {
				htmlData += ' <span class="name">' + file.name + '</span>';
	        	htmlData += '   <a class="delete" onclick="deleteFile(' + fileNo + ');"><i class="far fa-minus-square"></i></a>';
			}
	        htmlData += '</div>';
	        $('.file-list').append(htmlData);
	        fileNo++;
		});
	 },
     error: function(e){
            console.log('error: ' + e);
     }
  });
}

let replyList;
function showReply(bno, curPage) {
	 let show = Array();
	 $.ajax({        
	        url : "/api/reply/list",
	        data: {"bno": bno},
	        method: "GET",
	        dataType : "json",
	        success : function(data){
				replyList = data
				$.each(replyList, function(num, reply) {
						show.push('<p>'+reply.writer+ '  |  ' + reply.regDate 
					   + '<button type="button" id="editReplyBtn" class="btn btn-link" onclick="showEditReplyFrm('+reply.bno+", "+reply.rno+", "+num+", "+curPage+')">수정</button>/'
					   + '<button type="button" id="deleteReplyBtn" class="btn btn-link" onclick="deleteReply('+reply.bno+', '+reply.rno+', '+curPage+')">삭제</button></p>'
					   + '<p>'+reply.content+'</p><hr />');
				});
					$("#replyList").html(show);	   
			}
	});
}
	
function isEmpty(str) {
	return !str.trim().length;
}

function addReply(bno, curPage, writer) {

	if (isEmpty($('#replyContent').val())) {
		 alert("댓글 내용은 필수 입력 항목입니다.")
		 return false;
	 }
	 
	 $.ajax({
		 url : 	'/api/reply/add',
		 method: 'POST',
		 dataType: 'json',
		 contentType: 'application/json; charset=utf-8',
		 data: JSON.stringify({
			 bno:	bno,
			 writer: writer,
			 content: $('#replyContent').val()}),
		 success: function(data) {
			console.log('success: ' + data);
			console.log("add="+curPage)
			showReply(bno, curPage);
		 }
	 });

}

function deleteReply(bno, rno, curPage) {
	setCookie("bno", bno);
	setCookie("rno", rno);
	
	if (!confirm("삭제 하시겠습니까?")) {
		showReply(bno, curPage);
	} else {
		
		$.ajax({
			 url : 	'/api/reply',
			 method: 'POST',
			 processData: false,
			 contentType: false,
			 success: function(data) {
				console.log('success: ' + data);
				showReply(bno, curPage);
			 }
		 });
	}

}

function showEditReplyFrm(bno, rno, num, curPage) {
	
	const addReplyForm = '<div class="insert">'
	+'<form>'
		+'<p>'
		+'<label>댓글 작성자</label><input type="text" id="writer" class="form-control" name="writer" readonly="readonly">'
		+'</p>'
		+'<p>'
		+'<textarea class="form-control" id="content" rows="10" cols="100" name="content"></textarea>'
		+'</p>'
		+'<p>'					
		+'<button id="edit" type="button" class="btn btn-outline-dark">댓글 수정</button>'
		+'</p>'
	+'</form>'
	'</div>';
	
	$("#bodyContent").html(addReplyForm);
	
	$("#writer").val(replyList[num].writer);
	$("#content").val(replyList[num].content);
	
	$("#edit").click(function() {
		updateReply(bno, rno, curPage);
	})
	
}	

function updateReply(bno, rno, curPage) {
	
	 if (isEmpty($('#writer').val())) {
		 alert("댓글 작성자는 필수 입력 항목입니다.")
		 return false;
	 }

 	if (isEmpty($('#content').val())) {
		 alert("댓글 내용은 필수 입력 항목입니다.")
		 return false;
	 }
	 
		 $.ajax({
		 url : 	'/api/reply',
		 method: 'PUT',
		 dataType: 'json',
		 contentType: 'application/json; charset=utf-8',
		 data: JSON.stringify({
			 bno: bno,
			 rno: rno,
			 writer: $('#writer').val(),
			 content: $('#content').val()}),
		 success: function(data) {
			 console.log('success: ' + data);
			 console.log("update="+curPage)
			 displayViewBoardForm(bno, curPage);
		 }
	 });
}

function login(argsMap) {
	
	const login = '<form class="login">'
    +'<p">'
        +'<h2>로그인 해 주세요</h2>'
    +'</p>'

    +'<p>'
    +    '<label>'
    +        '<input id="username" type="text" class="form-control" name="username" placeholder="admin">'
    +    '</label><br><br>'
    +'</p>'

    +'<p">'
    +    '<label>'
    +        '<input id="password" type="password" class="form-control" name="password" placeholder="1234">'
    +    '</label><br><br>'
    +'</p>'

    +'<p>'
    +    '<button id="loginBtn" type="button" class="btn btn-dark btn-sm btn-block">로그인</button>'
    +'</p>'
	+'</form>';
	
	$("#bodyContent").html(login);	
	
	$("#loginBtn").click(function() {
	let username = $("#username").val();
	let password = $("#password").val();

	console.log(username)
	console.log(password)
		
		$.ajax({
			 url : 	'/api/login',
			 type: 'POST',
			 contentType: 'application/json; charset=utf-8',
			 data: JSON.stringify({
				 username: username,
				 password: password}),
			 success: function(data) {
				 console.log(data);
				 if (argsMap.get('status') === 'view') {
				 	displayViewBoardForm(argsMap.get('bno'), argsMap.get('curPage'));
				 } else if (argsMap.get('status') === 'main') {
					displayMainContent(argsMap.get('curPage'));
					searchByKeyword(argsMap.get('curPage'), argsMap.get('searchType'), argsMap.get('keyword'));
				 }	
			 }
		 });
	});	 
	
}

function logout(argsMap) {
	$.ajax({
		url: '/api/logout',
		method: 'GET',
		dataType: 'json',
		success: function(data) {
//			if (argsMap.get('status') === 'main') {
//				displayMainContent(argsMap.get('curPage'));
//				searchByKeyword(argsMap.get('curPage'), argsMap.get('searchType'), argsMap.get('keyword'));
//			}
		}
		
	})
}
