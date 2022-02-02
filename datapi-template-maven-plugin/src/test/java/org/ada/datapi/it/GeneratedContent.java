package org.ada.datapi.it;

public class GeneratedContent {

    public static final String MODEL_CLASS_LOMBOK_IT =
            "package com.example.app.somewhere.model.someplace;\n" +
                    "\n" +
                    "import javax.persistence.*;\n" +
                    "\n" +
                    "import java.util.Map;\n" +
                    "import java.util.List;\n" +
                    "import com.example.app.dir1.Example;\n" +
                    "\n" +
                    "import lombok.AllArgsConstructor;\n" +
                    "import lombok.Builder;\n" +
                    "import lombok.Data;\n" +
                    "import lombok.NoArgsConstructor;\n" +
                    "\n" +
                    "@AllArgsConstructor\n" +
                    "@Builder\n" +
                    "@Data\n" +
                    "@NoArgsConstructor\n" +
                    "@Entity\n" +
                    "public class ExampleClass {\n" +
                    "\n" +
                    "    @Id\n" +
                    "    @GeneratedValue(strategy = GenerationType.AUTO)\n" +
                    "    private String key;\n" +
                    "\n" +
                    "    private Map var1;\n" +
                    "\n" +
                    "    private List var2;\n" +
                    "\n" +
                    "    private Example var3;\n" +
                    "\n" +
                    "}\n";

    public static final String MODEL_CLASS_NO_LOMBOK_IT =
            "package com.example.app.somewhere.model.someplace;\n" +
                    "\n" +
                    "import javax.persistence.*;\n" +
                    "\n" +
                    "import java.util.Map;\n" +
                    "import java.util.List;\n" +
                    "import com.example.app.dir1.Example;\n" +
                    "\n" +
                    "@Entity\n" +
                    "public class ExampleClass {\n" +
                    "\n" +
                    "    @Id\n" +
                    "    @GeneratedValue(strategy = GenerationType.AUTO)\n" +
                    "    private String key;\n" +
                    "\n" +
                    "    private Map var1;\n" +
                    "\n" +
                    "    private List var2;\n" +
                    "\n" +
                    "    private Example var3;\n" +
                    "\n" +
                    "    public String getKey() {\n" +
                    "        return this.key;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setKey(String key) {\n" +
                    "        this.key = key;\n" +
                    "    }\n" +
                    "\n" +
                    "    public Map getVar1() {\n" +
                    "        return this.var1;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setVar1(Map var1) {\n" +
                    "        this.var1 = var1;\n" +
                    "    }\n" +
                    "\n" +
                    "    public List getVar2() {\n" +
                    "        return this.var2;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setVar2(List var2) {\n" +
                    "        this.var2 = var2;\n" +
                    "    }\n" +
                    "\n" +
                    "    public Example getVar3() {\n" +
                    "        return this.var3;\n" +
                    "    }\n" +
                    "\n" +
                    "    public void setVar3(Example var3) {\n" +
                    "        this.var3 = var3;\n" +
                    "    }\n" +
                    "\n" +
                    "}\n";

    public static String CONTROLLER_CLASS_IT =
            "package com.example.app.somewhere.controller.someplace;\n" +
                    "\n" +
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.http.HttpStatus;\n" +
                    "import org.springframework.web.bind.annotation.*;\n" +
                    "\n" +
                    "import com.example.app.somewhere.service.someplace.ExampleClassService;\n" +
                    "import com.example.app.somewhere.model.someplace.ExampleClass;\n" +
                    "\n" +
                    "@RestController\n" +
                    "@RequestMapping(\"/exampleClass\")\n" +
                    "public class ExampleClassController {\n" +
                    "\n" +
                    "    private final ExampleClassService exampleClassService;\n" +
                    "\n" +
                    "    @Autowired\n" +
                    "    public ExampleClassController(ExampleClassService exampleClassService) {\n" +
                    "            this.exampleClassService = exampleClassService;\n" +
                    "    }\n" +
                    "\n" +
                    "    @RequestMapping(method = RequestMethod.POST)\n" +
                    "    @ResponseStatus(value = HttpStatus.CREATED)\n" +
                    "    public ExampleClass create(@RequestBody ExampleClass exampleClass) {\n" +
                    "            return this.exampleClassService.create(exampleClass);\n" +
                    "    }\n" +
                    "\n" +
                    "    @RequestMapping(value = \"/{key}\", method = RequestMethod.GET)\n" +
                    "    public ExampleClass read(@PathVariable String key) {\n" +
                    "            return this.exampleClassService.read(key);\n" +
                    "    }\n" +
                    "\n" +
                    "    @RequestMapping(value = \"/{key}\", method = RequestMethod.PUT)\n" +
                    "    public ExampleClass update(@PathVariable String key, @RequestBody ExampleClass exampleClass) {\n" +
                    "            return this.exampleClassService.update(exampleClass);\n" +
                    "    }\n" +
                    "\n" +
                    "    @RequestMapping(value = \"/{key}\", method = RequestMethod.DELETE)\n" +
                    "    public void delete(@PathVariable String key) {\n" +
                    "            this.exampleClassService.delete(key);\n" +
                    "    }\n" +
                    "\n" +
                    "}\n";

    public static String SERVICE_CLASS_IT =
            "package com.example.app.somewhere.service.someplace;\n" +
                    "\n" +
                    "import org.springframework.beans.factory.annotation.Autowired;\n" +
                    "import org.springframework.stereotype.Service;\n" +
                    "\n" +
                    "import com.example.app.somewhere.model.someplace.ExampleClass;\n" +
                    "import com.example.app.somewhere.repository.someplace.ExampleClassRepository;\n" +
                    "\n" +
                    "@Service\n" +
                    "public class ExampleClassService {\n" +
                    "\n" +
                    "    private final ExampleClassRepository exampleClassRepository;\n" +
                    "    \n" +
                    "    @Autowired\n" +
                    "    public ExampleClassService(ExampleClassRepository exampleClassRepository) {\n" +
                    "        this.exampleClassRepository = exampleClassRepository;\n" +
                    "    }\n" +
                    "    \n" +
                    "    public ExampleClass create(ExampleClass exampleClass) {\n" +
                    "        return this.exampleClassRepository.save(exampleClass);\n" +
                    "    }\n" +
                    "    \n" +
                    "    public ExampleClass read(String key) {\n" +
                    "        return this.exampleClassRepository.findOne(key);\n" +
                    "    }\n" +
                    "    \n" +
                    "    public ExampleClass update(ExampleClass exampleClass) {\n" +
                    "        return this.exampleClassRepository.save(exampleClass);\n" +
                    "    }\n" +
                    "    \n" +
                    "    public void delete(String key) {\n" +
                    "        this.exampleClassRepository.delete(key);\n" +
                    "    }\n" +
                    "\n" +
                    "}";

    public static String REPOSITORY_CLASS_IT =
            "package com.example.app.somewhere.repository.someplace;\n" +
                    "\n" +
                    "import org.springframework.data.domain.Page;\n" +
                    "import org.springframework.data.domain.Pageable;\n" +
                    "import org.springframework.data.jpa.domain.Specification;\n" +
                    "import org.springframework.data.repository.CrudRepository;\n" +
                    "\n" +
                    "import com.example.app.somewhere.model.someplace.ExampleClass;\n" +
                    "\n" +
                    "public interface ExampleClassRepository extends CrudRepository<ExampleClass, String> {\n" +
                    "\n" +
                    "    Page<ExampleClass> findAll(Specification<ExampleClass> spec, Pageable pageInfo);\n" +
                    "\n" +
                    "    ExampleClass findOne(Specification<ExampleClass> spec);\n" +
                    "\n" +
                    "}";

}
