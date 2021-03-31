package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//html 이아니라 data를 return해주는 controller = RestController
@RestController 
public class DummyControllerTest {
	
	@Autowired //의존성 주입(DI)
	private UserRepository userRepository;
	
	//save함수는 id를 전달하지 않으면 , insert를 해주고 
	//save함수는 id를 전달하면, id에 대한 데이터가 있으면, update를 해주고 
	//save함수는 id를 전달하면, 해당 아이ㅏ디에 대한 데이터가 없으면 insert를 한다.
	//email, password
	
	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제 실패하였습니다 해당 id는 DB에 없습니다.";
		}
		
		return "삭제 되었습니다 id : " + id;
	}
	
	@Transactional
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) {
		System.out.println("id : " + id); 
		System.out.println("password : " + requestUser.getPassword()); 
		System.out.println("email : " + requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패.");
		});
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		// userRepository.save(user);
		
		//더티 체킹
		return user; 
	}
	
//	https://localhost:8001/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	//한 페이지당 두건의 데이터를 리턴 받아볼꺼야
	@GetMapping("/dummy/user")
	public Page<User> pageList(@PageableDefault(size = 2, sort="id", direction = Sort.Direction.DESC)Pageable pageable) {
		Page<User> pagingUser  = userRepository.findAll(pageable);
		
		 
		List<User> users = pagingUser.getContent();
		return pagingUser;
	}
	
	//{id} 주소로 파라메타를 전달 받을 수 있습니다.
	//https://localhost:8000/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		//user/4번을 찾으면 내가 데이터 베이스에서 못찾아오게 되면,  user가 null이 될 것 아냐?
		// 그럼 return null이 리턴이 되잖아,..  그럼 프로그렘에 문제가 있지 않겠니 ?
		//Optional 로 너의 User객체를 감싸서 가져 올테니 null인지 아닌지 판단해서  return해
		
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
		@Override
		public IllegalArgumentException get() {
			// TODO Auto-generated method stub
			return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
		}
		});
		// 요청 : 웹브라우저
		//user객체 = 자바 오브젝트
		// 변환  : 웹 브라우저가 이해할 수 있는 데이터 -> JSON
		// 스프링 부트 > MessageConverter이라는 애가 응답시에 자동 자동
		//	만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		//user 오브젝트를 json로 변환해서 브라우저에게 던져준다.
		return user;	
	}
	
	//http://localhost:8000/blog/dummy/join(요청) 
	//http의 바디에 username password, eamil을 가지고 요청
	@PostMapping("/dummy/join")
	public String join(User user) {
		System.out.println("id : " + user.getId());
		System.out.println("username : " +user.getUsername());
		System.out.println("password : " +user.getPassword());
		System.out.println("email : " +user.getEmail());
		System.out.println("role : " + user.getRole());
		System.out.println("createDate : " + user.getCreateDate());
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다";
	}
}
