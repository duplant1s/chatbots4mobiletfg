package upc.edu.gessi.tfg.controllers;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import upc.edu.gessi.tfg.models.Parameter;
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.models.RequestParameterIntegration;
import upc.edu.gessi.tfg.services.ParameterService;

@RestController
@RequestMapping("/parameters")
public class ParameterController {

    @Autowired
    private ParameterService parameterService;

    @GetMapping
    public ResponseEntity<Iterable<Parameter>> getAllParameters() {
        return ResponseEntity.ok(parameterService.getAllParameters());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parameter> getParameter(@PathVariable String id) {
        Parameter param = parameterService.getParameter(id);
        if (param == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(param);
    }

    @PostMapping
    public ResponseEntity<Parameter> createParameter(@RequestBody Parameter param) {
        parameterService.createParameter(param);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parameter> updateParameter(@PathVariable String id, @RequestBody Parameter param) {
        parameterService.updateParameter(id, param);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Parameter> deleteParameter(@PathVariable String id) {
        parameterService.deleteParameter(id);
        return ResponseEntity.noContent().build();
    }


    //INTEGRATIONS

    @GetMapping("/integrations")
    public ResponseEntity<Iterable<ParameterIntegration>> getAllParameterIntegrations() {
        return ResponseEntity.ok(parameterService.getAllParameterIntegrations());
    }

    @GetMapping("/integrations/{id}")
    public ResponseEntity<ParameterIntegration> getParameterIntegration(@PathVariable String id) {
        ParameterIntegration param = parameterService.getParameterIntegration(id);
        if (param == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(param);
    }

    @PostMapping("/integrations")
    public ResponseEntity<ParameterIntegration> createParameterIntegration(@RequestBody ParameterIntegration param) {
        parameterService.createParameterIntegration(param);
        return ResponseEntity.created(null).build();
    }

    @PutMapping("/integrations/{id}")
    public ResponseEntity<ParameterIntegration> updateParameterIntegration(@PathVariable String id, @RequestBody ParameterIntegration param) {
        parameterService.updateParameterIntegration(id, param);
        return ResponseEntity.ok().build();
        
    }

    @DeleteMapping("/integrations/{id}")
    public ResponseEntity<ParameterIntegration> deleteParameterIntegration(@PathVariable String id) {
        parameterService.deleteParameterIntegration(id);
        return ResponseEntity.noContent().build();
    }

    //USER STORY 3
    @GetMapping("/integrations/request")
    public ResponseEntity<List<List<Object>>> getParameterIntegrationsRequest(@RequestBody RequestParameterIntegration request ) {
        List<List<Object>> paramIntegrations = parameterService.getParameterIntegrations(request.getSourceApp(), request.getSourceFeature(), request.getTargetApp(), request.getTargetFeature());

        if (paramIntegrations == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(paramIntegrations);
    }

    //USER STORY 4
    @GetMapping("/integrations/request/custom")
    public ResponseEntity<List<String>> getCustomParameters(@RequestBody RequestParameterIntegration request ) {
        List<String> paramIntegrations = parameterService.getCustomParameters(request.getSourceApp(), request.getSourceFeature(), request.getTargetApp(), request.getTargetFeature());

        if (paramIntegrations == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(paramIntegrations);
    }
}
