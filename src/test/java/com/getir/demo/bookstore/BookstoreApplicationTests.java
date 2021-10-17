package com.getir.demo.bookstore;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.getir.demo.bookstore.models.Admin;
import com.getir.demo.bookstore.models.Book;
import com.getir.demo.bookstore.models.Customer;
import com.getir.demo.bookstore.repositories.AdminRepository;
import com.getir.demo.bookstore.repositories.BookRepository;
import com.getir.demo.bookstore.repositories.CustomerRepository;
import com.getir.demo.bookstore.repositories.OrderRepository;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = BookstoreApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
  locations = "classpath:application-IT.properties")
@TestMethodOrder(OrderAnnotation.class)
class BookstoreApplicationTests {

	
	@Autowired
    private MockMvc mvc;
	
	@Autowired
	private CustomerRepository customerRepo;
	
	@Autowired
	private AdminRepository adminRepo;
	
	@Autowired
	private BookRepository bookRepo;
	
	@Autowired
	private OrderRepository orderRepo;
	
	@BeforeAll
	public static void cleanUp() {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
	    MongoDatabase db = mongoClient.getDatabase("bookstore_db_IT");
	    db.drop();
	}
	
	@AfterAll
	public static void tearDown() {
		MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
	    MongoDatabase db = mongoClient.getDatabase("bookstore_db_IT");
	    db.drop();
	}
	
	@Test
	@Order(1)
	public void testRegisterAdmin_thenStatus200() throws Exception {
		mvc.perform(post("/admin/registration").content("{\n" + 
				"    \"name\":\"Admin\"\n" + 
				"}")
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isCreated())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@Order(2)
	public void testRegisterCustomer_thenStatus200() throws Exception {
		mvc.perform(post("/customers/registration").content("{\n" + 
				"    \"name\":\"Arunabh\",\n" + 
				"    \"email\":\"arunabh@gmail.com\"\n" + 
				"}")
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isCreated())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
		
	@Test
	@Order(3)
	public void testRegisterSameCustomer_thenStatus409() throws Exception {
		mvc.perform(post("/customers/registration").content("{\n" + 
				"    \"name\":\"Arunabh\",\n" + 
				"    \"email\":\"arunabh@gmail.com\"\n" + 
				"}")
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isConflict())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
		
	}
	
	@Test
	@Order(4)
	public void testRegisterBook_thenStatus200() throws Exception {
		Admin admin = adminRepo.findByName("Admin");
		mvc.perform(post("/books/registration").content("{\n" + 
				"    \"name\":\"long stories\",\n" + 
				"    \"author\" : \"ruskin bond\",\n" + 
				"    \"price\" : 10.25\n" + 
				"}")
				.header("Authorization", admin.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isCreated())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}
	
	@Test
	@Order(5)
	public void testUpdateBookInventory_thenStatus204() throws Exception {
		Admin admin = adminRepo.findByName("Admin");
		Book book = bookRepo.findBookByName("long stories");
		mvc.perform(put("/books/inventory").content("{\n" + 
				"    \"bookId\" : \""+book.getId()+"\",\n" + 
				"    \"quantity\" : 1\n" + 
				"}")
				.header("Authorization", admin.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isNoContent());	
	}
	
	@Test
	@Order(6)
	public void testUpdateBookInventory_thenStatus400() throws Exception {
		Admin admin = adminRepo.findByName("Admin");
		Book book = bookRepo.findBookByName("long stories");
		mvc.perform(put("/books/inventory").content("{\n" + 
				"    \"bookId\" : \""+book.getId()+"\",\n" + 
				"    \"quantity\" : -1\n" + 
				"}")
				.header("Authorization", admin.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isBadRequest())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}
	
	@Test
	@Order(7)
	public void testgetCustomerOrders_thenStatus200() throws Exception {
		Customer customer = customerRepo.findByEmail("arunabh@gmail.com");
		mvc.perform(get("/customers/orders?page=1&size=10")
				.header("Authorization", customer.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}
	
	@Test
	@Order(8)
	public void testPlaceOrder_thenStatus200() throws Exception {
		Book book = bookRepo.findBookByName("long stories");
		Customer customer = customerRepo.findByEmail("arunabh@gmail.com");
		mvc.perform(post("/orders/create_new").content("[\n" + 
				"    {\n" + 
				"        \"bookId\" : \""+book.getId()+"\",\n" + 
				"        \"quantity\" : 1\n" + 
				"    }\n" + 
				"]")
				.header("Authorization", customer.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isCreated())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@Order(9)
	public void testPlaceOrderOOS_thenStatus400() throws Exception {
		Book book = bookRepo.findBookByName("long stories");
		Customer customer = customerRepo.findByEmail("arunabh@gmail.com");
		mvc.perform(post("/orders/create_new").content("[\n" + 
				"    {\n" + 
				"        \"bookId\" : \""+book.getId()+"\",\n" + 
				"        \"quantity\" : 1\n" + 
				"    }\n" + 
				"]")
				.header("Authorization", customer.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isBadRequest())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@Order(10)
	public void testPlaceOrderBadInput_thenStatus400() throws Exception {
		Book book = bookRepo.findBookByName("long stories");
		Customer customer = customerRepo.findByEmail("arunabh@gmail.com");
		mvc.perform(post("/orders/create_new").content("[\n" + 
				"    {\n" + 
				"        \"bookId\" : \""+book.getId()+"\",\n" + 
				"        \"quantity\" : -1\n" + 
				"    }\n" + 
				"]")
				.header("Authorization", customer.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isBadRequest())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@Order(11)
	public void testGetOrdersById_thenStatus200() throws Exception {
		Admin admin = adminRepo.findByName("Admin");
		com.getir.demo.bookstore.models.Order order = orderRepo.findAll().get(0);
		mvc.perform(get("/orders/"+order.getId())
				.header("Authorization", admin.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}
	
	@Test
	@Order(12)
	public void testGetOrdersById_thenStatus404() throws Exception {
		Admin admin = adminRepo.findByName("Admin");
		com.getir.demo.bookstore.models.Order order = orderRepo.findAll().get(0);
		mvc.perform(get("/orders/"+admin.getId())
				.header("Authorization", admin.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isNotFound())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}
	
	@Test
	@Order(13)
	public void testGetOrdersById_thenStatus401() throws Exception {
		Admin admin = adminRepo.findByName("Admin");
		com.getir.demo.bookstore.models.Order order = orderRepo.findAll().get(0);
		mvc.perform(get("/orders/"+order.getId())
				.header("Authorization", order.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isUnauthorized())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}
	
	@Test
	@Order(14)
	public void testGetOrdersByDate_thenStatus200() throws Exception {
		Admin admin = adminRepo.findByName("Admin");
		mvc.perform(post("/orders/search").content("{\n" + 
				"    \"startDate\" : \"2021-10-14\",\n" + 
				"    \"endDate\" : \"2024-10-17\"\n" + 
				"}")
				.header("Authorization", admin.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}
	
	@Test
	@Order(15)
	public void testGetStatsMonthly_thenStatus200() throws Exception {
		Customer customer = customerRepo.findByEmail("arunabh@gmail.com");
		mvc.perform(get("/stats/orders/monthly")
				.header("Authorization", customer.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isOk())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}
	
	@Test
	@Order(15)
	public void testGetStatsMonthly_thenStatus401() throws Exception {
		Admin admin = adminRepo.findByName("Admin");
		mvc.perform(get("/stats/orders/monthly")
				.header("Authorization", admin.getId().toString())
	      .contentType(MediaType.APPLICATION_JSON))
	      .andExpect(status().isUnauthorized())
	      .andExpect(content()
	      .contentTypeCompatibleWith(MediaType.APPLICATION_JSON));	
	}

}
