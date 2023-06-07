package upc.edu.gessi.tfg.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import upc.edu.gessi.tfg.models.Feature;
import upc.edu.gessi.tfg.models.FeatureIntegration;
import upc.edu.gessi.tfg.services.FeatureService;

@RequestMapping("/features")
@RestController
public class FeatureController {
    
    @Autowired
    private FeatureService featureService;

    @Tag(name = "Features CRUD", description = "CRUD operations for features")
    @Operation(summary = "Get all features")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Features found"),
    })
    @GetMapping
    public ResponseEntity<List<Feature>> getAllFeatures() {
        return ResponseEntity.ok(featureService.getAllFeatures());
    }

    @Tag(name = "Features CRUD", description = "CRUD operations for features")
    @Operation(summary = "Get a feature by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Features found"),
    })
    @GetMapping("/{id}")
    public ResponseEntity<Feature> getFeature(@PathVariable String id) {
        Feature feature = featureService.getFeatureById(id);
        if (feature == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(feature);
    }

    @Tag(name = "Features CRUD", description = "CRUD operations for features")
    @Operation(summary = "Create a new feature")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Feature created"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Feature already exists")
    })
    @PostMapping
    public ResponseEntity<Feature> createFeature(@RequestBody Feature feature) {
        if (featureService.getFeatureById(feature.getIdentifier()) != null)
            return ResponseEntity.status(409).build();
        featureService.createFeature(feature);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "Features CRUD", description = "CRUD operations for features")
    @Operation(summary = "Update an existing feature")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feature updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Feature not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Feature> updateFeature(@PathVariable String id, @RequestBody Feature feature) {
        if (featureService.getFeatureById(id) == null)
            return ResponseEntity.notFound().build();
        featureService.updateFeature(id, feature);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Features CRUD", description = "CRUD operations for features")
    @Operation(summary = "Delete an existing feature")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Feature deleted"),
        @ApiResponse(responseCode = "404", description = "Feature not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Feature> deleteUser(@PathVariable String id) {
        if (featureService.getFeatureById(id) == null)
            return ResponseEntity.notFound().build();
        featureService.deleteFeature(id);
        return ResponseEntity.noContent().build();
    }

    //INTEGRATIONS

    @Tag(name = "Feature integrations CRUD", description = "CRUD operations for feature integrations")
    @Operation(summary = "Get all feature integrations")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feature integrations found"),
    })
    @GetMapping("/integrations")
    public ResponseEntity<List<FeatureIntegration>> getAllFeatureIntegrations() {
        return ResponseEntity.ok(featureService.getAllFeatureIntegrations());
    }

    @Tag(name = "Feature integrations CRUD", description = "CRUD operations for feature integrations")
    @Operation(summary = "Get a feature integration by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feature integration found"),
        @ApiResponse(responseCode = "404", description = "Feature integration not found")
    })
    @GetMapping("/integrations/{id}")
    public ResponseEntity<FeatureIntegration> getFeatureIntegration(@PathVariable String id) {
        FeatureIntegration featureIntegration = featureService.getFeatureIntegration(id);
        if (featureIntegration == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(featureIntegration);
    }

    @Tag(name = "Feature integrations CRUD", description = "CRUD operations for feature integrations")
    @Operation(summary = "Create a new feature integration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Feature integration created"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "409", description = "Feature integration already exists")
    })
    @PostMapping("/integrations")
    public ResponseEntity<FeatureIntegration> createFeatureIntegration(@RequestBody FeatureIntegration featureIntegration) {
        if (featureService.getFeatureIntegration(featureIntegration.getIdentifier()) != null)
            return ResponseEntity.status(409).build();
        featureService.createFeatureIntegration(featureIntegration);
        return ResponseEntity.created(null).build();
    }

    @Tag(name = "Feature integrations CRUD", description = "CRUD operations for feature integrations")
    @Operation(summary = "Update an existing feature integration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Feature integration updated"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "404", description = "Feature integration not found")
    })
    @PutMapping("/integrations/{id}")
    public ResponseEntity<FeatureIntegration> updateFeatureIntegration(@PathVariable String id, @RequestBody FeatureIntegration featureIntegration) {
        if (featureService.getFeatureIntegration(id) == null)
            return ResponseEntity.notFound().build();
        featureService.updateFeatureIntegration(id, featureIntegration);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "Feature integrations CRUD", description = "CRUD operations for feature integrations")
    @Operation(summary = "Delete an existing feature integration")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Feature integration deleted"),
        @ApiResponse(responseCode = "404", description = "Feature integration not found")
    })
    @DeleteMapping("/integrations/{id}")
    public ResponseEntity<FeatureIntegration> deleteFeatureIntegration(@PathVariable String id) {
        if (featureService.getFeatureIntegration(id) == null)
            return ResponseEntity.notFound().build();
        featureService.deleteFeatureIntegration(id);
        return ResponseEntity.noContent().build();
    }
}
