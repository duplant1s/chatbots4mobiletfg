// package upc.edu.gessi.tfg.controllers;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import upc.edu.gessi.tfg.models.Parameter;
// import upc.edu.gessi.tfg.models.ParameterIntegration;
// import upc.edu.gessi.tfg.services.ParameterService;

// @RestController
// @RequestMapping("/parameters")
// public class ParameterController {

//     @Autowired
//     private ParameterService parameterService;

//     @GetMapping
//     public ResponseEntity<Iterable<Parameter>> getAllParameters() {
//         return ResponseEntity.ok(parameterService.getAllParameters());
//     }

//     @GetMapping("/{id}")
//     public ResponseEntity<Parameter> getUserById(@PathVariable long id) {
//         return parameterService.getParameterById(id)
//             .map(ResponseEntity::ok)
//             .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping
//     public ResponseEntity<Parameter> createUser(@RequestBody Parameter param) {
//         return new ResponseEntity<>(parameterService.createParameter(param), HttpStatus.CREATED);
//     }

//     @PutMapping("/{id}")
//     public ResponseEntity<Parameter> updateUser(@PathVariable long id, @RequestBody Parameter param) {
//         return parameterService.getParameterById(id)
//         .map(existingParam -> {
//             param.setIdentifier(existingParam.getIdentifier());
//             return new ResponseEntity<>(parameterService.createParameter(param), HttpStatus.OK);
//         })
//         .orElse(ResponseEntity.notFound().build());
//     }

//     @DeleteMapping("/{id}")
//     public ResponseEntity<Parameter> deleteUser(@PathVariable long id) {
//         parameterService.deleteParameter(id);
//         return ResponseEntity.noContent().build();
//     }

//     @GetMapping("/integration/{id}")
//     public ResponseEntity<ParameterIntegration> getParameterIntegrationById(@PathVariable long id) {
//         return parameterService.getParameterIntegrationById(id)
//             .map(ResponseEntity::ok)
//             .orElse(ResponseEntity.notFound().build());
//     }

//     @PostMapping("/integration")
//     public ResponseEntity<ParameterIntegration> createParameterIntegration(@RequestBody ParameterIntegration param) {
//         return new ResponseEntity<>(parameterService.createParameterIntegration(param), HttpStatus.CREATED);
//     }

//     @PutMapping("/integration/{id}")
//     public ResponseEntity<ParameterIntegration> updateParameterIntegration(@PathVariable long id, @RequestBody ParameterIntegration param) {
//         return parameterService.getParameterIntegrationById(id)
//         .map(existingParam -> {
//             param.setId(existingParam.getId());
//             return new ResponseEntity<>(parameterService.createParameterIntegration(param), HttpStatus.OK);
//         })
//         .orElse(ResponseEntity.notFound().build());
//     }

//     @DeleteMapping("/integration/{id}")
//     public ResponseEntity<ParameterIntegration> deleteParameterIntegration(@PathVariable long id) {
//         parameterService.deleteParameterIntegration(id);
//         return ResponseEntity.noContent().build();
//     }
// }
