package com.vishal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.vishal.bean.Message;
import com.vishal.bean.User;
import com.vishal.service.PeopleService;

@RestController
public class PeopleRESTController {

	@Autowired
	PeopleService peopleService;

	@PostMapping("/addUser/{type}")
	public String addUser(@RequestPart("user") User u, @PathVariable String type, MultipartFile photo) {
		return peopleService.addUser(u, type, photo);
	}

	@PostMapping("/login/{email}")
	public String login(@PathVariable String email) {
		return peopleService.login(email);
	}

	@GetMapping("/getUserByAccountType/{type}/{email}")
	public User getUserByAccountType(@PathVariable String type, @PathVariable String email) {
		return peopleService.getUserByAccountType(type, email);
	}

	@GetMapping("/getUserByEmail/{email}")
	public User getUserByEmail(@PathVariable String email) {
		return peopleService.getUserByEmail(email);
	}

	@GetMapping("/getUserSearch/{state}/{city}/{area}/{email}")
	public List<User> getUserSearch(@PathVariable String state, @PathVariable String city, @PathVariable String area,
			@PathVariable String email) {
		if (area.equals("nodata")) {
			area = "";
		}
		return peopleService.getUserSearch(state, city, area, email);
	}

	@GetMapping("/getMessages/{semail}/{remail}")
	public List<Message> getMessages(@PathVariable String semail, @PathVariable String remail) {
		return peopleService.getMessages(semail, remail);
	}

	@GetMapping("/getPhoto/{email}")
	public byte[] getPhoto(@PathVariable String email) {
		byte[] img = peopleService.getPhoto(email);
		if (img != null) {
			return img;
		}
		return null;
	}

	@GetMapping("/downloadFile/{id}")
	public byte[] downloadFile(@PathVariable int id) {
		byte[] b = peopleService.downloadFile(id);
		if (b != null) {
			return b;
		} else {
			return null;
		}
	}

	@PostMapping("/sendMessage")
	public String sendMessage(@RequestPart("message") Message m, MultipartFile file) {
		return peopleService.sendMessage(m, file);
	}

	@PostMapping("/sendMessageWithoutFile")
	public String sendMessageWithoutFile(@RequestBody Message m) {
		return peopleService.sendMessageWithoutFile(m);
	}

}
