  
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="sectionContainerCenter">
	<div class="msg">${msg}</div>
		<form id="frm" class="frm" action="/user/join" method="post">
			<div id="idChkResult" class="msg"></div>
			<div>
				<input type="text" name="user_id" placeholder="아이디">
				<button type="button" onclick="chkId()">아이디 중복체크</button>
			</div>
			<div><input type="password" name="user_pw" placeholder="비밀번호"></div>
			<div><input type="password" name="user_pwre" placeholder="비밀번호 확인"></div>
			<div><input type="text" name="nm" placeholder="이름"></div>
			<div><input type="submit" value="회원가입"></div>
		</form>
		<div><a href="/user/login">로그인</a></div>
	</div>
	
	<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
	<script>
	function chkId() {
		const user_id = frm.user_id.value
		/*ajax통신 : refresh 없이 필요한 값만 가져와서 바꾸는것*/
		axios.post('/user/ajaxIdChk', { /*axios : ajax 통신 중에 하나*/
			'user_id': user_id //user_id만 적어도 실행된다. 변수명과 키값이 같으므로!!
				/*'키값' : 밸류*/
		}).then(function (res) {
			console.log(res)
			if(res.data == '2') { // 아이디없음
				idChkResult.innerText = '사용할 수 있는 아이디입니다.'
				
			} else if(res.data == '3') {//아이디 중복됨
				idChkResult.innerText = '이미 사용중입니다.'
			}
		})
	}
	</script>
</div>