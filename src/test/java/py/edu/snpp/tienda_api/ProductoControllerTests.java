package py.edu.snpp.tienda_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProductoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void creaCategoriaProductoYPermiteBuscarPorNombre() throws Exception {
        MvcResult categoriaResult = mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"nombre":"Bebidas"}
                                """))
                .andExpect(status().isCreated())
                .andReturn();

        String categoriaId = categoriaResult.getResponse().getContentAsString()
                .replaceAll(".*\"id\":(\\d+).*", "$1");

        mockMvc.perform(post("/api/productos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre":"Cafe molido",
                                  "descripcion":"Paquete de 500 g",
                                  "precio":25000.00,
                                  "stock":12,
                                  "categoriaId":%s
                                }
                                """.formatted(categoriaId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.categoria.nombre", is("Bebidas")));

        mockMvc.perform(get("/api/productos/buscar").param("nombre", "CAFE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre", is("Cafe molido")))
                .andExpect(jsonPath("$[0].categoria.nombre", is("Bebidas")));

        mockMvc.perform(get("/api/productos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoria.nombre", is("Bebidas")));
    }
}
