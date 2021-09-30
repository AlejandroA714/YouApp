package sv.com.udb.services.foo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import sv.com.udb.services.foo.dto.Foo;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;

@Slf4j
@RestController
@RequestMapping(value = "/foos")
public class FooController {
   @GetMapping(value = "/{id}")
   public Foo findOne(@PathVariable Long id) {
      return new Foo(Long.parseLong(randomNumeric(2)), randomAlphabetic(4));
   }

   @GetMapping(value = "")
   public List findAll(Principal principal) {
      LOGGER.info("Principal: {}", principal);
      List fooList = new ArrayList();
      fooList.add(
            new Foo(Long.parseLong(randomNumeric(2)), randomAlphabetic(4)));
      fooList.add(
            new Foo(Long.parseLong(randomNumeric(2)), randomAlphabetic(4)));
      fooList.add(
            new Foo(Long.parseLong(randomNumeric(2)), randomAlphabetic(4)));
      return fooList;
   }

   @ResponseStatus(HttpStatus.CREATED)
   @PostMapping
   public void create(@RequestBody Foo newFoo) {
      LOGGER.info("Foo created");
   }
}
