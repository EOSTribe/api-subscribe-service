package api.subscribe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class HealthController {

    @RequestMapping(value = "/health", method = RequestMethod.POST, produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Object> health() {
        return new ResponseEntity<>("{\"health\":\"OK\"}", HttpStatus.OK);
    }

}
