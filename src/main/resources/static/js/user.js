let index = {
	init: function() {
		$("#btn-save").on("click", () => { // function() >> ()=>를 사용한 이유는 this를 바인딩 하기 위해서.
			this.save();
		});
	},
	
	save: function() {
		// alert('user의 save함수 호출됨');
		let data = {
			username : $("#username").val(),
			password : $("#password").val(),
			email : $("#email").val()
		};
		
		//console.log(data);	
		
		// ajax호출시, default가 비동기 호출 
		// ajax통신을 이용해서 3개의 데이터를 json으로 변경하여, insert 요청
		$.ajax({
			type:"POST",
			url : "/blog/api/user",
			data : JSON.stringify(data), // http body데이터
			contentType: 'application/json',
			dataType : "json" // 요청을 서보로해서 응답이 왔을 때,  기본적으로 모든것이 문자열 (생긴게 json이라면, ) => javascript오브젝트로 변경
		}).done(function(resp){
			alert("회원가입이 완료 되었습니다.");
			//console.log(resp);
			location.href="/blog";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	}
}
index.init();