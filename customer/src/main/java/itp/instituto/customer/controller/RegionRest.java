package itp.instituto.customer.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import itp.instituto.customer.entity.Customer;
import itp.instituto.customer.entity.Region;
import itp.instituto.customer.service.CustomerService;
import itp.instituto.customer.service.RegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/regions")
public class RegionRest {

    @Autowired
    RegionService regionService;

    // -------------------Retrieve All Regions--------------------------------------------

    @GetMapping
    public ResponseEntity<List<Region>> listAll() {
        log.info("Listando regiones");
        List<Region> regions =  new ArrayList<>();
            regions = regionService.findAll();
            if (regions.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
        return  ResponseEntity.ok(regions);
    }

    // -------------------Retrieve Single Region------------------------------------------

    @GetMapping(value = "/{id}")
    public ResponseEntity<Region> getRegion(@PathVariable("id") long id) {
        log.info("Fetching Customer with id {}", id);
        Region region = regionService.getRegion(id);
        if (  null == region) {
            log.error("Region with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        return  ResponseEntity.ok(region);
    }

    // -------------------Create a Customer-------------------------------------------

    @PostMapping
    public ResponseEntity<Region> createRegion( @RequestBody Region region, BindingResult result) {
        log.info("Creating Region : {}", region);
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, this.formatMessage(result));
        }

       Region regionDB = regionService.create (region);

        return  ResponseEntity.status( HttpStatus.CREATED).body(regionDB);
    }

    // ------------------- Update a Region ------------------------------------------------

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id, @RequestBody Region region) {
        log.info("Updating Region with id {}", id);

        Region regionDB = regionService.getRegion(id);

        if ( null ==regionDB ) {
            log.error("Unable to update. Region with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        region.setId(id);
        regionDB=regionService.update(region);
        return  ResponseEntity.ok(regionDB);
    }

    // ------------------- Delete a Region-----------------------------------------

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        log.info("Deleting Region with id {}", id);

        Region region = regionService.getRegion(id);
        if ( null == region ) {
            log.error("Unable to delete. Region with id {} not found.", id);
            return  ResponseEntity.notFound().build();
        }
        regionService.delete(region);
        return (ResponseEntity<Void>) ResponseEntity.noContent();
    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String>  error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;

                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

}
