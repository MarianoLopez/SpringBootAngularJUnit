package com.z;


import com.z.models.CallBackMessage;
import static com.z.testUtils.TestUtil.toJson;
import com.z.models.User;
import com.z.testUtils.TestUtil;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)//an available port will be picked at random each time your test runs
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//orden de ejecución de los métodos
public class SpringBootTestApplicationTests {
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private static CallBackMessage callback;
    @Rule
    public TestName name = new TestName();
    
        @Before
        public void setup(){
           //mock all the Spring MVC infrastructure
           mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilter(springSecurityFilterChain).build();
        }
        private void methodNamePrint(){System.out.println("Método: "+name.getMethodName());}
        
        public static RequestPostProcessor userROLE() {return user("TheTesterGuy").roles("USER");}//perfil user
        public static RequestPostProcessor adminROLE() {return user("TheTesterGuy2").roles("ADMIN");}//perfil admin
        
        @Test
        public void a_redirect() throws Exception{
            methodNamePrint();
            //users/{username} sin login = redirección a login form
            mockMvc.perform(get("/users/mariano")).andExpect(status().is3xxRedirection());
        }
   
	@Test
	public void b_getUsersWithUserRole() throws Exception {
        methodNamePrint();
        //GET /users/ con rol USER = http 200
        mockMvc.perform(get("/users/").with(userROLE()))
            .andDo(print())//imprimir log por consola
            .andExpect(status().isOk())//http 200
            .andExpect(content().contentType(APPLICATION_JSON_UTF8));//json object
	}
        
        @Test
	public void b_getUserWithUserRole() throws Exception {
        methodNamePrint();
        //GET /users/{username} con rol USER = http 200
        String username="Mariano";
        mockMvc.perform(get("/users/"+username).with(userROLE()))
            .andDo(print())//imprimir log por consola
            .andExpect(status().isOk())//http 200
            .andExpect(content().contentType(APPLICATION_JSON_UTF8));//json object
	}
        
        @Test
        public void c1_ForbiddenUsersPostWithUserRole() throws Exception{
            methodNamePrint();
            String json = toJson(new User(99L, "testing", "testing", "testing", "testing", true));
            //POST /users/{user} con rol USER = HTTP 403
            mockMvc.perform(post("/users/").with(userROLE()).with(csrf().asHeader())
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json))
            .andDo(print())
            .andExpect(status().isForbidden());
        }
        @Test()
        public void c2_ForbiddenUsersPutWithUserRole() throws Exception{
            methodNamePrint();
            String json = toJson(new User(99L, "testing", "testing", "testing", "testing", true));
            //PUT /users/{user} con rol USER = HTTP 403
            mockMvc.perform(put("/users/").with(userROLE()).with(csrf().asHeader())
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json))
            .andDo(print())
            .andExpect(status().isForbidden());
        }
        @Test
        public void c3_ForbiddenUsersDeleteWithUserRole() throws Exception{
            methodNamePrint();
            String json = toJson(new User(99L, "testing", "testing", "testing", "testing", true));
            //DELETE /users/{user} con rol USER = HTTP 403
            mockMvc.perform(delete("/users/").with(userROLE()).with(csrf().asHeader())
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json))
            .andDo(print())
            .andExpect(status().isForbidden());
        }
        
        @Test
        public void d1_UsersPostWithAdminRole() throws Exception{
            methodNamePrint();
            /*json como string dado que la clase user tiene el atributo password (Access.WRITE_ONLY)
             ,al hacer el parse de user a json se omite el atributo y genera una violación de constraint en db*/
            String json = "{\"id\":\"\",\"username\":\"tester\",\"password\":\"tester\",\"firstName\":\"tester\",\"lastName\":\"tester\",\"state\":true,\"profileList\":[{\"id\":2,\"type\":\"USER\"}]}";
            //POST /users/{user} con rol ADMIN = HTTP 200
            MvcResult result = mockMvc.perform(post("/users/").with(adminROLE()).with(csrf().asHeader())
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json("{\"message\":\"Usuario añadido\"}")).andReturn();
            //Usuario previamente insertado "necesidad de saber ID"
            callback = TestUtil.jsonToObj(result.getResponse().getContentAsString(), CallBackMessage.class);
        }
        
        @Test
        public void d2_UsersPutWithAdminRole() throws Exception{
            methodNamePrint();
            //PUT /users/{user} con rol ADMIN = HTTP 200
            mockMvc.perform(put("/users/").with(adminROLE()).with(csrf().asHeader())
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(toJson(callback.data)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json("{\"message\":\"Usuario modificado\"}"));
        }
        
        @Test
        public void d3_UsersDeleteWithAdminRole() throws Exception{
            methodNamePrint();
            //DELETE /users/{user} con rol ADMIN = HTTP 200
            mockMvc.perform(delete("/users/").with(adminROLE()).with(csrf().asHeader())
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(toJson(callback.data)))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json("{\"message\":\"Usuario eliminado\"}"));
        }
        
        @Test
        public void d4_UsersPostFieldErrorWithAdminRole() throws Exception{
            methodNamePrint();
            //POST /users/{user} con rol ADMIN, firstName = "" --> HTTP 400
            String json = "{\"id\":\"\",\"username\":\"tester\",\"password\":\"tester\",\"firstName\":\"\",\"lastName\":\"tester\",\"state\":true,\"profileList\":[{\"id\":2,\"type\":\"USER\"}]}";
            String json_error_esperado = "{\"message\":\"Error en insert: firstName: Debe tener entre 2 y 30 caracteres. \"}";
            mockMvc.perform(post("/users/").with(adminROLE()).with(csrf().asHeader())
                    .contentType(APPLICATION_JSON_UTF8)
                    .content(json))
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andExpect(content().json(json_error_esperado));
        }
             

}
