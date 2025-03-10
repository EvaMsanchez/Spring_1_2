package com.eva.curso.springboot.webapp.springboot_web.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eva.curso.springboot.webapp.springboot_web.models.User;
import com.eva.curso.springboot.webapp.springboot_web.models.dto.ParamDto;


@RestController
@RequestMapping("/api/var")
public class PathVariableController 
{
    // Atributos que coincidan con la configuración del properties, inyección de configuraciones
    @Value("${config.username}")
    private String username;

    //@Value("${config.message}")
    //private String message;
    
    @Value("${config.listOfValues}")
    //private String[] listOfValues;
    private List<String> listOfValues;

    @Value("${config.code}")
    private Integer code;

    // Con lenguaje de expresión de Spring (#), es lo mismo pero de forma manual
    @Value("#{'${config.listOfValues}'.toUpperCase().split(',')}")
    private List<String> valueList;

    @Value("#{'${config.listOfValues}'.toUpperCase()}")
    private String valueString;

    @Value("#{${config.valuesMap}}")
    private Map<String, Object> valuesMap;

    @Value("#{${config.valuesMap}.product}")
    private String product;

    @Value("#{${config.valuesMap}.price}")
    private Long price;


    // Objeto Spring Environment (ya lo podemos utilizar) inyección de dependencias
    @Autowired
    private Environment environment;


    // Método para obtener parámetros de la ruta a través de una variable
    @GetMapping("/baz/{message}")
    public ParamDto baz (@PathVariable String message) // mismo nombre que la variable de la ruta
    {
        ParamDto param = new ParamDto();
        param.setMessage(message);
        return param;
    }
    

    @GetMapping("/mix/{product}/{id}")
    public Map<String, Object> mixPathVar(@PathVariable String product, @PathVariable Long id) // mismo nombre que la variable de la ruta
    {
        Map<String, Object> json = new HashMap<>();
        json.put("product", product);
        json.put("id", id);
        return json;
    }


    // Método que realiza una petición POST, se envía en el cuerpo del body no por URL
    @PostMapping("/create")
    public User create(@RequestBody User user)
    {
        user.setName(user.getName().toUpperCase());
        return user; // devuelve el objeto que recibimos JSON pero por POST
    }


    // Método inyectando valores de un archivo properties personalizado
    @GetMapping("/values")
    public Map<String, Object> values(@Value("${config.message}") String message)
    {
        Long code2 = environment.getProperty("config.code", Long.class);

        Map<String, Object> json = new HashMap<>();
        json.put("username", username);
        json.put("code", code);
        json.put("message", message);

        // Otra forma de obtener la información de los properties con Environment (se recibe por defecto como String)
        json.put("message2", environment.getProperty("config.message"));
        json.put("code2", code2);
        // Otra forma de pasar a número entero el code
        //json.put("code2", Integer.valueOf(environment.getProperty("config.code")));
        

        json.put("listOfValues", listOfValues);
        json.put("valueList", valueList);
        json.put("valueString", valueString);
        json.put("valuesMap", valuesMap);
        json.put("product", product);
        json.put("price", price);
        return json;
    }
     
}
