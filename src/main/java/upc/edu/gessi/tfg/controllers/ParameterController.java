package upc.edu.gessi.tfg.controllers;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import upc.edu.gessi.tfg.models.Parameter;
import upc.edu.gessi.tfg.models.ParameterIntegration;
import upc.edu.gessi.tfg.models.RequestParameterIntegration;
import upc.edu.gessi.tfg.services.ParameterService;

@RestController
@RequestMapping("/parameters")
public class ParameterController {

    @Autowired
    private ParameterService parameterService;

    @Tag(name = "Parameters CRUD", description = "CRUD operations for parameters")
    @Operation(summary = "Get all parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameters found"),
    })
    @GetMapping
    public ResponseEntity<Iterable<Parameter>> getAllParameters() {
        return ResponseEntity.ok(parameterService.getAllParameters());
    }

    @Tag(name = "Parameters CRUD", description = "CRUD operations for parameters")
    @Operation(summary = "Get a parameter by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameter found"),
            @ApiResponse(responseCode = "404", description = "Parameter not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Parameter> getParameter(@PathVariable String id) {
        Parameter param = parameterService.getParameter(id);
        if (param == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(param);
    }

    @Tag(name = "Parameters CRUD", description = "CRUD operations for parameters")
    @Operation(summary = "Create a parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parameter created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Parameter already exists")
    })
    @PostMapping
    public ResponseEntity<Parameter> createParameter(@RequestBody Parameter param) {
        if (parameterService.getParameter(param.getName()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        parameterService.createParameter(param);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "Parameters CRUD", description = "CRUD operations for parameters")
    @Operation(summary = "Update a parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameter updated"),
            @ApiResponse(responseCode = "404", description = "Parameter not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Parameter> updateParameter(@PathVariable String id, @RequestBody Parameter param) {
        if (parameterService.getParameter(id) == null)
            return ResponseEntity.notFound().build();
        parameterService.updateParameter(id, param);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Parameters CRUD", description = "CRUD operations for parameters")
    @Operation(summary = "Delete a parameter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parameter deleted"),
            @ApiResponse(responseCode = "404", description = "Parameter not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Parameter> deleteParameter(@PathVariable String id) {
        parameterService.deleteParameter(id);
        return ResponseEntity.noContent().build();
    }


    //INTEGRATIONS
    @Tag(name = "Parameters Integrations CRUD", description = "CRUD operations for parameters integrations")
    @Operation(summary = "Get all parameters integrations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameters integrations found"),
    })
    @GetMapping("/integrations")
    public ResponseEntity<Iterable<ParameterIntegration>> getAllParameterIntegrations() {
        return ResponseEntity.ok(parameterService.getAllParameterIntegrations());
    }

    @Tag(name = "Parameters Integrations CRUD", description = "CRUD operations for parameters integrations")
    @Operation(summary = "Get a parameter integration by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameter integration found"),
            @ApiResponse(responseCode = "404", description = "Parameter integration not found")
    })
    @GetMapping("/integrations/{id}")
    public ResponseEntity<ParameterIntegration> getParameterIntegration(@PathVariable String id) {
        ParameterIntegration param = parameterService.getParameterIntegration(id);
        if (param == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(param);
    }

    @Tag(name = "Parameters Integrations CRUD", description = "CRUD operations for parameters integrations")
    @Operation(summary = "Create a parameter integration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Parameter integration created"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Parameter integration already exists")
    })
    @PostMapping("/integrations")
    public ResponseEntity<ParameterIntegration> createParameterIntegration(@RequestBody ParameterIntegration param) {
        if (parameterService.getParameterIntegration(param.getIdentifier()) != null)
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        parameterService.createParameterIntegration(param);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "Parameters Integrations CRUD", description = "CRUD operations for parameters integrations")
    @Operation(summary = "Update a parameter integration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameter integration updated"),
            @ApiResponse(responseCode = "404", description = "Parameter integration not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @PutMapping("/integrations/{id}")
    public ResponseEntity<ParameterIntegration> updateParameterIntegration(@PathVariable String id, @RequestBody ParameterIntegration param) {
        if (parameterService.getParameterIntegration(id) == null)
            return ResponseEntity.notFound().build();
        parameterService.updateParameterIntegration(id, param);
        return ResponseEntity.ok().build();
        
    }

    @Tag(name = "Parameters Integrations CRUD", description = "CRUD operations for parameters integrations")
    @Operation(summary = "Delete a parameter integration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Parameter integration deleted"),
            @ApiResponse(responseCode = "404", description = "Parameter integration not found")
    })
    @DeleteMapping("/integrations/{id}")
    public ResponseEntity<ParameterIntegration> deleteParameterIntegration(@PathVariable String id) {
        if (parameterService.getParameterIntegration(id) == null)
            return ResponseEntity.notFound().build();
        parameterService.deleteParameterIntegration(id);
        return ResponseEntity.noContent().build();
    }

    //USER STORY 3
    @Tag(name = "User stories", description = "User stories operations")
    @Operation(summary = "USER STORY #3: Request source-target parameter integrations for selected app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Parameter integrations found"),
            @ApiResponse(responseCode = "404", description = "Parameter integrations not found")
    })
    @GetMapping("/integrations/request")
    public ResponseEntity<List<List<Object>>> getParameterIntegrationsRequest(@RequestBody RequestParameterIntegration request ) {
        List<List<Object>> paramIntegrations = parameterService.getParameterIntegrations(request.getSourceApp(), request.getSourceFeature(), request.getTargetApp(), request.getTargetFeature());

        if (paramIntegrations == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(paramIntegrations);
    }

    //USER STORY 4
    @Tag(name = "User stories", description = "User stories operations")
    @Operation(summary = "USER STORY #4: Request custom parameters for selected app")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Custom parameters found"),
            @ApiResponse(responseCode = "404", description = "Custom parameters not found")
    })
    @GetMapping("/integrations/request/custom")
    public ResponseEntity<List<String>> getCustomParameters(@RequestBody RequestParameterIntegration request ) {
        List<String> paramIntegrations = parameterService.getCustomParameters(request.getSourceApp(), request.getSourceFeature(), request.getTargetApp(), request.getTargetFeature());

        if (paramIntegrations == null)
            return ResponseEntity.notFound().build();
        
        return ResponseEntity.ok(paramIntegrations);
    }
}
