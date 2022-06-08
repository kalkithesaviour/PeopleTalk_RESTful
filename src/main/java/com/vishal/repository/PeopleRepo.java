package com.vishal.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import com.vishal.bean.Message;
import com.vishal.bean.User;

@Repository
public class PeopleRepo {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public String addUser(User u, String type, MultipartFile photo) {
		try {
			String query = "INSERT INTO users (email, name, phone, gender, dob, state, city, area, password, photo, account_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(query,
					new Object[] { u.getEmail(), u.getName(), u.getPhone(), u.getGender(), u.getDob(), u.getState(),
							u.getCity(), u.getArea(), u.getPassword(), photo.getInputStream(), type });
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "failed";
		}
	}

	public String login(String email) {
		class DataMapper implements RowMapper<String> {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString("password");
			}
		}
		String dbHashedPassword = null;
		try {
			final String query = "SELECT password FROM users WHERE email = ?";
			dbHashedPassword = jdbcTemplate.queryForObject(query, new DataMapper(), new Object[] { email });
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
		}
		return dbHashedPassword;
	}

	public User getUserByAccountType(String type, String email) {
		class DataMapper implements RowMapper<User> {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u = new User();
				u.setName(rs.getString("name"));
				u.setEmail(rs.getString("email"));
				u.setPhone(rs.getString("phone"));
				u.setGender(rs.getString("gender"));
				u.setState(rs.getString("state"));
				u.setCity(rs.getString("city"));
				u.setArea(rs.getString("area"));
				u.setDob(rs.getString("dob"));
				return u;
			}
		}
		try {
			final String query = "SELECT * FROM users WHERE email = ? AND account_type = ?";
			User u = jdbcTemplate.queryForObject(query, new DataMapper(), new Object[] { email, type });
			return u;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public User getUserByEmail(String email) {
		class DataMapper implements RowMapper<User> {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u = new User();
				u.setName(rs.getString("name"));
				u.setEmail(rs.getString("email"));
				u.setPhone(rs.getString("phone"));
				u.setGender(rs.getString("gender"));
				u.setState(rs.getString("state"));
				u.setCity(rs.getString("city"));
				u.setArea(rs.getString("area"));
				u.setDob(rs.getString("dob"));
				return u;
			}
		}
		try {
			final String query = "SELECT * FROM users WHERE email = ?";
			User u = jdbcTemplate.queryForObject(query, new DataMapper(), new Object[] { email });
			return u;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<User> getUserSearch(String state, String city, String area, String email) {
		class DataMapper implements RowMapper<User> {
			public User mapRow(ResultSet rs, int rowNum) throws SQLException {
				User u = new User();
				u.setName(rs.getString("name"));
				u.setEmail(rs.getString("email"));
				u.setPhone(rs.getString("phone"));
				u.setGender(rs.getString("gender"));
				u.setState(rs.getString("state"));
				u.setCity(rs.getString("city"));
				u.setArea(rs.getString("area"));
				u.setDob(rs.getString("dob"));
				return u;
			}
		}
		try {
			final String query = "SELECT * FROM users WHERE state = ? AND city = ? AND area LIKE ? AND email != ?";
			List<User> u = jdbcTemplate.query(query, new DataMapper(),
					new Object[] { state, city, "%" + area + "%", email });
			if (u.isEmpty()) {
				return null;
			}
			return u;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Message> getMessages(String semail, String remail) {
		class DataMapper implements RowMapper<Message> {
			public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
				Message m = new Message();
				m.setId(rs.getInt("id"));
				m.setMessage(rs.getString("msg"));
				m.setsEmail(rs.getString("sid"));
				m.setrEmail(rs.getString("rid"));
				m.setFileName(rs.getString("filename"));
				m.setUdate(rs.getDate("udate").toLocalDate());
				m.setUtime(rs.getString("utime"));
				return m;
			}
		}
		try {
			final String query = "SELECT * FROM peoplemsg WHERE sid = ? AND rid = ?";
			List<Message> m = jdbcTemplate.query(query, new DataMapper(), new Object[] { semail, remail });
			if (m.isEmpty()) {
				return null;
			}
			return m;
		} catch (EmptyResultDataAccessException ex) {
			return null;
		}
	}

	public byte[] getPhoto(String email) {
		class DataMapper implements RowMapper<byte[]> {
			public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getBytes("photo");
			}
		}
		try {
			final String query = "SELECT photo FROM users WHERE email = ?";
			byte[] r = jdbcTemplate.queryForObject(query, new DataMapper(), new Object[] { email });
			return r;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] downloadFile(int id) {
		class DataMapper implements RowMapper<byte[]> {
			public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getBytes("ufile");
			}
		}
		try {
			final String query = "SELECT ufile FROM peoplemsg WHERE id = ?";
			byte[] r = jdbcTemplate.queryForObject(query, new DataMapper(), new Object[] { id });
			return r;
		} catch (EmptyResultDataAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String sendMessage(Message m, MultipartFile file) {
		try {
			String query = "INSERT INTO peoplemsg (sid, rid, msg, filename, ufile, udate, utime) VALUES (?, ?, ?, ?, ?, ?, ?)";
			jdbcTemplate.update(query, new Object[] { m.getsEmail(), m.getrEmail(), m.getMessage(), m.getFileName(),
					file.getInputStream(), m.getUdate(), m.getUtime() });
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "failed";
		}
	}

	public String sendMessageWithoutFile(Message m) {
		try {
			String query = "INSERT INTO peoplemsg (sid, rid, msg, udate, utime) VALUES (?, ?, ?, ?, ?)";
			jdbcTemplate.update(query,
					new Object[] { m.getsEmail(), m.getrEmail(), m.getMessage(), m.getUdate(), m.getUtime() });
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "failed";
		}
	}

}
